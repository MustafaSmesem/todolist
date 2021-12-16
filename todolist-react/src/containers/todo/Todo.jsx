import React, {useState, useEffect} from 'react';
import {Box, Chip, Divider, IconButton, TableBody, TableCell, TableRow, Typography} from "@mui/material";
import {UseTable} from "../../widgets/table/Table";
import CircleOutlinedIcon from '@mui/icons-material/CircleOutlined';
import {useStyle} from './style';
import ClearIcon from '@mui/icons-material/Clear';
import {Dialog, DropDownGroup, DropDownPriority} from "../../widgets/widgets";
import DateRangeOutlinedIcon from "@mui/icons-material/DateRangeOutlined";
import Controls from "../../widgets/form/controls/controls";
import AddIcon from "@mui/icons-material/Add";
import {useTodo} from "../../context/todoContextProvider";
import {useGroup} from "../../context/groupContextProvider";
import ModeEditOutlinedIcon from "@mui/icons-material/ModeEditOutlined";
import TodoForm from "../../components/forms/todoForm";

import todoService from '../../service/todosService';
import {toast} from "react-toastify";

const priorities = [
    {id: 1, name: 'p1'},
    {id: 2, name: 'p2'},
    {id: 3, name: 'p3'},
    {id: 4, name: 'p4'},
]
const allPriority = {id: 0, name: 'all'};

const headCell = [
    {id: 'state', label: '', disableSorting: true},
    {id: 'description', label: 'Task'},
    {id: 'dueDate', label: 'Due Date'},
    {id: 'group', label: 'Group'},
    {id: 'priority', label: 'Priority'},
    {id: 'actions', label: '', disableSorting: true},
];

const tableName = 'todo';


