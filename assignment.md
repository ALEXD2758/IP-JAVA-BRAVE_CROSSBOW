# todo:

1. As HR Manager
* I want to create or delete departments => DONE
* I want to promote employees as department Manager => DONE
* I want to create new employee => DONE
  * username must be unique and unmodifiable => DONE
  * password must be set => DONE

2. As a department manager
* I want enrol unassigned employee in my department => DONE 
* I want to remove employee from my department => DONE
* I want a daily employees time report => DONE
  * the report must be placed in a configurable directory.
  * One directory per department
  * A report is a file named "report_{yyyy-mm-dd}.csv"
  * for each of my employee a record containing the project name, the reported time and when the time has been reported

3. As an employeeyyy
* I want to update my personal informations (password, firstname, lastname) => DONE
* I want to add my time report in my timesheet => DONE

# considerations
* A initial list of employee and their enrollment must be loaded at startup => DONE
* The application should be able to migrate database at startup => DONE
* This application must be secure => DONE
* This application should be easily monitored => DONE
* provide all information to build the application and package it into a docker image => NOT DONE
* provide all information to run and configure the application => DONE
in command prompt, after logging as a superuser => DONE
* (optional) integration with a SSO provider (eg: keycloak)

1st problem: problem of version not declared in the pom of maven version 3 (necesssary - more restrictive than other versions)
2nd problem: Error running jOOQ code generation tool => The configuration tag has to be directly under the plugin tag, not inside execution:
3rd problem: Flywaydb(flyway-core) not added on the classpath
4rth problem: statement for alter column department_id wrong, new one set to : ALTER TABLE employee ALTER COLUMN department_id SET NOT NULL;
5th problem: position of "serial" and no existent primary key in id fields of employee and department
6th problem: fixed up the employees csv