import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './app/App';
import reportWebVitals from './reportWebVitals';
import {BrowserRouter} from "react-router-dom";
import {ThemeProvider} from "@mui/material/styles";
import theme from './style';
import {UserProvider} from "./context/userContextProvider";


ReactDOM.render(
    <React.StrictMode>
        <BrowserRouter>
            <UserProvider>
                <ThemeProvider theme={theme}>
                    <App/>
                </ThemeProvider>
            </UserProvider>
        </BrowserRouter>
    </React.StrictMode>,
    document.getElementById('root')
);

reportWebVitals();
