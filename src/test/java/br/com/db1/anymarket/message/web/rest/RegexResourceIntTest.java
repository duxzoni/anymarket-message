package br.com.db1.anymarket.message.web.rest;

import br.com.db1.anymarket.message.AnymarketMessageApp;

import br.com.db1.anymarket.message.domain.Regex;
import br.com.db1.anymarket.message.repository.RegexRepository;
import br.com.db1.anymarket.message.service.RegexService;
import br.com.db1.anymarket.message.service.dto.RegexDTO;
import br.com.db1.anymarket.message.service.mapper.RegexMapper;
import br.com.db1.anymarket.message.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static br.com.db1.anymarket.message.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RegexResource REST controller.
 *
 * @see RegexResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnymarketMessageApp.class)
public class RegexResourceIntTest {

    private static final String DEFAULT_STRING_PATTERN = "AAAAAAAAAA";
    private static final String UPDATED_STRING_PATTERN = "BBBBBBBBBB";

    @Autowired
    private RegexRepository regexRepository;


    @Autowired
    private RegexMapper regexMapper;
    

    @Autowired
    private RegexService regexService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRegexMockMvc;

    private Regex regex;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RegexResource regexResource = new RegexResource(regexService);
        this.restRegexMockMvc = MockMvcBuilders.standaloneSetup(regexResource)
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
    public static Regex createEntity(EntityManager em) {
        Regex regex = new Regex()
            .stringPattern(DEFAULT_STRING_PATTERN);
        return regex;
    }

    @Before
    public void initTest() {
        regex = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegex() throws Exception {
        int databaseSizeBeforeCreate = regexRepository.findAll().size();

        // Create the Regex
        RegexDTO regexDTO = regexMapper.toDto(regex);
        restRegexMockMvc.perform(post("/api/regexes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regexDTO)))
            .andExpect(status().isCreated());

        // Validate the Regex in the database
        List<Regex> regexList = regexRepository.findAll();
        assertThat(regexList).hasSize(databaseSizeBeforeCreate + 1);
        Regex testRegex = regexList.get(regexList.size() - 1);
        assertThat(testRegex.getStringPattern()).isEqualTo(DEFAULT_STRING_PATTERN);
    }

    @Test
    @Transactional
    public void createRegexWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = regexRepository.findAll().size();

        // Create the Regex with an existing ID
        regex.setId(1L);
        RegexDTO regexDTO = regexMapper.toDto(regex);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegexMockMvc.perform(post("/api/regexes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regexDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Regex in the database
        List<Regex> regexList = regexRepository.findAll();
        assertThat(regexList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStringPatternIsRequired() throws Exception {
        int databaseSizeBeforeTest = regexRepository.findAll().size();
        // set the field null
        regex.setStringPattern(null);

        // Create the Regex, which fails.
        RegexDTO regexDTO = regexMapper.toDto(regex);

        restRegexMockMvc.perform(post("/api/regexes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regexDTO)))
            .andExpect(status().isBadRequest());

        List<Regex> regexList = regexRepository.findAll();
        assertThat(regexList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRegexes() throws Exception {
        // Initialize the database
        regexRepository.saveAndFlush(regex);

        // Get all the regexList
        restRegexMockMvc.perform(get("/api/regexes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regex.getId().intValue())))
            .andExpect(jsonPath("$.[*].stringPattern").value(hasItem(DEFAULT_STRING_PATTERN.toString())));
    }
    

    @Test
    @Transactional
    public void getRegex() throws Exception {
        // Initialize the database
        regexRepository.saveAndFlush(regex);

        // Get the regex
        restRegexMockMvc.perform(get("/api/regexes/{id}", regex.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(regex.getId().intValue()))
            .andExpect(jsonPath("$.stringPattern").value(DEFAULT_STRING_PATTERN.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingRegex() throws Exception {
        // Get the regex
        restRegexMockMvc.perform(get("/api/regexes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegex() throws Exception {
        // Initialize the database
        regexRepository.saveAndFlush(regex);

        int databaseSizeBeforeUpdate = regexRepository.findAll().size();

        // Update the regex
        Regex updatedRegex = regexRepository.findById(regex.getId()).get();
        // Disconnect from session so that the updates on updatedRegex are not directly saved in db
        em.detach(updatedRegex);
        updatedRegex
            .stringPattern(UPDATED_STRING_PATTERN);
        RegexDTO regexDTO = regexMapper.toDto(updatedRegex);

        restRegexMockMvc.perform(put("/api/regexes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regexDTO)))
            .andExpect(status().isOk());

        // Validate the Regex in the database
        List<Regex> regexList = regexRepository.findAll();
        assertThat(regexList).hasSize(databaseSizeBeforeUpdate);
        Regex testRegex = regexList.get(regexList.size() - 1);
        assertThat(testRegex.getStringPattern()).isEqualTo(UPDATED_STRING_PATTERN);
    }

    @Test
    @Transactional
    public void updateNonExistingRegex() throws Exception {
        int databaseSizeBeforeUpdate = regexRepository.findAll().size();

        // Create the Regex
        RegexDTO regexDTO = regexMapper.toDto(regex);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restRegexMockMvc.perform(put("/api/regexes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(regexDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Regex in the database
        List<Regex> regexList = regexRepository.findAll();
        assertThat(regexList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRegex() throws Exception {
        // Initialize the database
        regexRepository.saveAndFlush(regex);

        int databaseSizeBeforeDelete = regexRepository.findAll().size();

        // Get the regex
        restRegexMockMvc.perform(delete("/api/regexes/{id}", regex.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Regex> regexList = regexRepository.findAll();
        assertThat(regexList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Regex.class);
        Regex regex1 = new Regex();
        regex1.setId(1L);
        Regex regex2 = new Regex();
        regex2.setId(regex1.getId());
        assertThat(regex1).isEqualTo(regex2);
        regex2.setId(2L);
        assertThat(regex1).isNotEqualTo(regex2);
        regex1.setId(null);
        assertThat(regex1).isNotEqualTo(regex2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegexDTO.class);
        RegexDTO regexDTO1 = new RegexDTO();
        regexDTO1.setId(1L);
        RegexDTO regexDTO2 = new RegexDTO();
        assertThat(regexDTO1).isNotEqualTo(regexDTO2);
        regexDTO2.setId(regexDTO1.getId());
        assertThat(regexDTO1).isEqualTo(regexDTO2);
        regexDTO2.setId(2L);
        assertThat(regexDTO1).isNotEqualTo(regexDTO2);
        regexDTO1.setId(null);
        assertThat(regexDTO1).isNotEqualTo(regexDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(regexMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(regexMapper.fromId(null)).isNull();
    }
}
