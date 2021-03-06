/*
 * This file is generated by jOOQ.
 */
package be.app.sb.db.tables.daos;


import be.app.sb.db.tables.Department;
import be.app.sb.db.tables.records.DepartmentRecord;

import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DepartmentDao extends DAOImpl<DepartmentRecord, be.app.sb.db.tables.pojos.Department, Integer> {

    /**
     * Create a new DepartmentDao without any configuration
     */
    public DepartmentDao() {
        super(Department.DEPARTMENT, be.app.sb.db.tables.pojos.Department.class);
    }

    /**
     * Create a new DepartmentDao with an attached configuration
     */
    public DepartmentDao(Configuration configuration) {
        super(Department.DEPARTMENT, be.app.sb.db.tables.pojos.Department.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Integer getId(be.app.sb.db.tables.pojos.Department object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>ID IN (values)</code>
     */
    public List<be.app.sb.db.tables.pojos.Department> fetchById(Integer... values) {
        return fetch(Department.DEPARTMENT.ID, values);
    }

    /**
     * Fetch a unique record that has <code>ID = value</code>
     */
    public be.app.sb.db.tables.pojos.Department fetchOneById(Integer value) {
        return fetchOne(Department.DEPARTMENT.ID, value);
    }

    /**
     * Fetch records that have <code>NO IN (values)</code>
     */
    public List<be.app.sb.db.tables.pojos.Department> fetchByNo(String... values) {
        return fetch(Department.DEPARTMENT.NO, values);
    }

    /**
     * Fetch records that have <code>NAME IN (values)</code>
     */
    public List<be.app.sb.db.tables.pojos.Department> fetchByName(String... values) {
        return fetch(Department.DEPARTMENT.NAME, values);
    }
}
