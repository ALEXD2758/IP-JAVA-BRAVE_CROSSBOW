package be.app.sb.departements;

import be.app.sb.db.tables.daos.DepartmentDao;
import be.app.sb.db.tables.pojos.Department;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Define this Controller as REST
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    private final DepartmentDao departmentDao;

    private final DSLContext dsl;

    public DepartmentController(DepartmentService departmentService, DSLContext dsl, Configuration jooqConfiguration) {
        this.departmentDao = new DepartmentDao(jooqConfiguration);
        this.departmentService = departmentService;
        this.dsl = dsl;
    }

    /**
     * HTTP GET request for returning a list of all departments in department table
     * Only accessible by ROLE_HR and ROLE_DPM (see @Secured annotation)
     * @return List<Department> of all departments
     */
    @Secured({"ROLE_HR", "ROLE_DPM"})
    @GetMapping("/")
    public List<Department> departments() {
        return this.departmentService.getAllDepartments();
    }

    /**
     * HTTP DELETE request for deleting a department by its id from the department table
     * Only accessible by ROLE_HR (see @Secured annotation)
     * @param id the @PathVariable integer id of the department
     */
    @Secured("ROLE_HR")
    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable int id) {
        this.departmentService.deleteDepartmentById(id);
    }

    /**
     * HTTP POST request for adding a new department to the department table
     * Only accessible by ROLE_HR (see @Secured annotation)
     * @param newDepartment the @RequestBody of the department object
     * @return the department being added
     */
    @Secured("ROLE_HR")
    @PostMapping("/")
    public Department newDepartment(@RequestBody Department newDepartment) {
        return this.departmentService.createNewDepartment(newDepartment);
    }

    /**
     * HTTP GET request for finding the department object by its department no
     * Only accessible by ROLE_HR and ROLE_DPM (see @Secured annotation)
     * @param no the @PathVariable String of the department no
     * @return the department object being found
     */
    @Secured({"ROLE_HR", "ROLE_DPM"})
    @GetMapping("/{no}")
    public Department findDepartment(@PathVariable("no") String no) {
        return this.departmentService.getDepartmentByNo(no);
    }
}