import http from "./httpService";

const url = http.apiEndpoint + '/users';

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
