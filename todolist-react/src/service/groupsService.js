import http from "./httpService";
import {apiEndpoint} from "../config.json";

const url = apiEndpoint + '/group';


export function getAll() {
    return http.get(url + '/all');
}

export function saveGroup(group) {
    return http.post(url + '/save', group);
}

export function deleteGroup(groupId) {
    return http.delete(url + '/' + groupId);
}

// eslint-disable-next-line import/no-anonymous-default-export
export default {
    getAll,
    saveGroup,
    deleteGroup,
}
