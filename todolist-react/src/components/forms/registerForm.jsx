import React from 'react';
import {toast} from "react-toastify";
import {Form, UseForm} from "../../widgets/form/Form";
import {Divider, Grid, Typography} from "@mui/material";
import Controls from "../../widgets/form/controls/controls";
import {register} from "../../service/userService";
import {useNavigate} from "react-router-dom";
import {useAuthService} from "../../service/authService";
import groupService from '../../service/groupsService';

export const formValues = {
    name: '',
    surname: '',
    username: '',
    password: '',
}


function RegisterForm() {
    const {saveUserData} = useAuthService();
    const navigate = useNavigate();
    const validate = (fieldValues = values) => {
        let temp = {...errors};

        if ('name' in fieldValues)
            temp.name = fieldValues.name ? (
                fieldValues.name.length > 2 ? "" : "Name must be more than 2 characters"
            ) : "This field is required";
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

    const {values, errors, setErrors, handleInputChange, resetForm} = UseForm(formValues, true, validate);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (validate()) {
            try {
                const {data} = await register(values);
                saveUserData(data);
                await groupService.saveGroup({title: 'Default'}).then(
                    navigate("/")
                );
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
            <Typography variant={"h4"}>Register</Typography>
            <Divider style={{marginBlock: 20}}/>
            <Grid container justifyContent={"center"}>
                <Controls.Input
                    label='Name'
                    name='name'
                    value={values.name}
                    onChange={handleInputChange}
                    error={errors.name}
                    required={true}
                />
                <Controls.Input
                    label='Surname'
                    name='surname'
                    value={values.surname}
                    onChange={handleInputChange}
                />
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
                <Grid item justifyContent={"space-around"} style={{marginBlock: 20}}>
                    <Controls.Button
                        label="Register"
                        type="submit"
                    />
                    <Controls.Button
                        label="reset"
                        variant="outlined"
                        onClick={resetForm}
                        style={{marginLeft: 20}}
                    />
                </Grid>
            </Grid>

        </Form>
    );
}

export default RegisterForm;
