import http from "./httpService";

const url = http.apiEndpoint + '/todo';


export function getAll() {
    return http.get(url + '/all');
}

export function saveTodo(todo) {
    return http.post(url + '/save', todo);
}

export function deleteTodo(todoId) {
    return http.delete(url + '/' + todoId);
}

// eslint-disable-next-line import/no-anonymous-default-export
export default {
    getAll,
    saveTodo,
    deleteTodo,
}
