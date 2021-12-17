import http from "./httpService";
import {useUser} from "../context/userContextProvider";


const url = http.apiEndpoint + '/authenticate';

function useAuthService() {
    const {userInfo, setUserInfo} = useUser();

    const authenticate = async (credential) => {
        const {data} = await http.post(url , credential);
        saveUserData(data);
    }

    const logout = () => {
        setUserInfo(null);
        localStorage.removeItem('userInfo')
    }

    const saveUserData = (data) => {
        setUserInfo(data);
        localStorage.setItem('userInfo', JSON.stringify(data));
        data !== null && http.setBearerToken(data.token);
    }


    return {
        authenticate,
        logout,
        saveUserData,
        userInfo,
    };
}

export {
    useAuthService,
}
