import React, {useState} from 'react';
import {Box} from "@mui/material";
import Controls from "../../widgets/form/controls/controls";
import {Form, UseForm} from "../../widgets/form/Form";
import {DropDownGroup} from "../../widgets/dropdown/DropDownGroup";
import {DropDownPriority} from "../../widgets/dropdown/DropDownPriority";

function TodoForm(props) {
    const {todo, groups, priorities, handleSubmit} = props;

    const {values, handleInputChange} = UseForm({...todo}, true, ()=>true);

    const [disableSubmit, setDisableSubmit] = useState(false);

    const submit = async (e) => {
        e.preventDefault();
        setDisableSubmit(true);
        await handleSubmit(values, todo.group);
    }

    return (
        <Form onSubmit={submit}>
            <Box display={"flex"} flexDirection="column" alignItems={"center"}>
                <Controls.Input
                    label='Description'
                    name='description'
                    value={values.description}
                    onChange={handleInputChange}
                    required={true}
                    variant="standard"
                    style={{width: '100%'}}
                />
                <Box display={"flex"} alignItems={"center"} style={{width: '100%', marginTop: 20}}>
                    <span style={{width: 20}}/>
                    <Controls.DatePicker
                        label='Due Date'
                        name='dueDate'
                        value={values.dueDate}
                        onChange={handleInputChange}
                    />
                    <span style={{width: 20}}/>

                    {groups.length > 0 &&
                    <DropDownGroup selectedGroup={todo ? values.group : '-1'} groups={groups}
                                   handleChange={(groupId) => {
                                       values.group = groupId;
                                   }}
                    />}
                    <span style={{width: 20}}/>

                    <DropDownPriority selectedPriority={todo ? values.priority : 4} priorities={priorities}
                                      handleChange={(priorityId) => {
                                          values.priority = priorityId;
                                      }}
                    />
                    <span style={{width: 20}}/>

                </Box>
                <Controls.Button
                    label="Save"
                    type="submit"
                    style={{width: '80%', marginTop: 20}}
                    disabled={disableSubmit}
                />
            </Box>
        </Form>
    );
}

export default TodoForm;
