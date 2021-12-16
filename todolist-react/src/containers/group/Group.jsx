import React, {useState, useEffect} from 'react';
import {UseTable} from "../../widgets/table/Table";
import Controls from "../../widgets/form/controls/controls";
import {Divider, IconButton, InputAdornment, TableBody, TableCell, TableRow, Toolbar} from "@mui/material";
import {useStyle} from "./style";
import SearchRoundedIcon from '@mui/icons-material/SearchRounded';
import AddIcon from '@mui/icons-material/Add';
import GroupAddIcon from '@mui/icons-material/GroupAdd';
import {Dialog} from "../../widgets/widgets";
import {useGroup} from "../../context/groupContextProvider";
import ClearIcon from "@mui/icons-material/Clear";
import ModeEditOutlinedIcon from "@mui/icons-material/ModeEditOutlined";
import {toast} from "react-toastify";
import groupService from '../../service/groupsService';


const headCells = [
    {id: 'title', label: 'Group Name'},
    {id: 'todosCount', label: 'Assigned Todo Count'},
    {id: 'actions', label: 'Actions', disableSorting: true},
];

const tableName = 'group';


function Group() {

    const {groups, setGroups} = useGroup();
    const [groupName, setGroupName] = useState("");
    const [groupId, setGroupId] = useState(null);
    const [error, setError] = useState(null);
    const [filterFn, setFilterFn] = useState({fn: items => items});
    const classes = useStyle();
    const [openPopup, setOpenPopup] = useState(false);



    const {Table, TableHead, TablePagination, recordAfterPagingAndSorting, handleSort} = UseTable(groups, headCells, filterFn, tableName);

    const handleSearch = e => {
        let target = e.target;
        setFilterFn({
            fn: items => {
                if (target.value.length < 1) return items;
                else return items.filter(x => x.title.toLowerCase().includes(target.value))
            }
        });
    }

    useEffect(() => {
        handleSort(localStorage.getItem(tableName + 'TableSortLabel') || 'completionDate');
    }, []);

    const validate = (value) => {
        if (value.length < 3) {
            setError("Group name must be more than 2 characters");
            return false;
        }
        else if(groups.some( e => e.title === value)) {
            setError("This group name is already exist");
            return false;
        }
        else setError("");
        return true;
    }

    const handleInputChange = e => {
        const {value} = e.target;
        setGroupName(value);
        validate(value);
    }

    async function submit() {
        if (validate(groupName)) {
            try {
                const obj = {title: groupName};
                if (groupId != null) obj.id = groupId;
                const {data: group} = await groupService.saveGroup(obj);
                let type = 'added';
                let newGroups = [...groups];
                if (groupId != null) {
                    newGroups = groups.filter(r => r.id !== groupId);
                    type = 'updated';
                    setGroupId(null);
                }
                setGroups([
                    ...newGroups,
                    group
                ]);
                setGroupName("");
                setOpenPopup(false);
                toast.success(`Group [${groupName}] has been ${type} successfully`);
            } catch (error) {
                toast.error(error.toString());
            }

        }
    }

    function handleEdit(record) {
        setGroupName(record.title);
        setGroupId(record.id);
        setOpenPopup(true);
    }

    async function handleDelete(group) {
        if (group.todosCount > 0)
            toast.warn(`You cannot remove this group it's assigned to ${group.todosCount} Todo`);
        else if(groups.length < 2)
            toast.warn(`You cannot remove this group it's the last group`);
        else {
            try {
                await groupService.deleteGroup(group.id);
                setGroups(groups.filter(record => record.id !== group.id));
                toast.success(`Group [${group.title}] has been deleted successfully`);
            } catch (error) {
                toast.error(error.toString());
            }
        }
    }

    return (
        <>
            <h3>Group Management</h3>
            <Divider style={{marginBottom: 30, marginTop: 10}}/>
            <Toolbar className={classes.toolbar}>
                <Controls.Input
                    label="Search Group"
                    className={classes.searchInput}
                    InputProps={{
                        startAdornment: (
                            <InputAdornment position="start" children={<SearchRoundedIcon />}/>
                        )
                    }}
                    onChange={handleSearch}
                />
                <Controls.Button
                    label="Add Group"
                    variant="outlined"
                    startIcon={<AddIcon />}
                    onClick={()=>setOpenPopup(true)}
                />
            </Toolbar>
            <Table>
                <TableHead />
                <TableBody>
                    {
                        recordAfterPagingAndSorting()?.map( record => (
                            <TableRow key={record.id}>
                                <TableCell style={{width:"50%"}} align={"center"}>{record.title}</TableCell>
                                <TableCell align={"center"}>{record.todosCount}</TableCell>
                                <TableCell>
                                    <IconButton onClick={() => handleEdit(record)}>
                                        <ModeEditOutlinedIcon color="blue" />
                                    </IconButton>
                                    <IconButton onClick={() => handleDelete(record)}>
                                        <ClearIcon color="red" />
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))
                    }
                </TableBody>
            </Table>
            <TablePagination />
            <Dialog
                openPopup={openPopup}
                setOpenPopup={setOpenPopup}
                title={groupId ? "Update group" : "Add new group"}
                style={{width: 1000}}
            >
                <Controls.Input
                    label={'Group Name'}
                    InputProps={{
                        startAdornment: (
                            <InputAdornment position="start" children={<GroupAddIcon />}/>
                        )
                    }}
                    className={classes.groupInput}
                    value={groupName}
                    onChange={handleInputChange}
                    error={error}
                    required={true}
                />
                <br/><br/>
                <Controls.Button
                    className={classes.fullButton}
                    label="Submit"
                    onClick={submit}
                />
            </Dialog>
        </>
    );
}

export default Group;
