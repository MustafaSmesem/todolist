import React, {createContext, useState, useContext} from "react";

const UserContext = createContext({});
UserContext.displayName = "UserContext";


function UserProvider({children}) {
    const [userInfo, setUserInfo] = useState(null);

    return (
        <UserContext.Provider value={{ userInfo, setUserInfo }}>
            {children}
        </UserContext.Provider>
    );
}

function useUser() {
    const context = useContext(UserContext);
    if (context === undefined)
        throw new Error('useUser must be used within a UserProvider');
    return context;
}

export {
    UserProvider,
    useUser,
};
