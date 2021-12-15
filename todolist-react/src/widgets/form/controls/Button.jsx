import React from 'react';
import {Button as MuiButton} from '@mui/material';
import {makeStyles} from "@mui/styles";

const useStyle = makeStyles(theme => ({
   root: {
       margin: theme.spacing(1),
   }
}));

function Button(props) {
    const classes = useStyle();
    const {label, size, color, variant, onClick, ...other} = props;
    return (
        <MuiButton
            classes={{root: classes.root}}
            variant={variant || 'contained'}
            size={size || 'large'}
            color={color || 'primary'}
            onClick={onClick}
            {...other}
        >{label}</MuiButton>
    );
}

export default Button;
