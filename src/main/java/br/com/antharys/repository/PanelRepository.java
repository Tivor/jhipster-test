package br.com.antharys.repository;

import br.com.antharys.domain.Panel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Panel entity.
 */
@SuppressWarnings("unused")
public interface PanelRepository extends JpaRepository<Panel,Long> {

}
