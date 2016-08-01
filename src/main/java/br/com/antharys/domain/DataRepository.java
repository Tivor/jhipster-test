package br.com.antharys.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DataRepository.
 */
@Entity
@Table(name = "data_repository")
public class DataRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "jdbc_url", nullable = false)
    private String jdbcUrl;

    @NotNull
    @Column(name = "jdbc_driver", nullable = false)
    private String jdbcDriver;

    @Column(name = "db_user")
    private String dbUser;

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

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataRepository dataRepository = (DataRepository) o;
        if(dataRepository.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dataRepository.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DataRepository{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", jdbcUrl='" + jdbcUrl + "'" +
            ", jdbcDriver='" + jdbcDriver + "'" +
            ", dbUser='" + dbUser + "'" +
            '}';
    }
}
