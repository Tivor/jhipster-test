package br.com.antharys.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Panel.
 */
@Entity
@Table(name = "panel")
public class Panel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 5, max = 30)
    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "sql_string")
    private String sqlString;

    @ManyToOne
    @NotNull
    private DataRepository dataRepository;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSqlString() {
        return sqlString;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }

    public DataRepository getDataRepository() {
        return dataRepository;
    }

    public void setDataRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Panel panel = (Panel) o;
        if(panel.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, panel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Panel{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", sqlString='" + sqlString + "'" +
            '}';
    }
}
