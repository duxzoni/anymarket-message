package br.com.db1.anymarket.message.web.rest;

import br.com.db1.anymarket.message.AnymarketMessageApp;

import br.com.db1.anymarket.message.domain.Unhandled;
import br.com.db1.anymarket.message.domain.Origin;
import br.com.db1.anymarket.message.repository.UnhandledRepository;
import br.com.db1.anymarket.message.repository.search.UnhandledSearchRepository;
import br.com.db1.anymarket.message.service.UnhandledService;
import br.com.db1.anymarket.message.service.dto.UnhandledDTO;
import br.com.db1.anymarket.message.service.mapper.UnhandledMapper;
import br.com.db1.anymarket.message.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static br.com.db1.anymarket.message.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UnhandledResource REST controller.
 *
 * @see UnhandledResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnymarketMessageApp.class)
public class UnhandledResourceIntTest {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_JSON = "AAAAAAAAAA";
    private static final String UPDATED_JSON = "BBBBBBBBBB";

    @Autowired
    private UnhandledRepository unhandledRepository;


    @Autowired
    private UnhandledMapper unhandledMapper;
    

    @Autowired
    private UnhandledService unhandledService;

    /**
     * This repository is mocked in the br.com.db1.anymarket.message.repository.search test package.
     *
     * @see br.com.db1.anymarket.message.repository.search.UnhandledSearchRepositoryMockConfiguration
     */
    @Autowired
    private UnhandledSearchRepository mockUnhandledSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUnhandledMockMvc;

