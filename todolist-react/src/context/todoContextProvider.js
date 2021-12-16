import React, {useContext, useState} from "react";

const TodoContext = React.createContext([]);
TodoContext.displayName = "TodoContext";


function TodoProvider({children}) {
    const [todos, setTodos] = useState([]);

    return (
        <TodoContext.Provider value={{todos, setTodos}}>
            {children}
        </TodoContext.Provider>
    );
}

function useTodo() {
    const context = useContext(TodoContext);
    if (context === undefined)
        throw new Error('useTodo must be used within a TodoProvider');
    return context;
}

export {
    TodoProvider,
    useTodo,
};
