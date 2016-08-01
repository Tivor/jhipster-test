package br.com.antharys.service.impl;

import br.com.antharys.service.DashboardService;
import br.com.antharys.domain.Dashboard;
import br.com.antharys.repository.DashboardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Dashboard.
 */
@Service
@Transactional
public class DashboardServiceImpl implements DashboardService{

    private final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);
    
    @Inject
    private DashboardRepository dashboardRepository;
    
    /**
     * Save a dashboard.
     * 
     * @param dashboard the entity to save
     * @return the persisted entity
     */
    public Dashboard save(Dashboard dashboard) {
        log.debug("Request to save Dashboard : {}", dashboard);
        Dashboard result = dashboardRepository.save(dashboard);
        return result;
    }

    /**
     *  Get all the dashboards.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Dashboard> findAll() {
        log.debug("Request to get all Dashboards");
        List<Dashboard> result = dashboardRepository.findAllWithEagerRelationships();
        return result;
    }

    /**
     *  Get one dashboard by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Dashboard findOne(Long id) {
        log.debug("Request to get Dashboard : {}", id);
        Dashboard dashboard = dashboardRepository.findOneWithEagerRelationships(id);
        return dashboard;
    }

    /**
     *  Delete the  dashboard by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Dashboard : {}", id);
        dashboardRepository.delete(id);
    }
}
