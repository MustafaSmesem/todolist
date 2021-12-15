import React from 'react';
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import {ListItem as MuiListItem} from '@mui/material';

function ListItem(props) {
    const {label, children, onClick} = props;
    return (
        <MuiListItem button onClick={onClick}>
            <ListItemIcon>
                {children}
            </ListItemIcon>
            <ListItemText primary={label} />
        </MuiListItem>
    );
}

export default ListItem;
