package br.com.antharys.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.antharys.domain.Panel;
import br.com.antharys.repository.PanelRepository;
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
 * REST controller for managing Panel.
 */
@RestController
@RequestMapping("/api")
public class PanelResource {

    private final Logger log = LoggerFactory.getLogger(PanelResource.class);
        
    @Inject
    private PanelRepository panelRepository;
    
    /**
     * POST  /panels : Create a new panel.
     *
     * @param panel the panel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new panel, or with status 400 (Bad Request) if the panel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/panels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Panel> createPanel(@Valid @RequestBody Panel panel) throws URISyntaxException {
        log.debug("REST request to save Panel : {}", panel);
        if (panel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("panel", "idexists", "A new panel cannot already have an ID")).body(null);
        }
        Panel result = panelRepository.save(panel);
        return ResponseEntity.created(new URI("/api/panels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("panel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /panels : Updates an existing panel.
     *
     * @param panel the panel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated panel,
     * or with status 400 (Bad Request) if the panel is not valid,
     * or with status 500 (Internal Server Error) if the panel couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/panels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Panel> updatePanel(@Valid @RequestBody Panel panel) throws URISyntaxException {
        log.debug("REST request to update Panel : {}", panel);
        if (panel.getId() == null) {
            return createPanel(panel);
        }
        Panel result = panelRepository.save(panel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("panel", panel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /panels : get all the panels.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of panels in body
     */
    @RequestMapping(value = "/panels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Panel> getAllPanels() {
        log.debug("REST request to get all Panels");
        List<Panel> panels = panelRepository.findAll();
        return panels;
    }

    /**
     * GET  /panels/:id : get the "id" panel.
     *
     * @param id the id of the panel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the panel, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/panels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Panel> getPanel(@PathVariable Long id) {
        log.debug("REST request to get Panel : {}", id);
        Panel panel = panelRepository.findOne(id);
        return Optional.ofNullable(panel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /panels/:id : delete the "id" panel.
     *
     * @param id the id of the panel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/panels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePanel(@PathVariable Long id) {
        log.debug("REST request to delete Panel : {}", id);
        panelRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("panel", id.toString())).build();
    }

}
