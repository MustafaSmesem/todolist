import React, {useEffect, useState} from 'react';
import {UseTable} from "../../widgets/table/Table";
import userService from '../../service/userService';
import {toast} from "react-toastify";
import {Divider, IconButton, TableBody, TableCell, TableRow, Typography} from "@mui/material";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import CircleOutlinedIcon from "@mui/icons-material/CircleOutlined";
import ModeEditOutlinedIcon from "@mui/icons-material/ModeEditOutlined";
import ClearIcon from "@mui/icons-material/Clear";

const headCells = [
    {id: 'name', label: 'Name'},
    {id: 'surname', label: 'Surname'},
    {id: 'username', label: 'Email'},
    {id: 'admin', label: 'Admin'},
    {id: 'actions', label: 'Actions', disableSorting: true},
];
const tableName = 'users';

function Administration() {

    const [users, setUsers] = useState([]);
    const {Table, TableHead, TablePagination, recordAfterPagingAndSorting, handleSort} = UseTable(users, headCells, {fn: items => items}, tableName);

    useEffect(() => {
        const getAllUsers = async () => {
            try {
                const {data} = await userService.getAll();
                setUsers(data);
            } catch (error) {
                toast.error(error.toString());
            }
        }
        getAllUsers();
        handleSort(localStorage.getItem(tableName + 'TableSortLabel') || 'name');
    }, []);


    return (
        <>
            <Typography variant="h5">Administration Page</Typography>
            <Divider style={{marginBottom: 30, marginTop: 10}}/>
            {recordAfterPagingAndSorting().length > 0
                ?(<>
                    <Table>
                        <TableHead/>
                        <TableBody>
                            {
                                recordAfterPagingAndSorting()?.map(record => (
                                    <TableRow key={record.id}>
                                        <TableCell align={"center"}>
                                            <Typography variant="subtitle2">{record.name}</Typography>
                                        </TableCell>
                                        <TableCell align={"center"}>
                                            <Typography variant="subtitle2">{record.surname}</Typography>
                                        </TableCell>
                                        <TableCell align={"center"}>
                                            <Typography variant="subtitle2">{record.username}</Typography>
                                        </TableCell>
                                        <TableCell align={"center"}>
                                            {record.admin ?
                                                <IconButton color={"green"}>
                                                    <CheckCircleIcon/>
                                                </IconButton>:
                                                <IconButton color={"green"}>
                                                    <CircleOutlinedIcon/>
                                                </IconButton>
                                            }
                                        </TableCell>
                                        <TableCell align={"center"}>
                                            <IconButton>
                                                <ModeEditOutlinedIcon color="blue"/>
                                            </IconButton>
                                            <IconButton>
                                                <ClearIcon color="red"/>
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                ))
                            }
                        </TableBody>
                    </Table>
                    <TablePagination/>
                </>)
                : <Typography variant={"subtitle2"} color={"primary"}>There are no users in this table</Typography>
            }
        </>
    );
}

export default Administration;
