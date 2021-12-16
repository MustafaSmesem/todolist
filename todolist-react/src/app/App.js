import './App.css';
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css'
import {Main} from "../containers/containers";
import React, {useEffect} from "react";
import {useAuthService} from "../service/authService";
import {GroupProvider} from "../context/groupContextProvider";
import {TodoProvider} from "../context/todoContextProvider";

function App() {

    const {saveUserData} = useAuthService();

    useEffect(() => {
        const data = JSON.parse(localStorage.getItem('userInfo'));
        saveUserData(data);
    }, []);

    return (
        <>
            <ToastContainer/>
            <GroupProvider>
                <TodoProvider>
                    <Main/>
                </TodoProvider>
            </GroupProvider>
        </>
    );
}

export default App;
