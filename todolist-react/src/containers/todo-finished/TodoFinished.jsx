import React, {useContext, useEffect} from 'react';
import {useStyle} from "./style";
import TodoContext from "../../context/todoContext";
import {UseTable} from "../../widgets/table/Table";
import {Chip, Divider, IconButton, TableBody, TableCell, TableRow, Typography} from "@mui/material";
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ClearIcon from "@mui/icons-material/Clear";
import DateRangeOutlinedIcon from "@mui/icons-material/DateRangeOutlined";
import todoService from '../../service/todosService';
import {toast} from "react-toastify";


const headCells = [
    {id: 'state', label: 'Reactivate', disableSorting: true},
    {id: 'description', label: 'Description'},
    {id: 'completionDate', label: 'Completion Date'},
    {id: 'delete', label: 'delete', disableSorting: true},
];
const tableName = 'cTodo';


function TodoFinished(props) {
    const classes = useStyle();
    const {todos, setTodos} = useContext(TodoContext);
    const {Table, TableHead, TablePagination, recordAfterPagingAndSorting, handleSort} = UseTable(todos?.filter(record => record.state), headCells, {fn: items => items}, tableName);
    const {onDelete} = props;


    useEffect(() => {
        handleSort(localStorage.getItem(tableName + 'TableSortLabel') || 'completionDate');
    }, []);

    const handleCheckElement = async (record) => {
        const index = todos.indexOf(record);
        record.state = false;
        record.completionDate = new Date();
        if (~index) {
            todos[index] = record;
            setTodos([...todos]);

            try {
                await todoService.saveTodo(record);
                toast.success(`Todo [${record.description}] has been activated`);
            } catch (error) {
                toast.error("Something get wrong cannot reactivate the todo");
                record.state = true;
                todos[index] = record;
                setTodos([...todos]);
            }
        } else {
            toast.error("Cannot find the todo in this id ... please try later");
        }
    }

    return (
        <>
            <Typography variant="h5">Completed Tasks</Typography>
            <Divider style={{marginBottom: 30, marginTop: 10}}/>
            {recordAfterPagingAndSorting().length > 0
                ?(<>
                    <Table>
                        <TableHead/>
                        <TableBody>
                            {
                                recordAfterPagingAndSorting()?.map(record => (
                                    <TableRow key={record.id}>
                                        <TableCell>
                                            <IconButton color={"green"} classes={{root: classes.checkButton}} onClick={() => handleCheckElement(record)}>
                                                <CheckCircleIcon/>
                                            </IconButton>
                                        </TableCell>
                                        <TableCell align={"center"}>
                                            <Typography variant="subtitle2">{record.description}</Typography>
                                        </TableCell>
                                        <TableCell>
                                            <Chip
                                                label={record.completionDate.toDateString()}
                                                icon={<DateRangeOutlinedIcon/>}
                                                size={"small"}
                                                color={"info"}
                                                style={{paddingInline: 10}}
                                                variant={"outlined"}
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <IconButton onClick={() => onDelete(record)}>
                                                <ClearIcon color="red" />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                ))
                            }
                        </TableBody>
                    </Table>
                    <TablePagination/>
                </>)
                : <Typography variant={"subtitle2"} color={"primary"}>There are no completed todos yet</Typography>
            }

        </>
    );
}

export default TodoFinished;
