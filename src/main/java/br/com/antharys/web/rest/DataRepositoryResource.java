package br.com.antharys.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.antharys.domain.DataRepository;
import br.com.antharys.repository.DataRepositoryRepository;
import br.com.antharys.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DataRepository.
 */
@RestController
@RequestMapping("/api")
public class DataRepositoryResource {

    private final Logger log = LoggerFactory.getLogger(DataRepositoryResource.class);
        
    @Inject
    private DataRepositoryRepository dataRepositoryRepository;
    
    /**
     * POST  /data-repositories : Create a new dataRepository.
     *
     * @param dataRepository the dataRepository to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataRepository, or with status 400 (Bad Request) if the dataRepository has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/data-repositories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DataRepository> createDataRepository(@Valid @RequestBody DataRepository dataRepository) throws URISyntaxException {
        log.debug("REST request to save DataRepository : {}", dataRepository);
        if (dataRepository.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dataRepository", "idexists", "A new dataRepository cannot already have an ID")).body(null);
        }
        DataRepository result = dataRepositoryRepository.save(dataRepository);
        return ResponseEntity.created(new URI("/api/data-repositories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dataRepository", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-repositories : Updates an existing dataRepository.
     *
     * @param dataRepository the dataRepository to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataRepository,
     * or with status 400 (Bad Request) if the dataRepository is not valid,
     * or with status 500 (Internal Server Error) if the dataRepository couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/data-repositories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DataRepository> updateDataRepository(@Valid @RequestBody DataRepository dataRepository) throws URISyntaxException {
        log.debug("REST request to update DataRepository : {}", dataRepository);
        if (dataRepository.getId() == null) {
            return createDataRepository(dataRepository);
        }
        DataRepository result = dataRepositoryRepository.save(dataRepository);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dataRepository", dataRepository.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-repositories : get all the dataRepositories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dataRepositories in body
     */
    @RequestMapping(value = "/data-repositories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DataRepository> getAllDataRepositories() {
        log.debug("REST request to get all DataRepositories");
        List<DataRepository> dataRepositories = dataRepositoryRepository.findAll();
        return dataRepositories;
    }

    /**
     * GET  /data-repositories/:id : get the "id" dataRepository.
     *
     * @param id the id of the dataRepository to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataRepository, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/data-repositories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DataRepository> getDataRepository(@PathVariable Long id) {
        log.debug("REST request to get DataRepository : {}", id);
        DataRepository dataRepository = dataRepositoryRepository.findOne(id);
        return Optional.ofNullable(dataRepository)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /data-repositories/:id : delete the "id" dataRepository.
     *
     * @param id the id of the dataRepository to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/data-repositories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDataRepository(@PathVariable Long id) {
        log.debug("REST request to delete DataRepository : {}", id);
        dataRepositoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dataRepository", id.toString())).build();
    }

}
