package be.app.sb.timereports;

import be.app.sb.db.tables.daos.TimereportDao;
import be.app.sb.db.tables.pojos.Department;
import be.app.sb.db.tables.pojos.Employee;
import be.app.sb.db.tables.pojos.Timereport;
import be.app.sb.departements.DepartmentService;
import be.app.sb.employees.EmployeeService;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static be.app.sb.db.tables.Employee.EMPLOYEE;
import static be.app.sb.db.tables.Timereport.TIMEREPORT;
import static org.jooq.impl.DSL.*;

@Service
public class TimereportService {

    private final TimereportDao timereportDao;

    private final DepartmentService departmentService;

    private final EmployeeService employeeService;

    private final DSLContext dsl;

    private boolean checkCreateDir = true;

    //Specify here where you want to create the directories for the departments (The base directory)
    private final String BASE_DIRECTORY = "C:/timesheets/";

    public TimereportService(DepartmentService departmentService, EmployeeService employeeService,
                             Configuration jooqConfiguration, DSLContext dsl) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        this.timereportDao = new TimereportDao(jooqConfiguration);
        this.dsl = dsl;
        createDirectories();
    }

    /**
     * Create and initialize all directories for all departments present in department table
     * The checkCreateDir is by default set to true ; if first instanciation: set to false, otherwise, keep true
     * @return boolean false if
     */
    public void createDirectories() {
        if(checkCreateDir) {
            List<Department> listDepartments = this.departmentService.getAllDepartments();
            listDepartments.forEach(d -> {
                File file = new File(BASE_DIRECTORY + d.getName().toUpperCase());
                file.mkdirs();
            });
            checkCreateDir = false;
        }
    }

    /**
     * Insert a new time report for the authenticated employee with the LocalDateTime of now, the authenticated username
     * the number of hours, the project name and department name specified
     * @param user the authenticated user (referring to employee)
     * @param newTimereport the new Timereport object to create
     * @return
     */
    public Timereport insertNewTimereport(User user, Timereport newTimereport) {
        LocalDateTime time = LocalDateTime.now();
        newTimereport.setUserName(user.getUsername());
        newTimereport.setReportedTime(time);
        this.timereportDao.insert(newTimereport);
        return newTimereport;
    }

    /**
     * Export time report as a CSV file based on the department's name of the authenticated user and "today"'s date
     * @param user the authenticated user
     * @return a CSV formatted string
     */
    public String exportTimereportAsCsv(User user) {
        //Definition pattern for the date
        //TODO: export date pattern as a configurable
        String datePattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

        //Define the LocalDateTime of now for generating the report of TODAY
        //TODO: change this variable into a parameter, otherwise if the DPM is not doing it day by day,
        // impossible to generate report
        LocalDateTime dateTime = LocalDateTime.now();

        //Format the LocalDate of today for generating the report file name
        String dateFormatted = LocalDate.now().format(dateFormatter);

        //Fetching the employee record according to the authenticated user
        Employee authenticatedEmp = this.employeeService.findEmployeeByUserName(user.getUsername());
        //Fetching the department of that user
        Department departmentAuthEmp = this.departmentService.getDepartmentById(authenticatedEmp.getDepartmentId());
        //Get the name of the department of the authenticated user
        String departmentName = departmentAuthEmp.getName();

        //TODO: extraction to make, to return string "csv"
        //Fetch data into CSV format, from Timereport table according to the department name, and date of today
        String csv = this.dsl.selectFrom(TIMEREPORT)
                .where(TIMEREPORT.DEPARTMENT_NAME
                .equalIgnoreCase(departmentName)
                        .and(day(TIMEREPORT.REPORTED_TIME).eq(day(dateTime)))
                        .and(month(TIMEREPORT.REPORTED_TIME).eq(month(dateTime)))
                        .and(year(TIMEREPORT.REPORTED_TIME).eq(year(dateTime)))).fetch().formatCSV();
        //TODO: extract report name and file format as a configurable
        //Generate the file into the authenticated user's department's name and write the csv string inside
        try (PrintWriter writer = new PrintWriter(BASE_DIRECTORY +
                departmentName.toUpperCase() + "/report_" + dateFormatted + ".csv")) {
            writer.write(csv);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return csv;
        }
    }