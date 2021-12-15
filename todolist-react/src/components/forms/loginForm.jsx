import React from 'react';
import {toast} from "react-toastify";
import {Form, UseForm} from "../../widgets/form/Form";
import {Divider, Grid, Typography} from "@mui/material";
import Controls from "../../widgets/form/controls/controls";
import {useAuthService} from "../../service/authService";
import {useNavigate} from "react-router-dom";

export const formValues = {
    username: '',
    password: '',
}


function LoginForm(props) {

    const {authenticate} = useAuthService();
    const navigate = useNavigate();
    const validate = (fieldValues = values) => {
        let temp = {...errors};
        if ('username' in fieldValues)
            temp.username = (/$.+^|.+@.+\..+/).test(fieldValues.username) ? "" : "This is not a valid email";
        if ('password' in fieldValues)
            temp.password = fieldValues.password.length > 3 ? "" : `Password must be at least 4 character`;

        setErrors({
            ...temp
        });

        if (fieldValues === values)
            return Object.values(temp).every(x => x === "");
    }

    const {values, errors, setErrors, handleInputChange} = UseForm(formValues, true, validate);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (validate()) {
            try {
                await authenticate(values);
                setTimeout(()=>{
                    navigate("/");
                }, 1000)
            } catch ({response}) {
                if (response && (response.status === 400 || response.status === 401)) {
                    toast.error(response.data.message);
                    const errs = {...errors};
                    errs.password = response.data.message;
                    setErrors(errs);
                }
            }
        }
    }

    return (
        <Form onSubmit={handleSubmit}>
            <Typography variant={"h4"}>Sign In</Typography>
            <Divider style={{marginBlock: 20}}/>
            <Grid container justifyContent={"space-around"} >
                <Controls.Input
                    label='Email'
                    name='username'
                    value={values.username}
                    onChange={handleInputChange}
                    error={errors.username}
                    required={true}
                />
                <Controls.InputPassword
                    label='Password'
                    name='password'
                    value={values.password}
                    onChange={handleInputChange}
                    error={errors.password}
                />
                <Controls.Button
                    label="Sign In"
                    type="submit"
                    style={{width: '80%', marginTop: 20}}
                />
            </Grid>

        </Form>
    );
}

export default LoginForm;
