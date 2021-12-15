import React from 'react';
import {FormControl, FormControlLabel, FormLabel, Radio, RadioGroup as MuiRadioGroup} from "@mui/material";

function RadioGroup(props) {
    const {label, name, value, onChange, options} = props;
    return (
        <FormControl>
            <FormLabel>{label}</FormLabel>
            <MuiRadioGroup row
                           name={name}
                           value={value}
                           onChange={onChange}
            >
                {
                    options?.map((opt) => (
                        <FormControlLabel control={<Radio/>} label={opt.label} value={opt.id} key={opt.id}/>))
                }
            </MuiRadioGroup>
        </FormControl>
    );
}

export default RadioGroup;
