import {useState} from 'react';
import useStyle from "./style";

export function UseForm(initialValues, validateOnChange= false, validate) {
    const [values, setValues] = useState(initialValues);
    const [errors, setErrors] = useState({});

    const handleInputChange = e => {
        const {name, value} = e.target;
        setValues({
            ...values,
            [name]: value
        });
        if (validateOnChange)
            validate({[name]: value});
    }

    const resetForm = () => {
        setValues(initialValues);
        setErrors({});
    }

    return {
        values,
        setValues,
        errors,
        setErrors,
        handleInputChange,
        resetForm,
    };
}

export function Form(props) {
    const classes = useStyle();
    const {children, ...other} = props;
    return (
        <form className={classes.root} {...other}>
            {children}
        </form>
    );
}