function Todo(props) {
    const classes = useStyle();
    const {todos, setTodos} = useTodo();
    const {groups} = useGroup();
    const [filterItems, setFilterItems] = useState({dueDate: [null, null], group: '-1', priority: 0});
    const [filterFn, setFilterFn] = useState({fn: items => items});
    const [openPopup, setOpenPopup] = useState(false);
    const [newTodo, setNewTodo] = useState(null);

    const {onGroupChange, onDelete} = props;

    const {
        Table,
        TableHead,
        recordAfterPagingAndSorting,
        handleSort,
    } = UseTable(todos?.filter(record => !record.state), headCell, filterFn, tableName);

    useEffect(() => {
        handleSort(localStorage.getItem(tableName + 'TableSortLabel') || 'dueDate');
    }, []);


    const handleCheckElement = async (record) => {
        const index = todos.indexOf(record);
        record.state = true;
        record.completionDate = new Date();
        if (~index) {
            todos[index] = record;
            setTodos([...todos]);

            try {
                await todoService.saveTodo(record);
                toast.success(`Todo [${record.description}] has been checked`);
            } catch (error) {
                toast.error("Something get wrong cannot check the todo");
                record.state = false;
                todos[index] = record;
                setTodos([...todos]);
            }
        } else {
            toast.error("Cannot find the todo in this id ... please try later");
        }

    }

    const handleFilter = (value, name) => {
        const filters = {...filterItems, [name]: value};
        setFilterItems(filters);
        setFilterFn({
            fn: items => {
                return applyFilters(items, filters);
            }
        });
    }

    const applyFilters = (items, filters) => {
        const {group, priority, dueDate} = filters;
        return dateFilter(priorityFilter(groupFilter(items, group), priority), dueDate);
    }

    const groupFilter = (items, group) => {
        if (group !== '-1') items = items.filter(item => item.group === group);
        return items;
    }

    const priorityFilter = (items, priority) => {
        if (priority !== 0) items = items.filter(item => item.priority === priority);
        return items;
    }

    const dateFilter = (items, dueDate) => {
        if (dueDate[0] !== null) items = items.filter(item => item.dueDate.getDate() >= dueDate[0].getDate());
        if (dueDate[1] !== null) items = items.filter(item => item.dueDate.getDate() <= dueDate[1].getDate());
        return items;
    }

    const handleEditTodo = (todo) => {
        setNewTodo(todo);
        setOpenPopup(true);
    }

    const handleSubmit = async (todo, oldGroupId) => {
        if (todo.id != null) {
            const oldTodo = todos.find(t => t.id === todo.id);
            if (oldTodo.description === todo.description &&
                oldTodo.priority === todo.priority &&
                oldGroupId === todo.group &&
                oldTodo.dueDate === todo.dueDate) {

                toast.info(`No changes has been applied to this todo [${todo.description}]`);
                setOpenPopup(false);
                return;
            }
        }
        try {
            const {data: newTodo} = await todoService.saveTodo(todo);
            newTodo.dueDate = new Date(newTodo.dueDate);
            newTodo.completionDate = new Date(newTodo.completionDate);

            let type = 'added';
            let newTodos = [...todos];
            if (todo.id != null) {
                newTodos = todos.filter(r => r.id !== todo.id);
                type = 'updated';
                setNewTodo(null);
                await onGroupChange(oldGroupId, -1);
            }
            setTodos([
                ...newTodos,
                newTodo
            ]);
            await onGroupChange(newTodo.group, 1);
            setOpenPopup(false);
            toast.success(`Todo [${todo.description}] has been ${type} successfully`);
        } catch (error) {
            toast.error(error.toString());
        }
    }

    const handleChangeGroupType = async (groupId, record) => {
        if(groupId !== record.group) {
            const index = todos.indexOf(record);
            const oldGroup = record.group;
            record.group = groupId;
            if (~index) {
                todos[index] = record;
                setTodos([...todos]);
                try {
                    await todoService.saveTodo(record);
                    toast.success(`Todo [${record.description}] group has been changed`);
                    await onGroupChange(oldGroup, -1);
                    await onGroupChange(groupId, 1);
                } catch (error) {
                    toast.error("Something get wrong cannot change the group");
                    record.group = oldGroup;
                    todos[index] = record;
                    setTodos([...todos]);
                }
            } else {
                toast.error("Cannot find the todo in this id ... please try later");
            }
        } else {
            toast.info(`This todo [${record.description}] already assigned to this group`);
        }
    }

    const handleChangePriorityType = async (priorityId, record) => {
        const index = todos.indexOf(record);
        const oldPriority = record.priority;
        record.priority = priorityId;
        if (~index) {
            todos[index] = record;
            setTodos([...todos]);

            try {
                await todoService.saveTodo(record);
                toast.success(`Todo [${record.description}] Priority has been changed to ${priorityId}`);
            } catch (error) {
                toast.error("Something get wrong cannot change the priority");
                record.priority = oldPriority;
                todos[index] = record;
                setTodos([...todos]);
            }
        } else {
            toast.error("Cannot find the todo in this id ... please try later");
        }
    }

    return (
        <>
            <Box display={"flex"} justifyContent={"space-between"}>
                <Typography variant={"h5"}>Todo List</Typography>
                <Controls.Button
                    label="Add Task"
                    variant="outlined"
                    size="small"
                    startIcon={<AddIcon/>}
                    onClick={() => handleEditTodo({
                        description: '',
                        dueDate: new Date(),
                        priority: 4,
                        group: groups[0].id
                    })}
                />
            </Box>
            <Divider style={{marginBottom: 30, marginTop: 10}}/>
            {todos.filter(todo => !todo.state).length > 0 &&
            <Box display={{xs: "block", md: "flex"}} justifyContent={"space-around"} alignItems={"center"}>
                <Box m={2}>
                    <Controls.DatePickerRange
                        label={'Due Date'}
                        value={filterItems.dueDate}
                        onChange={(e) => handleFilter(e.target.value, 'dueDate')}
                    />
                </Box>
                <Box display={"flex"} justifyContent={"space-around"} alignItems={"center"}>
                    <Box m={2}>
                        <DropDownGroup selectedGroup={filterItems.group}
                                       groups={[{id: '-1', title: 'All Groups'}, ...groups]}
                                       handleChange={handleFilter}/>
                    </Box>
                    <Box m={2}>
                        <DropDownPriority selectedPriority={0} priorities={[allPriority, ...priorities]}
                                          handleChange={handleFilter}/>
                    </Box>
                </Box>

            </Box>}
            {recordAfterPagingAndSorting().length > 0
                ? (<>
                    <Table>
                        <TableHead/>
                        <TableBody>
                            {
                                recordAfterPagingAndSorting()?.map((record) => (
                                    <TableRow key={record.id}>
                                        <TableCell>
                                            <IconButton classes={{root: classes.checkButton}}
                                                        onClick={() => handleCheckElement(record)}
                                                        color={record.dueDate.getDate() < new Date().getDate() ? "error" : "info"}>
                                                <CircleOutlinedIcon/>
                                            </IconButton>
                                        </TableCell>
                                        <TableCell style={{width: "50%"}}>{record.description}</TableCell>
                                        <TableCell>
                                            <Chip
                                                label={record.dueDate.toDateString()}
                                                icon={<DateRangeOutlinedIcon/>}
                                                size={"small"}
                                                color={record.dueDate.getDate() < new Date().getDate() ? "error" : "success"}
                                                style={{paddingInline: 10}}
                                                variant={"outlined"}
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <DropDownGroup selectedGroup={record.group} groups={groups}
                                                           handleChange={(groupId) => handleChangeGroupType(groupId, record)}
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <DropDownPriority selectedPriority={record.priority} priorities={priorities}
                                                              handleChange={(priorityId) => handleChangePriorityType(priorityId, record)}
                                            />
                                        </TableCell>
                                        <TableCell>
                                            <IconButton onClick={() => handleEditTodo(record)}>
                                                <ModeEditOutlinedIcon color="blue"/>
                                            </IconButton>
                                            <IconButton onClick={() => onDelete(record)}>
                                                <ClearIcon color="red"/>
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                ))
                            }
                        </TableBody>
                    </Table>
                </>)
                : <Typography variant={"subtitle2"} color={"primary"}>There are no todos</Typography>
            }
            <Dialog
                openPopup={openPopup}
                setOpenPopup={setOpenPopup}
                title={newTodo ? "Update Todo" : "Add New Task"}
            >
                <TodoForm todo={newTodo} groups={groups} priorities={priorities} handleSubmit={handleSubmit}/>
            </Dialog>

        </>
    );
}

export default Todo;
