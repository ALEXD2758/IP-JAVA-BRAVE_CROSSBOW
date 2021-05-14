package be.app.sb.utils;

import java.io.*;
import java.util.List;

import be.app.sb.db.tables.daos.DepartmentDao;
import be.app.sb.db.tables.daos.EmployeeDao;
import be.app.sb.db.tables.pojos.Department;
import be.app.sb.db.tables.pojos.Employee;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.tools.csv.CSVReader;
import org.springframework.stereotype.Service;

import static be.app.sb.db.tables.Employee.EMPLOYEE;

/**
 * OpenCsvUtil serves to load data from CSV files into PostgreSQL using JOOQ library
 */
@Service
public class OpenCsvUtil {

    private final EmployeeDao employeeDao;

    private final DepartmentDao departmentDao;

    private final DSLContext dsl;

    public OpenCsvUtil(DSLContext dsl, Configuration jooqConfiguration) {
        this.employeeDao = new EmployeeDao(jooqConfiguration);
        this.dsl = dsl;
        this.departmentDao = new DepartmentDao(jooqConfiguration);
    }

    /**
     * Function that serves @parseCsvFileEmpDepartId by updating an employee ID according to the username specified
     * @param strArray a string array corresponding to a line of the CSV file "employees_department"
     * @return the updated employee object
     */
    public Employee updateEmpDepartId(String[] strArray) {
        //Fetch department by its department no
        Department department = departmentDao.fetchByNo(strArray[1]).get(0);
        //Fetch employee by its username
        Employee employee = employeeDao.fetchByUserName(strArray[0]).get(0);

        //Set the new department Id according to the department number present in the CSV file
        employee.setDepartmentId(department.getId());
        employeeDao.update(employee);
        return employee;
    }

    /**
     * Load a CSV file into the employee table according to the specified path
     * @param strEmpDepartFile the string of the path to the CSV file
     * @throws IOException
     */
    public void loadEmpIntoDb(String strEmpDepartFile) throws IOException {
    final File file = new File(strEmpDepartFile);
        this.dsl.loadInto(EMPLOYEE)
            .loadCSV(file)
                .fields(EMPLOYEE.ID, EMPLOYEE.USER_NAME, EMPLOYEE.BIRTH_DATE,
                        EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, EMPLOYEE.GENDER, EMPLOYEE.HIRE_DATE,
                        EMPLOYEE.PASSWORD, EMPLOYEE.ROLE, EMPLOYEE.DEPARTMENT_ID)
                .ignoreRows(0)
                .execute();
    }

    /**
     * Load the CSV file of employees_department and change the department ID of the corresponding username in
     * empployee table
     * @param strEmpFile the string of the path to the CSV file
     * @return a list of String arrays, each containing a line of the CSV file
     * @throws IOException
     */
    public List<String[]> parseCsvFileEmpDepartId(String strEmpFile) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(
                strEmpFile))) {
            List<String[]> r = reader.readAll();
            r.forEach(x -> updateEmpDepartId(x));
            return r;
        }
    }
}