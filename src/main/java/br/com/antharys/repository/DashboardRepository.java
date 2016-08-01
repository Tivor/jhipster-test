package br.com.antharys.repository;

import br.com.antharys.domain.Dashboard;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Dashboard entity.
 */
@SuppressWarnings("unused")
public interface DashboardRepository extends JpaRepository<Dashboard,Long> {

    @Query("select dashboard from Dashboard dashboard where dashboard.user.login = ?#{principal.username}")
    List<Dashboard> findByUserIsCurrentUser();

    @Query("select distinct dashboard from Dashboard dashboard left join fetch dashboard.panels")
    List<Dashboard> findAllWithEagerRelationships();

    @Query("select dashboard from Dashboard dashboard left join fetch dashboard.panels where dashboard.id =:id")
    Dashboard findOneWithEagerRelationships(@Param("id") Long id);

}
