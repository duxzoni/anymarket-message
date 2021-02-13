package br.com.db1.anymarket.message.web.rest;

import br.com.db1.anymarket.message.AnymarketMessageApp;

import br.com.db1.anymarket.message.domain.Translated;
import br.com.db1.anymarket.message.domain.Language;
import br.com.db1.anymarket.message.repository.TranslatedRepository;
import br.com.db1.anymarket.message.service.TranslatedService;
import br.com.db1.anymarket.message.service.dto.TranslatedDTO;
import br.com.db1.anymarket.message.service.mapper.TranslatedMapper;
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
 * Test class for the TranslatedResource REST controller.
 *
 * @see TranslatedResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnymarketMessageApp.class)
public class TranslatedResourceIntTest {

    private static final String DEFAULT_TRANSLATED_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSLATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION_TO_FIX = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_TO_FIX = "BBBBBBBBBB";

    private static final String DEFAULT_ARTICLE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE_LINK = "BBBBBBBBBB";

    @Autowired
    private TranslatedRepository translatedRepository;


    @Autowired
    private TranslatedMapper translatedMapper;
    

    @Autowired
    private TranslatedService translatedService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTranslatedMockMvc;

    private Translated translated;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TranslatedResource translatedResource = new TranslatedResource(translatedService);
        this.restTranslatedMockMvc = MockMvcBuilders.standaloneSetup(translatedResource)
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
    public static Translated createEntity(EntityManager em) {
        Translated translated = new Translated()
            .translatedMessage(DEFAULT_TRANSLATED_MESSAGE)
            .reason(DEFAULT_REASON)
            .actionToFix(DEFAULT_ACTION_TO_FIX)
            .articleLink(DEFAULT_ARTICLE_LINK);
        // Add required entity
        Language language = LanguageResourceIntTest.createEntity(em);
        em.persist(language);
        em.flush();
        translated.setLanguage(language);
        return translated;
    }

    @Before
    public void initTest() {
        translated = createEntity(em);
    }

