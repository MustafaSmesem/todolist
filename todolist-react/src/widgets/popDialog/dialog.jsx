import React from 'react';
import {Box, Dialog as MuiDialog, DialogContent, DialogTitle, IconButton} from "@mui/material";
import {useStyle} from "./style";
import CloseIcon from "@mui/icons-material/Close";

export function Dialog(props) {
    const classes = useStyle();
    const {title, children, openPopup, setOpenPopup, onKeyDown} = props;

    return (
        <MuiDialog open={openPopup} classes={{paper: classes.dialogWrapper}} onKeyDown={onKeyDown}>
            <DialogTitle>
                <Box display={"flex"} justifyContent={"space-between"}>
                    {title}
                    <IconButton aria-label="close" color={"primary"} onClick={() => setOpenPopup(false)}>
                        <CloseIcon />
                    </IconButton>
                </Box>
            </DialogTitle>
            <DialogContent dividers>
                {children}
            </DialogContent>
        </MuiDialog>
    );
}
