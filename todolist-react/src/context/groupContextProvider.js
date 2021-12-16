import React, {useContext, useState} from "react";

const GroupContext = React.createContext([]);
GroupContext.displayName = "GroupContext";


function GroupProvider({children}) {
    const [groups, setGroups] = useState([]);

    return (
        <GroupContext.Provider value={{groups, setGroups}}>
            {children}
        </GroupContext.Provider>
    );
}

function useGroup() {
    const context = useContext(GroupContext);
    if (context === undefined)
        throw new Error('useGroup must be used within a GroupProvider');
    return context;
}

export {
    GroupProvider,
    useGroup,
};
