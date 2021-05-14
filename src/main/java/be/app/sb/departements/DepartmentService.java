package be.app.sb.departements;

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

import static be.app.sb.db.tables.Department.DEPARTMENT;
import static be.app.sb.db.tables.Employee.EMPLOYEE;

@Service
public class DepartmentService {

    private final DepartmentDao departmentDao;

    private final DSLContext dsl;

    public DepartmentService(Configuration jooqConfiguration, DSLContext dsl) {
        this.departmentDao = new DepartmentDao(jooqConfiguration);
        this.dsl = dsl;
    }

    /**
     * Fetch a list of all departments in the department table
     * @return List<Department> containing all departments available
     */
    public List<Department> getAllDepartments() {
        return this.departmentDao.findAll();
    }

    /**
     * Fetch a department object by its department no
     * @param no the department no to find
     * @return the fetched department object
     */
    public Department getDepartmentByNo(String no) {
        return this.dsl
                .selectFrom(DEPARTMENT)
                .where(DEPARTMENT.NO.eq(no))
                .fetchOne()
                .into(Department.class);
    }

    /**
     * Fetch a department object by its department no
     * @param id the department id
     * @return the fetched department object
     */
    public Department getDepartmentById(int id) {
        return this.dsl
                .selectFrom(DEPARTMENT)
                .where(DEPARTMENT.ID.eq(id))
                .fetchOne()
                .into(Department.class);
    }

    /**
     * Delete a department by its id
     * @param id the department id
     */
    public void deleteDepartmentById(int id) {
        this.departmentDao.deleteById(id);
    }

    /**
     * Create a new department into the employee table
     * @param newDepartment the department object to be created
     * @return the new department created
     */
    public Department createNewDepartment(Department newDepartment) {
        this.departmentDao.insert(newDepartment);
        return newDepartment;
    }
}