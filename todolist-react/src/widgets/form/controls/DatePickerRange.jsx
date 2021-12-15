import * as React from 'react';
import TextField from '@mui/material/TextField';
import AdapterDateFns from '@mui/lab/AdapterDateFns';
import LocalizationProvider from '@mui/lab/LocalizationProvider';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import MobileDateRangePicker from '@mui/lab/MobileDateRangePicker';

function DatePickerRange(props) {
    const {label, name, value, onChange} = props;
    const convertToDifferentParam = (name, value) => ({
        target: {
            name, value
        }
    });

    return (
        <LocalizationProvider dateAdapter={AdapterDateFns}>
            <Stack spacing={3}>
                <MobileDateRangePicker
                    startText={`${label} start`}
                    value={value}
                    onChange={(newValue) => onChange(convertToDifferentParam(name, newValue))}
                    renderInput={(startProps, endProps) => (
                        <React.Fragment>
                            <TextField size={"small"} {...startProps} />
                            <Box sx={{ mx: 2 }}> to </Box>
                            <TextField size={"small"} {...endProps} />
                        </React.Fragment>
                    )}
                />
            </Stack>
        </LocalizationProvider>
    );
}

export default DatePickerRange;
