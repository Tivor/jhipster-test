package br.com.antharys.web.rest;

import br.com.antharys.DashboardApp;
import br.com.antharys.domain.DataRepository;
import br.com.antharys.repository.DataRepositoryRepository;

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
 * Test class for the DataRepositoryResource REST controller.
 *
 * @see DataRepositoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DashboardApp.class)
@WebAppConfiguration
@IntegrationTest
public class DataRepositoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_JDBC_URL = "AAAAA";
    private static final String UPDATED_JDBC_URL = "BBBBB";
    private static final String DEFAULT_JDBC_DRIVER = "AAAAA";
    private static final String UPDATED_JDBC_DRIVER = "BBBBB";
    private static final String DEFAULT_DB_USER = "AAAAA";
    private static final String UPDATED_DB_USER = "BBBBB";

    @Inject
    private DataRepositoryRepository dataRepositoryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDataRepositoryMockMvc;

    private DataRepository dataRepository;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DataRepositoryResource dataRepositoryResource = new DataRepositoryResource();
        ReflectionTestUtils.setField(dataRepositoryResource, "dataRepositoryRepository", dataRepositoryRepository);
        this.restDataRepositoryMockMvc = MockMvcBuilders.standaloneSetup(dataRepositoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dataRepository = new DataRepository();
        dataRepository.setName(DEFAULT_NAME);
        dataRepository.setJdbcUrl(DEFAULT_JDBC_URL);
        dataRepository.setJdbcDriver(DEFAULT_JDBC_DRIVER);
        dataRepository.setDbUser(DEFAULT_DB_USER);
    }

    @Test
    @Transactional
    public void createDataRepository() throws Exception {
        int databaseSizeBeforeCreate = dataRepositoryRepository.findAll().size();

        // Create the DataRepository

        restDataRepositoryMockMvc.perform(post("/api/data-repositories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dataRepository)))
                .andExpect(status().isCreated());

        // Validate the DataRepository in the database
        List<DataRepository> dataRepositories = dataRepositoryRepository.findAll();
        assertThat(dataRepositories).hasSize(databaseSizeBeforeCreate + 1);
        DataRepository testDataRepository = dataRepositories.get(dataRepositories.size() - 1);
        assertThat(testDataRepository.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDataRepository.getJdbcUrl()).isEqualTo(DEFAULT_JDBC_URL);
        assertThat(testDataRepository.getJdbcDriver()).isEqualTo(DEFAULT_JDBC_DRIVER);
        assertThat(testDataRepository.getDbUser()).isEqualTo(DEFAULT_DB_USER);
    }

    @Test
    @Transactional
    public void checkJdbcUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataRepositoryRepository.findAll().size();
        // set the field null
        dataRepository.setJdbcUrl(null);

        // Create the DataRepository, which fails.

        restDataRepositoryMockMvc.perform(post("/api/data-repositories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dataRepository)))
                .andExpect(status().isBadRequest());

        List<DataRepository> dataRepositories = dataRepositoryRepository.findAll();
        assertThat(dataRepositories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJdbcDriverIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataRepositoryRepository.findAll().size();
        // set the field null
        dataRepository.setJdbcDriver(null);

        // Create the DataRepository, which fails.

        restDataRepositoryMockMvc.perform(post("/api/data-repositories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dataRepository)))
                .andExpect(status().isBadRequest());

        List<DataRepository> dataRepositories = dataRepositoryRepository.findAll();
        assertThat(dataRepositories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDataRepositories() throws Exception {
        // Initialize the database
        dataRepositoryRepository.saveAndFlush(dataRepository);

        // Get all the dataRepositories
        restDataRepositoryMockMvc.perform(get("/api/data-repositories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dataRepository.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].jdbcUrl").value(hasItem(DEFAULT_JDBC_URL.toString())))
                .andExpect(jsonPath("$.[*].jdbcDriver").value(hasItem(DEFAULT_JDBC_DRIVER.toString())))
                .andExpect(jsonPath("$.[*].dbUser").value(hasItem(DEFAULT_DB_USER.toString())));
    }

    @Test
    @Transactional
    public void getDataRepository() throws Exception {
        // Initialize the database
        dataRepositoryRepository.saveAndFlush(dataRepository);

        // Get the dataRepository
        restDataRepositoryMockMvc.perform(get("/api/data-repositories/{id}", dataRepository.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dataRepository.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.jdbcUrl").value(DEFAULT_JDBC_URL.toString()))
            .andExpect(jsonPath("$.jdbcDriver").value(DEFAULT_JDBC_DRIVER.toString()))
            .andExpect(jsonPath("$.dbUser").value(DEFAULT_DB_USER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDataRepository() throws Exception {
        // Get the dataRepository
        restDataRepositoryMockMvc.perform(get("/api/data-repositories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataRepository() throws Exception {
        // Initialize the database
        dataRepositoryRepository.saveAndFlush(dataRepository);
        int databaseSizeBeforeUpdate = dataRepositoryRepository.findAll().size();

        // Update the dataRepository
        DataRepository updatedDataRepository = new DataRepository();
        updatedDataRepository.setId(dataRepository.getId());
        updatedDataRepository.setName(UPDATED_NAME);
        updatedDataRepository.setJdbcUrl(UPDATED_JDBC_URL);
        updatedDataRepository.setJdbcDriver(UPDATED_JDBC_DRIVER);
        updatedDataRepository.setDbUser(UPDATED_DB_USER);

        restDataRepositoryMockMvc.perform(put("/api/data-repositories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDataRepository)))
                .andExpect(status().isOk());

        // Validate the DataRepository in the database
        List<DataRepository> dataRepositories = dataRepositoryRepository.findAll();
        assertThat(dataRepositories).hasSize(databaseSizeBeforeUpdate);
        DataRepository testDataRepository = dataRepositories.get(dataRepositories.size() - 1);
        assertThat(testDataRepository.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDataRepository.getJdbcUrl()).isEqualTo(UPDATED_JDBC_URL);
        assertThat(testDataRepository.getJdbcDriver()).isEqualTo(UPDATED_JDBC_DRIVER);
        assertThat(testDataRepository.getDbUser()).isEqualTo(UPDATED_DB_USER);
    }

    @Test
    @Transactional
    public void deleteDataRepository() throws Exception {
        // Initialize the database
        dataRepositoryRepository.saveAndFlush(dataRepository);
        int databaseSizeBeforeDelete = dataRepositoryRepository.findAll().size();

        // Get the dataRepository
        restDataRepositoryMockMvc.perform(delete("/api/data-repositories/{id}", dataRepository.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DataRepository> dataRepositories = dataRepositoryRepository.findAll();
        assertThat(dataRepositories).hasSize(databaseSizeBeforeDelete - 1);
    }
}
