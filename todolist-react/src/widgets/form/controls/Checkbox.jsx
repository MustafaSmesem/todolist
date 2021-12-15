import React from 'react';
import {FormControlLabel, FormGroup, Checkbox as MuiCheckbox, FormControl} from "@mui/material";

function Checkbox(props) {
    const {label, name, value, onChange} = props;
    const convertToDifferentParam = (name, value) => ({
        target: {
            name, value
        }
    });
    return (
        <FormControl>
            <FormGroup>
                <FormControlLabel control={<MuiCheckbox value={value} name={name} onChange={(e) => onChange(convertToDifferentParam(name, e.target.checked))}/>} label={label}/>
            </FormGroup>
        </FormControl>
    );
}

export default Checkbox;
