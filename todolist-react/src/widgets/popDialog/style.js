import {makeStyles} from "@mui/styles";

export const useStyle = makeStyles(theme => ({
    dialogWrapper: {
        padding: theme.spacing(2),
        position: "absolute",
        top: theme.spacing(5),
        width: 1000
    },
    closeButton: {
        position: "absolute",
        top: 10,
        right: 10
    }
}));
