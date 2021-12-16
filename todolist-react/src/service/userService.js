import http from "./httpService";
import {apiEndpoint} from "../config.json";

const url = apiEndpoint + '/users';

export function register(user) {
    return http.post(url + '/register', user);
}

export function getAll() {
    return http.get(url + '/getAll');
}

export default {
    getAll,
    register,
}
