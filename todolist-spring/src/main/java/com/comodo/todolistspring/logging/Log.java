package com.comodo.todolistspring.logging;

import com.comodo.todolistspring.utils.Strings;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Formatter;
import java.util.logging.*;
import java.util.stream.Collectors;

public final class Log {

    public static final Thread.UncaughtExceptionHandler EXCEPTION_HANDLER = new ExceptionLoggerHandler();

    static Level currentLevel = Level.WARNING;

    private static final Logger logger = Logger.getLogger("spook");

    static final CUSTOM_FORMAT formatter = new CUSTOM_FORMAT();

    private static final Map<File, FileHandler> fileHandlers = new ConcurrentHashMap<>();

    private static final Map<String, LogLevel> classLevelMap = new ConcurrentHashMap<>();

    static {
        logger.setUseParentHandlers(false);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(new CUSTOM_FORMAT());
        ch.setLevel(currentLevel);
        logger.addHandler(ch);
        Thread.setDefaultUncaughtExceptionHandler(EXCEPTION_HANDLER);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                fileHandlers.values().forEach(FileHandler::close);
            }
        });
    }

    private Log() {
    }


    private static class ExceptionLoggerHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            Log.error("Exception occurred in thread :" + t.getName(), e);
            e.printStackTrace();
        }
    }

    private static void setLevel(Level level) {
        logger.setLevel(level);
        for (Handler handler : logger.getHandlers()) {
            handler.setLevel(level);
        }
        currentLevel = level;
    }

    public enum LogLevel {
        TRACE(Level.FINER),
        DEBUG(Level.FINE),
        INFO(Level.INFO),
        WARNING(Level.WARNING),
        ERROR(Level.SEVERE);

        Level level;

        LogLevel(Level level) {
            this.level = level;
        }
    }

    public static void trace(String message, Object... params) {
        log(Level.FINER, message, params);
    }

    public static void trace(boolean condition, String message, Object... params) {
        if (condition) {
            log(Level.FINER, message, params);
        }
    }

    public static void trace(Object object) {
        log(Level.FINER, object.toString());
    }

    public static void debug(String message, Object... params) {
        log(Level.FINE, message, params);
    }

    public static void debug(boolean condition, String message, Object... params) {
        if (condition) {
            log(Level.FINE, message, params);
        }
    }

    public static void debug(Object object) {
        log(Level.FINE, object.toString());
    }

    public static void info(String message, Object... params) {
        log(Level.INFO, message, params);
    }

    public static void info(boolean condition, String message, Object... params) {
        if (condition) {
            log(Level.INFO, message, params);
        }
    }

    public static void info(Object object) {
        log(Level.INFO, object.toString());
    }

    public static void warn(String message, Object... params) {
        log(Level.WARNING, message, params);
    }

    public static void exception(Throwable t) {
        logger.log(Level.SEVERE, "Exception occurred.", t);
    }

    public static void warn(boolean condition, String message, Object... params) {
        if (condition) {
            log(Level.WARNING, message, params);
        }
    }

    public static void warn(Object object) {
        log(Level.WARNING, object.toString());
    }

    public static void error(String message, Object... params) {
        log(Level.SEVERE, message, params);
    }

    public static void error(boolean condition, String message, Object... params) {
        if (condition) {
            log(Level.SEVERE, message, params);
        }
    }

    public static void error(Object object) {
        log(Level.SEVERE, object.toString());
    }

    public static void setError() {
        setLevel(Level.SEVERE);
    }

    public static void setWarn() {
        setLevel(Level.WARNING);
    }

    public static void setInfo() {
        setLevel(Level.INFO);
    }

    public static void setDebug() {
        setLevel(Level.FINE);
    }

    public static void setTrace() {
        setLevel(Level.FINER);
    }

    public static void addFileHandler(Path path) throws IOException {
        if (fileHandlers.containsKey(path.toFile())) {
            Log.info("Log File %s already exist. Appending.", path.toFile());
            return;
        }
        final FileHandler handler = new FileHandler(path.toFile().getAbsolutePath(), true);
        handler.setFormatter(formatter);
        handler.setLevel(currentLevel);
        logger.addHandler(handler);
        fileHandlers.put(path.toFile(), handler);
    }

    public static List<Path> getCurrentLogFiles() {
        return fileHandlers.keySet().stream().map(File::toPath).collect(Collectors.toList());
    }

    public static void flushFileHandlers() {
        fileHandlers.values().stream().filter(Objects::nonNull).forEach(Handler::flush);
    }

    public static void removeHandler(Path path) {
        if (fileHandlers.containsKey(path)) {
            FileHandler handler = fileHandlers.remove(path);
            logger.removeHandler(handler);
        }
    }

    public static synchronized void log(Level level, String message, Object... params) {
        final int stackPositionOfCaller = 2;
        StackTraceElement caller = new Throwable().getStackTrace()[stackPositionOfCaller];
        String className = caller.getClassName();

        boolean logIt = false;

        Level temp = currentLevel;
        if (classLevelMap.containsKey(className)) {
            Log.setLevel(classLevelMap.get(className).level);
        }

        if (logger.isLoggable(level))
            logIt = true;

        if (!logIt)
            return;

        Throwable thrown = null;
        if (params.length > 0) {
            Object last = params[params.length - 1];
            if (last instanceof Throwable) {
                if (params.length > 1) {
                    Object[] subParams = new Object[params.length - 1];
                    System.arraycopy(params, 0, subParams, 0, subParams.length);
                    params = subParams;
                }
                thrown = (Throwable) last;
            }
        }
        LogRecord record = new LogRecord(level, message);
        record.setLoggerName(logger.getName());
        record.setSourceClassName(className);
        record.setSourceMethodName(caller.getMethodName());
        record.setThrown(thrown);
        record.setParameters(params);
        logger.log(record);
        Log.setLevel(temp);

    }

    static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);

    static Map<Level, String> levelShortStringMap = new HashMap<>();

    static {
        levelShortStringMap.put(Level.FINEST, "FINT");
        levelShortStringMap.put(Level.FINER, "FINR");
        levelShortStringMap.put(Level.FINE, "FINE");
        levelShortStringMap.put(Level.CONFIG, "CONF");
        levelShortStringMap.put(Level.OFF, "LOFF");
        levelShortStringMap.put(Level.ALL, "LALL");
        levelShortStringMap.put(Level.WARNING, "WARN");
        levelShortStringMap.put(Level.INFO, "INFO");
        levelShortStringMap.put(Level.SEVERE, "DANG");
    }

    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_WHITE = "\u001B[38m";

    static Map<Level, String> levelShortColorMap = new HashMap<>();

    static {
        levelShortColorMap.put(Level.FINEST, "\u001B[32m");
        levelShortColorMap.put(Level.FINER, "\u001B[32m");
        levelShortColorMap.put(Level.FINE, "\u001B[32m");
        levelShortColorMap.put(Level.CONFIG, "\u001B[34m");
        levelShortColorMap.put(Level.OFF, "\u001B[35m");
        levelShortColorMap.put(Level.ALL, "\u001B[37m");
        levelShortColorMap.put(Level.WARNING, "\u001B[33m");
        levelShortColorMap.put(Level.INFO, "\u001B[37m");
        levelShortColorMap.put(Level.SEVERE, "\u001B[31m");
    }

    private static String padIfNecessary(String name, int length) {
        if (name.length() < length) {
            return Strings.rightPad(name, length);
        }
        return name;
    }

    private static class CUSTOM_FORMAT extends Formatter {

        @Override
        public String format(LogRecord record) {
            synchronized (this) {

                StringBuilder sb = new StringBuilder();
                sb.append(ANSI_CYAN);
                sb.append(padIfNecessary(format.format(new Date()), 24));
                sb.append(ANSI_WHITE);
                sb.append("| ");
                sb.append(levelShortColorMap.get(record.getLevel()));
                sb.append(padIfNecessary(levelShortStringMap.get(record.getLevel()), 5));
                sb.append(ANSI_WHITE);
                sb.append("| ");
                sb.append(levelShortColorMap.get(record.getLevel()));
                Object parameters[] = record.getParameters();

                boolean multiLine = false;
                if (record.getMessage().indexOf('\n') > 0)
                    multiLine = true;

                // generate Exception String if available
                String throwStr = "";
                if (record.getThrown() != null) {
                    Throwable t = record.getThrown();
                    StringWriter sw = new StringWriter();
                    try (PrintWriter pw = new PrintWriter(sw)) {
                        t.printStackTrace(pw);
                    }
                    throwStr = sw.toString();
                }

                if (!multiLine) {
                    if (parameters == null || parameters.length == 0) {
                        sb.append(padIfNecessary(record.getMessage(), 100));
                    } else {
                        try {
                            sb.append(padIfNecessary(String.format(Locale.ENGLISH, record.getMessage(), parameters), 100));
                        } catch (IllegalFormatException e) {
                            sb.append("Log Format Error: ")
                                    .append(record.getMessage())
                                    .append(" With Parameters: ")
                                    .append(Joiner.on(",").join(parameters));
                        }
                    }
                    sb.append(ANSI_WHITE);
                    sb.append("| ");
                    sb.append(levelShortColorMap.get(record.getLevel()));
                    sb.append(Strings.subStringAfterLast(record.getSourceClassName(), "."))
                            .append("#");
                    sb.append(record.getSourceMethodName());
                    sb.append("\n");
                } else {
                    sb.append(" \u2193 | ")
                            .append(Strings.subStringAfterLast(record.getSourceClassName(), "."))
                            .append("#");
                    sb.append(record.getSourceMethodName());
                    sb.append("\n");
                    for (String s : Splitter.on("\n").split(record.getMessage())) {
                        sb.append("    ").append(s).append("\n");
                    }
                }
                if (throwStr.length() > 0) {
                    sb.append("\n");
                    for (String s : Splitter.on("\n").split(throwStr)) {
                        sb.append(s).append("\n");
                    }
                }
                return sb.toString();
            }
        }
    }

    public static Path configureLogFile(Path _logFile, boolean _ignoreFileLogging) throws IOException {

        List<Path>  currentLogPaths = Log.getCurrentLogFiles();
        if (_ignoreFileLogging) {
            if(currentLogPaths.size()>0) {
                Log.info("File logging is disabled. Removing handlers %s", currentLogPaths.toString());
                currentLogPaths.forEach(Log::removeHandler);
            }
            return null;
        }

        if (currentLogPaths.size() > 0) {
            if (_logFile != null) {
                Log.warn("There is already a log file in use. %s", Log.getCurrentLogFiles().toString());
            }
            return currentLogPaths.get(0);
        }

        if (_logFile == null) {
            Path logRootDir = Paths.get(".").normalize().resolve("log");
            Files.createDirectories(logRootDir);
            SimpleDateFormat df = new SimpleDateFormat("yyMMdd-HHmmss", Locale.ENGLISH);
            String uniqueName = df.format(new Date());
            _logFile = new File(logRootDir.toFile(), uniqueName + ".log").toPath();
        } else {
            com.google.common.io.Files.createParentDirs(_logFile.toFile());
        }
        Log.addFileHandler(_logFile);
        Log.info("Logs are being recorded to : %s", _logFile);
        return _logFile;
    }

}
