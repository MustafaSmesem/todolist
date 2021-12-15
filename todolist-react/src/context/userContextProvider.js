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
        throw new Error('useCount must be used within a CountProvider');
    return context;
}

export {
    UserProvider,
    useUser,
};
