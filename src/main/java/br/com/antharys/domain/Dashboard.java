package br.com.antharys.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Dashboard.
 */
@Entity
@Table(name = "dashboard")
public class Dashboard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 10, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToMany
    @JoinTable(name = "dashboard_panel",
               joinColumns = @JoinColumn(name="dashboards_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="panels_id", referencedColumnName="ID"))
    private Set<Panel> panels = new HashSet<>();

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Panel> getPanels() {
        return panels;
    }

    public void setPanels(Set<Panel> panels) {
        this.panels = panels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dashboard dashboard = (Dashboard) o;
        if(dashboard.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dashboard.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Dashboard{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