    @Test
    @Transactional
    public void createTranslated() throws Exception {
        int databaseSizeBeforeCreate = translatedRepository.findAll().size();

        // Create the Translated
        TranslatedDTO translatedDTO = translatedMapper.toDto(translated);
        restTranslatedMockMvc.perform(post("/api/translateds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translatedDTO)))
            .andExpect(status().isCreated());

        // Validate the Translated in the database
        List<Translated> translatedList = translatedRepository.findAll();
        assertThat(translatedList).hasSize(databaseSizeBeforeCreate + 1);
        Translated testTranslated = translatedList.get(translatedList.size() - 1);
        assertThat(testTranslated.getTranslatedMessage()).isEqualTo(DEFAULT_TRANSLATED_MESSAGE);
        assertThat(testTranslated.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testTranslated.getActionToFix()).isEqualTo(DEFAULT_ACTION_TO_FIX);
        assertThat(testTranslated.getArticleLink()).isEqualTo(DEFAULT_ARTICLE_LINK);
    }

    @Test
    @Transactional
    public void createTranslatedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = translatedRepository.findAll().size();

        // Create the Translated with an existing ID
        translated.setId(1L);
        TranslatedDTO translatedDTO = translatedMapper.toDto(translated);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranslatedMockMvc.perform(post("/api/translateds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translatedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Translated in the database
        List<Translated> translatedList = translatedRepository.findAll();
        assertThat(translatedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTranslatedMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = translatedRepository.findAll().size();
        // set the field null
        translated.setTranslatedMessage(null);

        // Create the Translated, which fails.
        TranslatedDTO translatedDTO = translatedMapper.toDto(translated);

        restTranslatedMockMvc.perform(post("/api/translateds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translatedDTO)))
            .andExpect(status().isBadRequest());

        List<Translated> translatedList = translatedRepository.findAll();
        assertThat(translatedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = translatedRepository.findAll().size();
        // set the field null
        translated.setReason(null);

        // Create the Translated, which fails.
        TranslatedDTO translatedDTO = translatedMapper.toDto(translated);

        restTranslatedMockMvc.perform(post("/api/translateds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translatedDTO)))
            .andExpect(status().isBadRequest());

        List<Translated> translatedList = translatedRepository.findAll();
        assertThat(translatedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActionToFixIsRequired() throws Exception {
        int databaseSizeBeforeTest = translatedRepository.findAll().size();
        // set the field null
        translated.setActionToFix(null);

        // Create the Translated, which fails.
        TranslatedDTO translatedDTO = translatedMapper.toDto(translated);

        restTranslatedMockMvc.perform(post("/api/translateds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translatedDTO)))
            .andExpect(status().isBadRequest());

        List<Translated> translatedList = translatedRepository.findAll();
        assertThat(translatedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTranslateds() throws Exception {
        // Initialize the database
        translatedRepository.saveAndFlush(translated);

        // Get all the translatedList
        restTranslatedMockMvc.perform(get("/api/translateds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(translated.getId().intValue())))
            .andExpect(jsonPath("$.[*].translatedMessage").value(hasItem(DEFAULT_TRANSLATED_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())))
            .andExpect(jsonPath("$.[*].actionToFix").value(hasItem(DEFAULT_ACTION_TO_FIX.toString())))
            .andExpect(jsonPath("$.[*].articleLink").value(hasItem(DEFAULT_ARTICLE_LINK.toString())));
    }
    

    @Test
    @Transactional
    public void getTranslated() throws Exception {
        // Initialize the database
        translatedRepository.saveAndFlush(translated);

        // Get the translated
        restTranslatedMockMvc.perform(get("/api/translateds/{id}", translated.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(translated.getId().intValue()))
            .andExpect(jsonPath("$.translatedMessage").value(DEFAULT_TRANSLATED_MESSAGE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()))
            .andExpect(jsonPath("$.actionToFix").value(DEFAULT_ACTION_TO_FIX.toString()))
            .andExpect(jsonPath("$.articleLink").value(DEFAULT_ARTICLE_LINK.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingTranslated() throws Exception {
        // Get the translated
        restTranslatedMockMvc.perform(get("/api/translateds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTranslated() throws Exception {
        // Initialize the database
        translatedRepository.saveAndFlush(translated);

        int databaseSizeBeforeUpdate = translatedRepository.findAll().size();

        // Update the translated
        Translated updatedTranslated = translatedRepository.findById(translated.getId()).get();
        // Disconnect from session so that the updates on updatedTranslated are not directly saved in db
        em.detach(updatedTranslated);
        updatedTranslated
            .translatedMessage(UPDATED_TRANSLATED_MESSAGE)
            .reason(UPDATED_REASON)
            .actionToFix(UPDATED_ACTION_TO_FIX)
            .articleLink(UPDATED_ARTICLE_LINK);
        TranslatedDTO translatedDTO = translatedMapper.toDto(updatedTranslated);

        restTranslatedMockMvc.perform(put("/api/translateds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translatedDTO)))
            .andExpect(status().isOk());

        // Validate the Translated in the database
        List<Translated> translatedList = translatedRepository.findAll();
        assertThat(translatedList).hasSize(databaseSizeBeforeUpdate);
        Translated testTranslated = translatedList.get(translatedList.size() - 1);
        assertThat(testTranslated.getTranslatedMessage()).isEqualTo(UPDATED_TRANSLATED_MESSAGE);
        assertThat(testTranslated.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testTranslated.getActionToFix()).isEqualTo(UPDATED_ACTION_TO_FIX);
        assertThat(testTranslated.getArticleLink()).isEqualTo(UPDATED_ARTICLE_LINK);
    }

    @Test
    @Transactional
    public void updateNonExistingTranslated() throws Exception {
        int databaseSizeBeforeUpdate = translatedRepository.findAll().size();

        // Create the Translated
        TranslatedDTO translatedDTO = translatedMapper.toDto(translated);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restTranslatedMockMvc.perform(put("/api/translateds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(translatedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Translated in the database
        List<Translated> translatedList = translatedRepository.findAll();
        assertThat(translatedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTranslated() throws Exception {
        // Initialize the database
        translatedRepository.saveAndFlush(translated);

        int databaseSizeBeforeDelete = translatedRepository.findAll().size();

        // Get the translated
        restTranslatedMockMvc.perform(delete("/api/translateds/{id}", translated.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Translated> translatedList = translatedRepository.findAll();
        assertThat(translatedList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Translated.class);
        Translated translated1 = new Translated();
        translated1.setId(1L);
        Translated translated2 = new Translated();
        translated2.setId(translated1.getId());
        assertThat(translated1).isEqualTo(translated2);
        translated2.setId(2L);
        assertThat(translated1).isNotEqualTo(translated2);
        translated1.setId(null);
        assertThat(translated1).isNotEqualTo(translated2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TranslatedDTO.class);
        TranslatedDTO translatedDTO1 = new TranslatedDTO();
        translatedDTO1.setId(1L);
        TranslatedDTO translatedDTO2 = new TranslatedDTO();
        assertThat(translatedDTO1).isNotEqualTo(translatedDTO2);
        translatedDTO2.setId(translatedDTO1.getId());
        assertThat(translatedDTO1).isEqualTo(translatedDTO2);
        translatedDTO2.setId(2L);
        assertThat(translatedDTO1).isNotEqualTo(translatedDTO2);
        translatedDTO1.setId(null);
        assertThat(translatedDTO1).isNotEqualTo(translatedDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(translatedMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(translatedMapper.fromId(null)).isNull();
    }
}
