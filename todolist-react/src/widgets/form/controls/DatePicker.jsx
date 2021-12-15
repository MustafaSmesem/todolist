import React from 'react';
import LocalizationProvider from "@mui/lab/LocalizationProvider";
import DateAdapter from '@mui/lab/AdapterDateFns';
import {DatePicker as MuiDatePicker} from '@mui/lab';
import {TextField} from "@mui/material";


function DatePicker(props) {
    const {label, name, value, onChange} = props;
    const convertToDifferentParam = (name, value) => ({
        target: {
            name, value
        }
    });

    return (
        <LocalizationProvider dateAdapter={DateAdapter}>
            <MuiDatePicker
                label={label}
                value={value}
                onChange={(date) => onChange(convertToDifferentParam(name, date))}
                name={name}
                renderInput={(params) => <TextField size='small' {...params} />}
            />
        </LocalizationProvider>
    );
}

export default DatePicker;
