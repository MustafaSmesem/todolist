import React, {useState} from 'react';
import {IconButton, Menu, MenuItem, Typography} from "@mui/material";
import FlagIcon from '@mui/icons-material/Flag';


export function DropDownPriority(props) {
    const {selectedPriority, priorities, handleChange} = props;
    const [priority, setPriority] = useState(priorities.find(priority => priority.id === selectedPriority));
    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);
    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };
    const handleSelect = (priority) => {
        setPriority(priority);
        handleChange(priority.id, 'priority');
        handleClose();
    };

    const handleClose = () => {
        setAnchorEl(null);
    }
    return (
        <div>
            <IconButton
                id="basic-button"
                aria-controls="basic-menu"
                aria-haspopup="true"
                aria-expanded={open ? 'true' : undefined}
                onClick={handleClick}
                color={priority.name}
            >
                <Typography variant={"subtitle2"}>{priority.name}</Typography>
                <FlagIcon />
            </IconButton>
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                MenuListProps={{
                    'aria-labelledby': 'basic-button',
                }}
            >
                {priorities?.map(priority => (
                    <MenuItem onClick={() => handleSelect(priority)} key={priority.id}>
                        <Typography variant={"subtitle2"} style={{color: "silver", fontWeight: "bold"}}>{priority.name}</Typography>
                        <FlagIcon color={priority.name}/>
                    </MenuItem>
                ))}
            </Menu>
        </div>
    );

}
