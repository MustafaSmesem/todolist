import React, {useEffect} from 'react';
import {Container, Paper} from "@mui/material";
import {Navigate, Route, Routes} from "react-router-dom";
import useStyle from "./style";
import {SideBar} from "../../components/components";
import {AdminPage, GroupPage, LoginPage, TodoFinishedPage, TodoPage} from "../containers";
import {toast} from "react-toastify";
import groupsService from "../../service/groupsService";
import todosService from "../../service/todosService";
import {useUser} from "../../context/userContextProvider";
import {useGroup} from "../../context/groupContextProvider";
import {useTodo} from "../../context/todoContextProvider";


function Main() {
    const classes = useStyle();
    const {userInfo} = useUser();
    const {groups, setGroups} = useGroup();
    const {todos, setTodos} = useTodo();

    useEffect(() => {
        async function fetchGroupsAndTodos() {
            try {
                const {data: groupData} = await groupsService.getAll();
                setGroups(groupData);
                console.log({groupData})
                const {data: todoData} = await todosService.getAll();
                // eslint-disable-next-line array-callback-return
                todoData.map( todo => {
                    todo.dueDate = new Date(todo.dueDate);
                    todo.completionDate = new Date(todo.completionDate);
                });
                setTodos(todoData);
            } catch (error) {
                toast.error(error.toString());
            }
        }
        setTimeout(()=>{
            userInfo && fetchGroupsAndTodos();
        }, 1000)
    }, [userInfo]);

    const handleGroupTodosCountChange = async (groupId, amount) => {
        const index = groups.map(e => e.id).indexOf(groupId);
        try {
            groups[index].todosCount = groups[index].todosCount + amount;
            await groupsService.saveGroup(groups[index]);
            setGroups([...groups]);
        } catch (error) {
            toast.error("Cannot update group todos count, something went wrong");
        }
    }

    const handleTodoDelete = async (todo) => {
        try {
            await todosService.deleteTodo(todo.id);
            setTodos(todos.filter(record => record.id !== todo.id));
            await handleGroupTodosCountChange(todo.group, -1);
            toast.success(`Todo [${todo.description}] has been deleted successfully`);
        } catch (error) {
            toast.error(`Something get wrong cannot delete this todo`);
        }
    }

    return (
        <Container>
            <SideBar>
                <Paper className={classes.pageContent}>
                    <Routes>
                        <Route path="/login" element={userInfo ? <Navigate replace to={"/"}/> : <LoginPage/>}/>
                        <Route path="/group" element={userInfo ? <GroupPage/> : <Navigate replace to={"/login"}/>}/>
                        <Route path="/admin" element={userInfo ? (userInfo.admin ? <AdminPage/> : <div>Unauthorized .. you are not an admin</div>) : <Navigate replace to={"/login"}/>}/>
                        <Route path="/todo-finish" element={userInfo ? <TodoFinishedPage onDelete={handleTodoDelete}/> : <Navigate replace to={"/login"}/>}/>
                        <Route path="/" element={userInfo ? <TodoPage onGroupChange={handleGroupTodosCountChange} onDelete={handleTodoDelete}/> : <Navigate replace to={"/login"}/>}/>
                        <Route path="*" element={<div>Not Found 404</div>}/>
                    </Routes>
                </Paper>
            </SideBar>
        </Container>
    );
}

export default Main;
