import {makeStyles} from "@mui/styles";

export const useStyle = makeStyles(theme => ({
    toolbar: {
        display: 'flex',
        justifyContent: 'space-between',
    },
    searchInput: {
        width: '75%'
    },
    groupInput: {
        width: '100%',
    },
    fullButton: {
        width: '100%',
    }

}));
