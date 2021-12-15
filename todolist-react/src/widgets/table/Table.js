import React, {useState} from 'react';
import {
    Table as MuiTable,
    TableCell,
    TableHead as MuiTableHead,
    TablePagination as MuiTablePagination,
    TableRow,
    TableSortLabel
} from '@mui/material';
import useStyle from "./style";

export function UseTable(records, headCells, filterFn, tableName) {

    const classes = useStyle();
    const pages = [10, 20, 50];
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(pages[page]);
    const [orderDirection, setOrderDirection] = useState();
    const [orderBy, setOrderBy] = useState();

    const handlePageChange = (event, newPage) => {
        setPage(newPage);
    }

    const handleChangeRowsPerPage = event => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    }

    const handleSort = sortLabel => {
        const isAsc = orderBy === sortLabel && orderDirection === "asc";
        setOrderDirection(isAsc?'desc':'asc');
        setOrderBy(sortLabel);
        localStorage.setItem(tableName + 'TableSortLabel', sortLabel);
    }

    const Table = props => (
        <MuiTable className={classes.table}>
            {props.children}
        </MuiTable>
    );

    const TableHead = props => {

        return (
            <MuiTableHead>
                <TableRow>
                    {
                        headCells?.map(headCell => (
                            <TableCell key={headCell.id}
                                sortDirection={orderBy === headCell.id ? orderDirection : false}
                            >
                                {headCell.disableSorting ? headCell.label :
                                    <TableSortLabel
                                        active={orderBy === headCell.id}
                                        direction={orderBy === headCell.id ? orderDirection : 'asc'}
                                        onClick={()=> handleSort(headCell.id)}
                                    >
                                        {headCell.label}
                                    </TableSortLabel>
                                }

                            </TableCell>
                        ))
                    }
                </TableRow>
            </MuiTableHead>
        );
    }

    const TablePagination = props => (
        <MuiTablePagination
            rowsPerPageOptions={pages}
            component='div'
            count={records.length}
            page={page}
            rowsPerPage={rowsPerPage}
            onPageChange={handlePageChange}
            onRowsPerPageChange={handleChangeRowsPerPage}
        />
    );

    function stableSort(array, comparator) {
        const stabilizedThis = array.map((el, index) => [el, index]);
        stabilizedThis.sort((a, b) => {
            const order = comparator(a[0], b[0]);
            if (order !== 0) return order;
            return a[1] - b[1];
        });
        return stabilizedThis.map(el => el[0]);
    }

    function getComparator(orderDirection, orderBy) {
        return orderDirection === 'desc'
            ? (a, b) => descendingComparator(a, b, orderBy)
            : (a, b) => -descendingComparator(a, b, orderBy);
    }

    function descendingComparator(a, b, orderBy) {
        if(b[orderBy] < a[orderBy]) return -1;
        if(b[orderBy] > a[orderBy]) return 1;
        return 0;
    }

    const recordAfterPagingAndSorting = () => {
        return stableSort(filterFn.fn(records), getComparator(orderDirection, orderBy)).slice(page * rowsPerPage, (page + 1) * rowsPerPage);
    }

    return {
        Table,
        TableHead,
        TablePagination,
        recordAfterPagingAndSorting,
        handleSort,
    };
}
