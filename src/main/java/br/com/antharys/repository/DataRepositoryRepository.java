package br.com.antharys.repository;

import br.com.antharys.domain.DataRepository;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DataRepository entity.
 */
@SuppressWarnings("unused")
public interface DataRepositoryRepository extends JpaRepository<DataRepository,Long> {

}
