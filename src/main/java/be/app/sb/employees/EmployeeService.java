package be.app.sb.employees;

import be.app.sb.db.tables.daos.DepartmentDao;
import be.app.sb.db.tables.daos.EmployeeDao;
import be.app.sb.db.tables.pojos.Department;
import be.app.sb.db.tables.pojos.Employee;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static be.app.sb.db.tables.Employee.EMPLOYEE;

@Service
public class EmployeeService {

    private final EmployeeDao employeeDao;

    private final DepartmentDao departmentDao;

    private final DSLContext dsl;

    //Define a constant for the Department Manager (DPM) Role
    private final String DPM_ROLE = "DPM";

    //Define a constant for the Unset Department No
    private final String DP_UNSET_NO = "dddd";

    public EmployeeService(Configuration jooqConfiguration, DSLContext dsl) {
        this.employeeDao = new EmployeeDao(jooqConfiguration);
        this.departmentDao = new DepartmentDao(jooqConfiguration);
        this.dsl = dsl;
    }

    /**
     * Fetch all employees from employee table
     * @return a list of all employees
     */
    public List<Employee> getAll() {
        return this.employeeDao.findAll();
    }

    /**
     * Count all employees present in employee table
     * @return an integer of the total number of employees
     */
    public int getNumberEmployees() {
        return this.dsl.fetchCount(EMPLOYEE);
    }

    /**
     * Delete an employee by its id
     * @param id integer id of the employee to delete
     */
    public void deleteEmployeeById(int id) {
        this.employeeDao.deleteById(id);
    }

    /**
     * Create a new employee after encoding its non-hashed password
     * @param newEmployee the employee object
     * @return the new Employee created
     */
    public Employee createNewEmployee(Employee newEmployee) {
        encodePassword(newEmployee);
        this.employeeDao.insert(newEmployee);
        return newEmployee;
    }

    /**
     * Update an employee object (Only for ROLE_HR)
     * @param employee the employee object
     * @return the updated employee
     */
    public Employee updateEmployeeDetails(Employee employee) {
        this.employeeDao.update(employee);
        return employee;
    }

    /**
     * Update the employee password firstname and lastname of an employee object
     * @param user User object being built at authentication (See UserDetailsServiceImpl class)
     * @param password String of the non-hashed password to use for future authentication
     * @param firstName String of the firstName after update
     * @param lastName String of the lastName after update
     * @return the employee object updated
     */
    public Employee updateEmpDetailsByEmp(User user, String password, String firstName, String lastName) {
        Employee employee = this.employeeDao.fetchByUserName(user.getUsername()).get(0);
        employee.setPassword(password);
        encodePassword(employee);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        this.employeeDao.update(employee);
        return employee;
    }

    /**
     * Fetch employee id's by firstName or lastName
     * Check if the name is contained in any string of employee.first_name or employee.last_name columns
     * Ignore case
     * @param name String of the name to find
     * @return an array of integers id's
     */
    public Integer[] getEmployeeByFirstOrLastName(String name) {
        return this.dsl
                .select(EMPLOYEE.ID)
                .from(EMPLOYEE)
                .where(EMPLOYEE.FIRST_NAME.containsIgnoreCase(name)
                        .or(EMPLOYEE.LAST_NAME.containsIgnoreCase(name)))
                .fetchArray(EMPLOYEE.ID);
    }

    /**
     * Fetch an employee by its username
     * @param username String of the username
     * @return the fetched employee object
     */
    public Employee findEmployeeByUserName(String username) {
        return this.employeeDao.fetchByUserName(username).get(0);
    }

    /**
     * Set a hashed password of an employee with Pbkdf2PasswordEncoder inside the employee object
     * @param newEmployee the employee object
     */
    public void encodePassword(Employee newEmployee) {
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();
        String password = newEmployee.getPassword();
        newEmployee.setPassword(encoder.encode(password));
    }

    /**
     * Define a new role for the employee depending on the department no specified
     * @param id the employee id
     * @param dptNo the String of the department no
     * @return the employee object with the new department id
     */
    public Employee enrollEmpInDpt(int id, String dptNo) {
        Department department = this.departmentDao.fetchByNo(dptNo).get(0);
        Employee employee = this.employeeDao.fetchById(id).get(0);
        employee.setDepartmentId(department.getId());
        this.employeeDao.update(employee);
        return employee;
    }

    /**
     * Define the new role of an employee depending on the content of DPM_ROLE constant
     * @param id the employee id
     * @return the updated employee object with the new role
     */
    public Employee promoteEmpAsDptManager(int id) {
        Employee employee = this.employeeDao.fetchById(id).get(0);
        employee.setRole(DPM_ROLE);
        this.employeeDao.update(employee);
        return employee;
    }

    /**
     * Remove an employee from its department
     * Define the new DepartmentId based on the id of "dddd" (Unset) department
     * @param id the id of the employee
     * @return the updated employee object with the new department id
     */
    public Employee removeEmpFromDpt(int id) {
        Department department = this.departmentDao.fetchByNo(DP_UNSET_NO).get(0);
        Employee employee = this.employeeDao.fetchById(id).get(0);
        employee.setDepartmentId(department.getId());
        this.employeeDao.update(employee);
        return employee;
    }
}
