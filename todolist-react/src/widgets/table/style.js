import {makeStyles} from "@mui/styles";

const useStyle = makeStyles(theme => ({
    table: {
        marginTop: theme.spacing(3),
        '& thead th': {
            fontWeight: '600',
            color: '#fff',
            backgroundColor: theme.palette.primary.main,
        },
        '& tbody th': {
            fontWeight: '300'
        },
        '& tbody tr:hover': {
            backgroundColor: '#fffbf2',
            cursor: 'pointer'
        }
    }
}));

export default useStyle;
