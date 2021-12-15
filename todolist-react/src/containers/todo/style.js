import {makeStyles} from "@mui/styles";

export const useStyle = makeStyles(theme => ({
    checkButton: {
        "&:hover, &.Mui-focusVisible": {
            color: theme.palette.p2.main,
        },
        "&:active, &.Mui-focusVisible": {
            color: theme.palette.green.main,
        }
    },
    toolbar: {
        display: 'flex',
        justifyContent: 'space-between',
    },
}))
