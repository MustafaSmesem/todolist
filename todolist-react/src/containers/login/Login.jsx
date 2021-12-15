import React from 'react';
import {Divider, Grid} from "@mui/material";
import {LoginForm, RegisterForm} from "../../components/components";

function Login(props) {

    return (
        <Grid container display={"flex"} justifyContent={"space-between"} alignContent={"center"}>
            <Grid item xs={12} md={5}>
                <LoginForm />
            </Grid>
            <Grid item >
                <Divider orientation={"vertical"} style={{color: "silver", fontWeight: "bold"}}>Or</Divider>
            </Grid>
            <Grid item xs={12} md={5}>
                <RegisterForm />
            </Grid>
        </Grid>
    );
}

export default Login;
