package be.app.sb;

import be.app.sb.utils.OpenCsvUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    //Define here the string path to the initialization of employees CSV file
    public final String EMPLOYEE_FILE_PATH =
            "C:/brave-crossbow-master/src/main/resources/employees.csv";

    //Define here the string path to the initialization of employees_department CSV file
    public final String EMPLOYEE_DEPARTMENT_FILE_PATH =
            "C:/brave-crossbow-master/src/main/resources/employees_departments.csv";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    //

    /**
     * Make use of a bean of type CommandLineRunner to load data from CSV files by
     * running methods from OpenCsvUtil class AFTER the start of the Spring Boot app)
     * @param openCsvUtil
     * @return CommandLineRunner
     */
    @Bean
    public CommandLineRunner commandLineRunner(OpenCsvUtil openCsvUtil) {
        return args -> {
            openCsvUtil.loadEmpIntoDb(EMPLOYEE_FILE_PATH);
            openCsvUtil.parseCsvFileEmpDepartId(EMPLOYEE_DEPARTMENT_FILE_PATH);
        };
    }
}