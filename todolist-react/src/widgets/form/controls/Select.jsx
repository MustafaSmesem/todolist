import React from 'react';
import {FormControl, FormHelperText, InputLabel, MenuItem, Select as MuiSelect} from "@mui/material";

function Select(props) {
    const {label, name, value, onChange, options, error = null} = props;

    return (
        <FormControl variant="outlined"
                     {...(error && {error: true})}
        >
            <InputLabel>{label}</InputLabel>
            <MuiSelect
                label={label}
                name={name}
                value={value}
                onChange={onChange}
            >
                {
                    options?.map((option) => (
                        <MenuItem value={option.id} key={option.id}>{option.title}</MenuItem>
                    ))
                }
            </MuiSelect>
            {error && <FormHelperText>{error}</FormHelperText>}
        </FormControl>
    );
}

export default Select;
