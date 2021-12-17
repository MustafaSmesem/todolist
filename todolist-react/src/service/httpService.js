import axios from "axios";
import {toast} from 'react-toastify';
import {apiURL} from "../config.json";

const apiEndpoint = process.env.REACT_APP_API_URL || apiURL;


axios.interceptors.response.use(null, error => {
   const expectedError = error.response && error.response.status >= 400 && error.response.status < 500;
   if (!expectedError) {
       console.log("Logging the error", error);
       toast.error("An unexpected error occurred.");
   }
   return Promise.reject(error);
});

function setBearerToken(token) {
    if (token !== undefined) axios.defaults.headers.common['Authorization'] = "Bearer " + token;
}

// eslint-disable-next-line import/no-anonymous-default-export
export default {
    get: axios.get,
    post: axios.post,
    put: axios.put,
    delete: axios.delete,
    setBearerToken,
    apiEndpoint,
}
