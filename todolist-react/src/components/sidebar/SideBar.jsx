import * as React from 'react';
import PropTypes from 'prop-types';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import Drawer from '@mui/material/Drawer';
import IconButton from '@mui/material/IconButton';
import List from '@mui/material/List';
import MenuIcon from '@mui/icons-material/Menu';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import FormatListNumberedIcon from '@mui/icons-material/FormatListNumbered';
import PlaylistAddCheckIcon from '@mui/icons-material/PlaylistAddCheck';
import GroupWorkIcon from '@mui/icons-material/GroupWork';
import LogoutIcon from '@mui/icons-material/Logout';
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import ListItem from "./ListItem";
import NotificationsIcon from '@mui/icons-material/Notifications';
import SettingsIcon from '@mui/icons-material/Settings';
import {useNavigate} from "react-router-dom";
import {useAuthService} from "../../service/authService";


const drawerWidth = 240;

function ResponsiveDrawer(props) {

    const {userInfo, logout} = useAuthService();

    const {window} = props;
    const [mobileOpen, setMobileOpen] = React.useState(false);

    const navigate = useNavigate();

    const handleDrawerToggle = () => {
        setMobileOpen(!mobileOpen);
    };

    const handleLogout = () => {
        logout();
        navigate("/login");
    }

    const drawer = (
        <div>
            <Toolbar>
                <IconButton color="primary">
                    <NotificationsIcon/>
                </IconButton>
                {userInfo && userInfo.admin &&
                <IconButton color="blue">
                    <SettingsIcon/>
                </IconButton>}
            </Toolbar>
            <Divider/>
            <List>
                <ListItem label="TodoList" onClick={() => navigate("/")}>
                    <FormatListNumberedIcon color={"p3"}/>
                </ListItem>
                <ListItem label="Completed Tasks" onClick={() => navigate("/todo-finish")}>
                    <PlaylistAddCheckIcon color={"green"}/>
                </ListItem>
                <ListItem label="Group Management" onClick={() => navigate("/group")}>
                    <GroupWorkIcon color={"secondary"}/>
                </ListItem>
            </List>
            <Divider/>
            <List>
                {userInfo && userInfo.admin &&
                <ListItem label="Administration" onClick={() => navigate("/admin")}>
                    <AdminPanelSettingsIcon color={"blue"}/>
                </ListItem>}
                <ListItem label="Logout" onClick={handleLogout}>
                    <LogoutIcon color={"primary"}/>
                </ListItem>
            </List>
        </div>
    );

    const container = window !== undefined ? () => window().document.body : undefined;

    return (
        <Box sx={{display: 'flex'}}>
            <CssBaseline/>
            <AppBar
                position="fixed"
                sx={{
                    width: {sm: `100%`},
                    ml: {sm: `${drawerWidth}px`},
                }}
            >
                <Toolbar>
                    <IconButton
                        color="inherit"
                        aria-label="open drawer"
                        edge="start"
                        onClick={handleDrawerToggle}
                        sx={{mr: 2, display: {sm: 'none'}}}
                    >
                        <MenuIcon/>
                    </IconButton>
                    <Typography variant="h6" noWrap component="div" style={{marginLeft: userInfo && userInfo.fullName ? 300 : 20}}>
                        {userInfo && userInfo.fullName ? `Hi ${userInfo.fullName}, What you want to do today?` : "Please Sign in OR Sign up"}
                    </Typography>
                </Toolbar>
            </AppBar>
            {userInfo && userInfo.fullName &&
            <Box
                component="nav"
                sx={{width: {sm: drawerWidth}, flexShrink: {sm: 0}}}
                aria-label="mailbox folders"
            >
                <Drawer
                    container={container}
                    variant="temporary"
                    open={mobileOpen}
                    onClose={handleDrawerToggle}
                    ModalProps={{
                        keepMounted: true,
                    }}
                    sx={{
                        display: {xs: 'block', sm: 'none'},
                        '& .MuiDrawer-paper': {boxSizing: 'border-box', width: drawerWidth},
                    }}
                >
                    {drawer}
                </Drawer>
                <Drawer
                    variant="permanent"
                    sx={{
                        display: {xs: 'none', sm: 'block'},
                        '& .MuiDrawer-paper': {boxSizing: 'border-box', width: drawerWidth},
                    }}
                    open
                >
                    {drawer}
                </Drawer>
            </Box>
            }
            {props.children}
        </Box>
    );
}

ResponsiveDrawer.propTypes = {
    window: PropTypes.func,
};

export default ResponsiveDrawer;
