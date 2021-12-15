import React, {useState} from 'react';
import {FormControl, FormHelperText, IconButton, InputAdornment, InputLabel, OutlinedInput} from "@mui/material";
import {Visibility, VisibilityOff} from "@mui/icons-material";

function InputPassword(props) {
    const [showPassword, setShowPassword] = useState(false);
    const idNum = Math.random();
    const {value, label, name, onChange, error=null} = props;

    const handleClickShowPassword = () => {
        setShowPassword(!showPassword);
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    return (
        <FormControl sx={{ m: 1, width: '25ch' }} variant="outlined">
            <InputLabel htmlFor={`outlined-adornment-password-${idNum}`}>{label}</InputLabel>
            <OutlinedInput
                id={`outlined-adornment-password-${idNum}`}
                type={showPassword ? 'text' : 'password'}
                value={value}
                onChange={onChange}
                name={name}
                endAdornment={
                    <InputAdornment position="end">
                        <IconButton
                            aria-label="toggle password visibility"
                            onClick={handleClickShowPassword}
                            onMouseDown={handleMouseDownPassword}
                            edge="end"
                        >
                            {showPassword ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                    </InputAdornment>
                }
                label="Password"
            />
            {error && (<FormHelperText error id="error-text">
                {error}
            </FormHelperText>)}
        </FormControl>
    );
}

export default InputPassword;
