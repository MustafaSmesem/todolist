import './App.css';
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css'
import {Main} from "../containers/containers";
import {useEffect} from "react";
import {useAuthService} from "../service/authService";

function App() {

    const {saveUserData} = useAuthService();

    useEffect(() => {
        const data = JSON.parse(localStorage.getItem('userInfo'));
        saveUserData(data);
    }, []);

    return (
        <>
            <ToastContainer/>
            <Main/>
        </>
    );
}

export default App;