    private Unhandled unhandled;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UnhandledResource unhandledResource = new UnhandledResource(unhandledService);
        this.restUnhandledMockMvc = MockMvcBuilders.standaloneSetup(unhandledResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unhandled createEntity(EntityManager em) {
        Unhandled unhandled = new Unhandled()
            .message(DEFAULT_MESSAGE)
            .url(DEFAULT_URL)
            .json(DEFAULT_JSON);
        // Add required entity
        Origin origin = OriginResourceIntTest.createEntity(em);
        em.persist(origin);
        em.flush();
        unhandled.setOrigin(origin);
        return unhandled;
    }

    @Before
    public void initTest() {
        unhandled = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnhandled() throws Exception {
        int databaseSizeBeforeCreate = unhandledRepository.findAll().size();

        // Create the Unhandled
        UnhandledDTO unhandledDTO = unhandledMapper.toDto(unhandled);
        restUnhandledMockMvc.perform(post("/api/unhandleds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unhandledDTO)))
            .andExpect(status().isCreated());

        // Validate the Unhandled in the database
        List<Unhandled> unhandledList = unhandledRepository.findAll();
        assertThat(unhandledList).hasSize(databaseSizeBeforeCreate + 1);
        Unhandled testUnhandled = unhandledList.get(unhandledList.size() - 1);
        assertThat(testUnhandled.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testUnhandled.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testUnhandled.getJson()).isEqualTo(DEFAULT_JSON);

        // Validate the Unhandled in Elasticsearch
        verify(mockUnhandledSearchRepository, times(1)).save(testUnhandled);
    }

    @Test
    @Transactional
    public void createUnhandledWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = unhandledRepository.findAll().size();

        // Create the Unhandled with an existing ID
        unhandled.setId(1L);
        UnhandledDTO unhandledDTO = unhandledMapper.toDto(unhandled);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnhandledMockMvc.perform(post("/api/unhandleds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unhandledDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Unhandled in the database
        List<Unhandled> unhandledList = unhandledRepository.findAll();
        assertThat(unhandledList).hasSize(databaseSizeBeforeCreate);

        // Validate the Unhandled in Elasticsearch
        verify(mockUnhandledSearchRepository, times(0)).save(unhandled);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = unhandledRepository.findAll().size();
        // set the field null
        unhandled.setMessage(null);

        // Create the Unhandled, which fails.
        UnhandledDTO unhandledDTO = unhandledMapper.toDto(unhandled);

        restUnhandledMockMvc.perform(post("/api/unhandleds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unhandledDTO)))
            .andExpect(status().isBadRequest());

        List<Unhandled> unhandledList = unhandledRepository.findAll();
        assertThat(unhandledList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = unhandledRepository.findAll().size();
        // set the field null
        unhandled.setUrl(null);

        // Create the Unhandled, which fails.
        UnhandledDTO unhandledDTO = unhandledMapper.toDto(unhandled);

        restUnhandledMockMvc.perform(post("/api/unhandleds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unhandledDTO)))
            .andExpect(status().isBadRequest());

        List<Unhandled> unhandledList = unhandledRepository.findAll();
        assertThat(unhandledList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJsonIsRequired() throws Exception {
        int databaseSizeBeforeTest = unhandledRepository.findAll().size();
        // set the field null
        unhandled.setJson(null);

        // Create the Unhandled, which fails.
        UnhandledDTO unhandledDTO = unhandledMapper.toDto(unhandled);

        restUnhandledMockMvc.perform(post("/api/unhandleds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unhandledDTO)))
            .andExpect(status().isBadRequest());

        List<Unhandled> unhandledList = unhandledRepository.findAll();
        assertThat(unhandledList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUnhandleds() throws Exception {
        // Initialize the database
        unhandledRepository.saveAndFlush(unhandled);

        // Get all the unhandledList
        restUnhandledMockMvc.perform(get("/api/unhandleds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unhandled.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].json").value(hasItem(DEFAULT_JSON.toString())));
    }
    

    @Test
    @Transactional
    public void getUnhandled() throws Exception {
        // Initialize the database
        unhandledRepository.saveAndFlush(unhandled);

        // Get the unhandled
        restUnhandledMockMvc.perform(get("/api/unhandleds/{id}", unhandled.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unhandled.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.json").value(DEFAULT_JSON.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingUnhandled() throws Exception {
        // Get the unhandled
        restUnhandledMockMvc.perform(get("/api/unhandleds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnhandled() throws Exception {
        // Initialize the database
        unhandledRepository.saveAndFlush(unhandled);

        int databaseSizeBeforeUpdate = unhandledRepository.findAll().size();

        // Update the unhandled
        Unhandled updatedUnhandled = unhandledRepository.findById(unhandled.getId()).get();
        // Disconnect from session so that the updates on updatedUnhandled are not directly saved in db
        em.detach(updatedUnhandled);
        updatedUnhandled
            .message(UPDATED_MESSAGE)
            .url(UPDATED_URL)
            .json(UPDATED_JSON);
        UnhandledDTO unhandledDTO = unhandledMapper.toDto(updatedUnhandled);

        restUnhandledMockMvc.perform(put("/api/unhandleds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unhandledDTO)))
            .andExpect(status().isOk());

        // Validate the Unhandled in the database
        List<Unhandled> unhandledList = unhandledRepository.findAll();
        assertThat(unhandledList).hasSize(databaseSizeBeforeUpdate);
        Unhandled testUnhandled = unhandledList.get(unhandledList.size() - 1);
        assertThat(testUnhandled.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testUnhandled.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testUnhandled.getJson()).isEqualTo(UPDATED_JSON);

        // Validate the Unhandled in Elasticsearch
        verify(mockUnhandledSearchRepository, times(1)).save(testUnhandled);
    }

    @Test
    @Transactional
    public void updateNonExistingUnhandled() throws Exception {
        int databaseSizeBeforeUpdate = unhandledRepository.findAll().size();

        // Create the Unhandled
        UnhandledDTO unhandledDTO = unhandledMapper.toDto(unhandled);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restUnhandledMockMvc.perform(put("/api/unhandleds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unhandledDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Unhandled in the database
        List<Unhandled> unhandledList = unhandledRepository.findAll();
        assertThat(unhandledList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Unhandled in Elasticsearch
        verify(mockUnhandledSearchRepository, times(0)).save(unhandled);
    }

    @Test
    @Transactional
    public void deleteUnhandled() throws Exception {
        // Initialize the database
        unhandledRepository.saveAndFlush(unhandled);

        int databaseSizeBeforeDelete = unhandledRepository.findAll().size();

        // Get the unhandled
        restUnhandledMockMvc.perform(delete("/api/unhandleds/{id}", unhandled.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Unhandled> unhandledList = unhandledRepository.findAll();
        assertThat(unhandledList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Unhandled in Elasticsearch
        verify(mockUnhandledSearchRepository, times(1)).deleteById(unhandled.getId());
    }

    @Test
    @Transactional
    public void searchUnhandled() throws Exception {
        // Initialize the database
        unhandledRepository.saveAndFlush(unhandled);
        when(mockUnhandledSearchRepository.search(queryStringQuery("id:" + unhandled.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(unhandled), PageRequest.of(0, 1), 1));
        // Search the unhandled
        restUnhandledMockMvc.perform(get("/api/_search/unhandleds?query=id:" + unhandled.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unhandled.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].json").value(hasItem(DEFAULT_JSON.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Unhandled.class);
        Unhandled unhandled1 = new Unhandled();
        unhandled1.setId(1L);
        Unhandled unhandled2 = new Unhandled();
        unhandled2.setId(unhandled1.getId());
        assertThat(unhandled1).isEqualTo(unhandled2);
        unhandled2.setId(2L);
        assertThat(unhandled1).isNotEqualTo(unhandled2);
        unhandled1.setId(null);
        assertThat(unhandled1).isNotEqualTo(unhandled2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnhandledDTO.class);
        UnhandledDTO unhandledDTO1 = new UnhandledDTO();
        unhandledDTO1.setId(1L);
        UnhandledDTO unhandledDTO2 = new UnhandledDTO();
        assertThat(unhandledDTO1).isNotEqualTo(unhandledDTO2);
        unhandledDTO2.setId(unhandledDTO1.getId());
        assertThat(unhandledDTO1).isEqualTo(unhandledDTO2);
        unhandledDTO2.setId(2L);
        assertThat(unhandledDTO1).isNotEqualTo(unhandledDTO2);
        unhandledDTO1.setId(null);
        assertThat(unhandledDTO1).isNotEqualTo(unhandledDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(unhandledMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(unhandledMapper.fromId(null)).isNull();
    }
}
