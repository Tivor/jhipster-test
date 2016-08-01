package br.com.antharys.web.rest;

import br.com.antharys.DashboardApp;
import br.com.antharys.domain.Panel;
import br.com.antharys.repository.PanelRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PanelResource REST controller.
 *
 * @see PanelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DashboardApp.class)
@WebAppConfiguration
@IntegrationTest
public class PanelResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SQL_STRING = "AAAAA";
    private static final String UPDATED_SQL_STRING = "BBBBB";

    @Inject
    private PanelRepository panelRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPanelMockMvc;

    private Panel panel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PanelResource panelResource = new PanelResource();
        ReflectionTestUtils.setField(panelResource, "panelRepository", panelRepository);
        this.restPanelMockMvc = MockMvcBuilders.standaloneSetup(panelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        panel = new Panel();
        panel.setName(DEFAULT_NAME);
        panel.setSqlString(DEFAULT_SQL_STRING);
    }

    @Test
    @Transactional
    public void createPanel() throws Exception {
        int databaseSizeBeforeCreate = panelRepository.findAll().size();

        // Create the Panel

        restPanelMockMvc.perform(post("/api/panels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(panel)))
                .andExpect(status().isCreated());

        // Validate the Panel in the database
        List<Panel> panels = panelRepository.findAll();
        assertThat(panels).hasSize(databaseSizeBeforeCreate + 1);
        Panel testPanel = panels.get(panels.size() - 1);
        assertThat(testPanel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPanel.getSqlString()).isEqualTo(DEFAULT_SQL_STRING);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = panelRepository.findAll().size();
        // set the field null
        panel.setName(null);

        // Create the Panel, which fails.

        restPanelMockMvc.perform(post("/api/panels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(panel)))
                .andExpect(status().isBadRequest());

        List<Panel> panels = panelRepository.findAll();
        assertThat(panels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPanels() throws Exception {
        // Initialize the database
        panelRepository.saveAndFlush(panel);

        // Get all the panels
        restPanelMockMvc.perform(get("/api/panels?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(panel.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].sqlString").value(hasItem(DEFAULT_SQL_STRING.toString())));
    }

    @Test
    @Transactional
    public void getPanel() throws Exception {
        // Initialize the database
        panelRepository.saveAndFlush(panel);

        // Get the panel
        restPanelMockMvc.perform(get("/api/panels/{id}", panel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(panel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sqlString").value(DEFAULT_SQL_STRING.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPanel() throws Exception {
        // Get the panel
        restPanelMockMvc.perform(get("/api/panels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePanel() throws Exception {
        // Initialize the database
        panelRepository.saveAndFlush(panel);
        int databaseSizeBeforeUpdate = panelRepository.findAll().size();

        // Update the panel
        Panel updatedPanel = new Panel();
        updatedPanel.setId(panel.getId());
        updatedPanel.setName(UPDATED_NAME);
        updatedPanel.setSqlString(UPDATED_SQL_STRING);

        restPanelMockMvc.perform(put("/api/panels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPanel)))
                .andExpect(status().isOk());

        // Validate the Panel in the database
        List<Panel> panels = panelRepository.findAll();
        assertThat(panels).hasSize(databaseSizeBeforeUpdate);
        Panel testPanel = panels.get(panels.size() - 1);
        assertThat(testPanel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPanel.getSqlString()).isEqualTo(UPDATED_SQL_STRING);
    }

    @Test
    @Transactional
    public void deletePanel() throws Exception {
        // Initialize the database
        panelRepository.saveAndFlush(panel);
        int databaseSizeBeforeDelete = panelRepository.findAll().size();

        // Get the panel
        restPanelMockMvc.perform(delete("/api/panels/{id}", panel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Panel> panels = panelRepository.findAll();
        assertThat(panels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
