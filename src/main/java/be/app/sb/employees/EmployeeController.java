package be.app.sb.employees;

import be.app.sb.db.tables.daos.EmployeeDao;
import be.app.sb.db.tables.pojos.Employee;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Define this Controller as REST
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeDao employeeDao;

    private final EmployeeService employeeService;

    private final DSLContext dsl;

    public EmployeeController(EmployeeService employeeService, DSLContext dsl, Configuration jooqConfiguration) {
        this.employeeDao = new EmployeeDao(jooqConfiguration);
        this.employeeService = employeeService;
        this.dsl = dsl;
    }

    /**
     * HTTP GET request for returning a list of all employees in employee table
     * Only accessible by ROLE_HR and ROLE_DPM (see @Secured annotation)
     * @return List<Employee> of all employees
     */
    @Secured({"ROLE_HR", "ROLE_DPM"})
    @GetMapping("/")
    public List<Employee> employees() {
        //TODO: hide hashed password
        return this.employeeService.getAll();
    }

    /**
     * HTTP PUT request for enrolling an employee into a department
     * Only accessible by ROLE_HR and ROLE_DPM (see @Secured annotation)
     * @param id the @PathVariable int id of the employee
     * @param dptNo the @RequestParam to define in which department no to place the employee
     * @return the Employee being enrolled into his new department
     */
    @Secured({"ROLE_HR", "ROLE_DPM"})
    @PutMapping("/enrollDpt/{id}")
    public Employee enrollEmployeeDpt(@PathVariable("id") int id, @RequestParam String dptNo) {
        //TODO: hide hashed password
        return this.employeeService.enrollEmpInDpt(id, dptNo);
    }

    /**
     * HTTP PUT request for removing an employee from its department
     * Set the employee in "Unset" department
     * Only accessible by ROLE_HR and ROLE_DPM (see @Secured annotation)
     * @param id the @PathVariable integer id of the employee
     * @return the Employee being removed from his department
     */
    @Secured({"ROLE_HR", "ROLE_DPM"})
    @PutMapping("/removeDpt/{id}")
    public Employee removeEmployeeDpt(@PathVariable("id") int id) {
        //TODO: hide hashed password
        return this.employeeService.removeEmpFromDpt(id);
    }

    /**
     * HTTP GET request for finding all id integers of employees by first or last name
     * Only accessible by ROLE_HR and ROLE_DPM (see @Secured annotation)
     * @param name the @PathVariable String of the employee name
     * @return an Integer array containing all the id's
     */
    @Secured({"ROLE_HR", "ROLE_DPM"})
    @GetMapping("/find/{name}")
    public Integer[] findEmployees(@PathVariable("name") String name) {
        return this.employeeService.getEmployeeByFirstOrLastName(name);
    }

    /**
     * HTTP GET request for counting all the employees present in employee table
     * Only accessible by ROLE_HR (see @Secured annotation)
     * @return an integer of the number of employees counted
     */
    @Secured("ROLE_HR")
    @GetMapping("/countEmployees")
    public int count() {
        return this.employeeService.getNumberEmployees();
    }

    /**
     * HTTP DELETE request for deleting an employee by its id from the employee table
     * Only accessible by ROLE_HR (see @Secured annotation)
     * @param id the @PathVariable integer id of the employee
     */
    @Secured("ROLE_HR")
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable int id) {
        this.employeeService.deleteEmployeeById(id);
    }

    /**
     * HTTP POST request for adding a new employee to the employee table
     * Only accessible by ROLE_HR (see @Secured annotation)
     * @param newEmployee the @RequestBody of the employee object
     * @return the employee being added
     */
    @Secured("ROLE_HR")
    @PostMapping("/")
    public Employee newEmployee(@RequestBody Employee newEmployee) {
        //TODO: hide hashed password
        return this.employeeService.createNewEmployee(newEmployee);
    }

    /**
     * HTTP PUT request for updating the details of an employee
     * Constraint added in PostgreSQL for adding a trigger function to the column "username",
     * to make it immutable on UPDATE
     * Only accessible by ROLE_HR (see @Secured annotation)
     * @param employee the @RequestBody of the employee object
     * @return the updated object of the employee
     */
    @Secured("ROLE_HR")
    @PutMapping("/{id}")
    public Employee updateEmployee(@RequestBody Employee employee) {
        //TODO: hide hashed password
        this.employeeService.updateEmployeeDetails(employee);
        return employee;
    }

    /**
     * HTTP PUT request for promoting an employee into a Department Manager, by its id
     * Only accessible by ROLE_HR (see @Secured annotation)
     * @param id the @PathVariable integer id of the employee
     * @return the employee object being promoted
     */
    @Secured("ROLE_HR")
    @PutMapping("/promote/{id}")
    public Employee promoteEmpDptManager(@PathVariable("id") int id) {
        //TODO: hide hashed password
        return this.employeeService.promoteEmpAsDptManager(id);
    }

    /**
     * HTTP PUT request for an employee to change his password, firstName and lastName
     * Does not change the username, as per stated in requirements (IMMUTABLE)
     * Only accessible by ROLE_EM (see @Secured annotation)
     * @param user User object being built at authentication (See UserDetailsServiceImpl class)
     * @param password String of the non-hashed password to use for future authentication
     * @param firstName String of the firstName after update
     * @param lastName String of the lastName after update
     * @return the updated employee object
     */
    @Secured("ROLE_EM")
    @PutMapping("/updateInfo/")
    public Employee updatePersonalInfo(@AuthenticationPrincipal User user, @RequestParam String password,
                                       @RequestParam String firstName, @RequestParam String lastName) {
        //TODO: hide hashed password
        return this.employeeService.updateEmpDetailsByEmp(user, password, firstName, lastName);
    }
}