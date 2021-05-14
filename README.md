<img src="https://img.shields.io/badge/java-%23ED8B00.svg?&style=for-the-badge&logo=java&logoColor=white"/> * * *  <img src="https://img.shields.io/badge/spring%20-%236DB33F.svg?&style=for-the-badge&logo=spring&logoColor=white"/>  * * *  <img src="https://img.shields.io/badge/docker%20-%230db7ed.svg?&style=for-the-badge&logo=docker&logoColor=white"/>

# BRAVE CROSSBOW
Project : Brave crossbow

## Purpose
Showing what I can do and being hired!


## Prerequisites to run
- Java JDK11
- Docker
- PostgreSQL

## Run the application

This project uses JOOQ library to generate the domain and dao layers as well.
If by any mean, you need to reload it after a change. Follow these few steps:

-- **A. REMOVE THE FUNCTION/TRIGGER FROM SQL SCRIPT**:

As CREATE FUNCTION and CREATE TRIGGER are not supported yet by JOOQ library, you will need to remove it in order to generate the classes.

Go to : src/main/resources/db/migration/V0004__add_timesheet.sql and remove it temporary (make sure to replace it after classes generation)

-- **B. MAVEN CLEAN INSTALL**:
For generating the JOOQ classes, simply run this command inside the root directory:
```
$ mvn clean install
```
The target directory has been configured to create the classes inside src/main/java/be/app/sb/db
You can configure it, if needed, inside the POM file, plugin jooq-codegen-maven

-- **C. CHANGE UPPERCASE FIELDS INTO LOWERCASE**:
I couldn't figure out how to configure the automatic generation of fields and table to lowercase, instead of Uppercase.
As a temporary workaround, go to "Department", "Employee", "Timereport" classes and change manually the name of each field (TableField ... = createField("XXX"..." and also the name of the class name (see table reference of the class: "unqualified name" in DSL.name("XXX")..."

-- **1. CREATE DATABASE (NOT SCHEMA)**:
Log in as a superuser with PostgreSQL and create database (not schema) the way you prefer with syntax: 
```"CREATE DATABASE name_database;" 
```
It will create a separate DB.

-- **1.1 CONFIGURE SQL SCRIPTS FOLDER AND SCHEMA**:
In src/main/resources/application.properties, you will find a few configuration parameters. Change it if needed.

List:
```
spring.flyway.locations=classpath:db/migration
```
Define the classpath where there are the SQL scripts (by default in this project is db/migration). If you haven't moved it, don't change it.
```
spring.flyway.schemas=sbtest
```
Specify the schema name to use
```
spring.datasource.url=jdbc:postgresql://localhost:5432/dbtest?currentSchema=sbtest
```
Change "dbtest" by the database name (not schema name) previously created in step 1, and "sbtest" with the schema name.

```
spring.datasource.username=postgres
spring.datasource.password=admin
```
Define the username and password there

-- **2.1 WITH IDE/MAVEN**: 
For running the application, either launch it in your IDE or run below command inside the root directory of the project
Change file Paths  in "TimereportService" class and "Application" class (absolute path)

```
mvn spring-boot:run
```
The application will create a sbtest schema using Flyway-core, generating tables from the 3 scripts present in src/main/resources/db/migration

IMPORTANT TO NOTE: Automatic data loading from 2 CSV files (in src/main/resources) happens each time the application runs.
To deactivate it, simply go src/main/java/be/app/sb/Application.java and remove the "commandLineRunner" bean.

-- **2.2 WITH DOCKER**: Use this exact procedure: 

You will also need to change the PostgreSQL URL in application.properties

-- **2.2.1 Build your application with maven**:

Your JAR was previously built but if you made changes, execute this command inside your command prompt:
```
mvn install
```

