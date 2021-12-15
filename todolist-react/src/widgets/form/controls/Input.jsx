import React from 'react';
import {TextField} from "@mui/material";

function Input(props) {
    const {value, label, name, onChange, error=null, ...other} = props;
    return (
        <TextField variant='outlined'
                   label={label}
                   name={name}
                   value={value}
                   onChange={onChange}
                   {...(error && {error: true, helperText: error})}
                   {...other}
        />
    );
}

export default Input;
