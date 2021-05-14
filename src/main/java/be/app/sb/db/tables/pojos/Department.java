/*
 * This file is generated by jOOQ.
 */
package be.app.sb.db.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;


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
public class Department implements Serializable {

    private static final long serialVersionUID = -887329814;

    private Integer id;
    private String  no;
    private String  name;

    public Department() {}

    public Department(Department value) {
        this.id = value.id;
        this.no = value.no;
        this.name = value.name;
    }

    public Department(
        Integer id,
        String  no,
        String  name
    ) {
        this.id = id;
        this.no = no;
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNo() {
        return this.no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Department other = (Department) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (no == null) {
            if (other.no != null)
                return false;
        }
        else if (!no.equals(other.no))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.no == null) ? 0 : this.no.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Department (");

        sb.append(id);
        sb.append(", ").append(no);
        sb.append(", ").append(name);

        sb.append(")");
        return sb.toString();
    }
}
