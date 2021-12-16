import React, {useState} from 'react';
import {Chip, Menu, MenuItem, Typography} from "@mui/material";


export function DropDownGroup(props) {
    const {groups, selectedGroup, handleChange} = props;
    const [group, setGroup] = useState(groups.find(group => group.id === selectedGroup));
    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);
    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const handleSelect = (group) => {
        setGroup(group);
        handleChange(group.id, 'group');
        handleClose();
    };

    const handleClose = () => {
        setAnchorEl(null);
    }

    return (
        <div>
            <Chip
                id="basic-button"
                aria-controls="basic-menu"
                aria-haspopup="true"
                aria-expanded={open ? 'true' : undefined}
                color="secondary"
                variant={"outlined"}
                onClick={handleClick}
                label={group && group.title.toUpperCase()}
                size={"small"}
            />
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                MenuListProps={{
                    'aria-labelledby': 'basic-button',
                }}
            >
                {groups?.map(group => (
                    <MenuItem onClick={() => handleSelect(group)} key={group.id}>
                        <Typography variant={"subtitle2"} color="secondary" >{group && group.title.toUpperCase()}</Typography>
                    </MenuItem>
                ))}
            </Menu>
        </div>
    );

}

