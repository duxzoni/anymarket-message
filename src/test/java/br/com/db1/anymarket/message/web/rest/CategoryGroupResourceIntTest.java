package br.com.db1.anymarket.message.web.rest;

import br.com.db1.anymarket.message.AnymarketMessageApp;

import br.com.db1.anymarket.message.domain.CategoryGroup;
import br.com.db1.anymarket.message.repository.CategoryGroupRepository;
import br.com.db1.anymarket.message.repository.search.CategoryGroupSearchRepository;
import br.com.db1.anymarket.message.service.CategoryGroupService;
import br.com.db1.anymarket.message.service.dto.CategoryGroupDTO;
import br.com.db1.anymarket.message.service.mapper.CategoryGroupMapper;
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
 * Test class for the CategoryGroupResource REST controller.
 *
 * @see CategoryGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AnymarketMessageApp.class)
public class CategoryGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CategoryGroupRepository categoryGroupRepository;


    @Autowired
    private CategoryGroupMapper categoryGroupMapper;
    

    @Autowired
    private CategoryGroupService categoryGroupService;

    /**
     * This repository is mocked in the br.com.db1.anymarket.message.repository.search test package.
     *
     * @see br.com.db1.anymarket.message.repository.search.CategoryGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private CategoryGroupSearchRepository mockCategoryGroupSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCategoryGroupMockMvc;

    private CategoryGroup categoryGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CategoryGroupResource categoryGroupResource = new CategoryGroupResource(categoryGroupService);
        this.restCategoryGroupMockMvc = MockMvcBuilders.standaloneSetup(categoryGroupResource)
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
    public static CategoryGroup createEntity(EntityManager em) {
        CategoryGroup categoryGroup = new CategoryGroup()
            .name(DEFAULT_NAME);
        return categoryGroup;
    }

    @Before
    public void initTest() {
        categoryGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategoryGroup() throws Exception {
        int databaseSizeBeforeCreate = categoryGroupRepository.findAll().size();

        // Create the CategoryGroup
        CategoryGroupDTO categoryGroupDTO = categoryGroupMapper.toDto(categoryGroup);
        restCategoryGroupMockMvc.perform(post("/api/category-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the CategoryGroup in the database
        List<CategoryGroup> categoryGroupList = categoryGroupRepository.findAll();
        assertThat(categoryGroupList).hasSize(databaseSizeBeforeCreate + 1);
        CategoryGroup testCategoryGroup = categoryGroupList.get(categoryGroupList.size() - 1);
        assertThat(testCategoryGroup.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the CategoryGroup in Elasticsearch
        verify(mockCategoryGroupSearchRepository, times(1)).save(testCategoryGroup);
    }

    @Test
    @Transactional
    public void createCategoryGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categoryGroupRepository.findAll().size();

        // Create the CategoryGroup with an existing ID
        categoryGroup.setId(1L);
        CategoryGroupDTO categoryGroupDTO = categoryGroupMapper.toDto(categoryGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryGroupMockMvc.perform(post("/api/category-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryGroup in the database
        List<CategoryGroup> categoryGroupList = categoryGroupRepository.findAll();
        assertThat(categoryGroupList).hasSize(databaseSizeBeforeCreate);

        // Validate the CategoryGroup in Elasticsearch
        verify(mockCategoryGroupSearchRepository, times(0)).save(categoryGroup);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryGroupRepository.findAll().size();
        // set the field null
        categoryGroup.setName(null);

        // Create the CategoryGroup, which fails.
        CategoryGroupDTO categoryGroupDTO = categoryGroupMapper.toDto(categoryGroup);

        restCategoryGroupMockMvc.perform(post("/api/category-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryGroupDTO)))
            .andExpect(status().isBadRequest());

        List<CategoryGroup> categoryGroupList = categoryGroupRepository.findAll();
        assertThat(categoryGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategoryGroups() throws Exception {
        // Initialize the database
        categoryGroupRepository.saveAndFlush(categoryGroup);

        // Get all the categoryGroupList
        restCategoryGroupMockMvc.perform(get("/api/category-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getCategoryGroup() throws Exception {
        // Initialize the database
        categoryGroupRepository.saveAndFlush(categoryGroup);

        // Get the categoryGroup
        restCategoryGroupMockMvc.perform(get("/api/category-groups/{id}", categoryGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(categoryGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCategoryGroup() throws Exception {
        // Get the categoryGroup
        restCategoryGroupMockMvc.perform(get("/api/category-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategoryGroup() throws Exception {
        // Initialize the database
        categoryGroupRepository.saveAndFlush(categoryGroup);

        int databaseSizeBeforeUpdate = categoryGroupRepository.findAll().size();

        // Update the categoryGroup
        CategoryGroup updatedCategoryGroup = categoryGroupRepository.findById(categoryGroup.getId()).get();
        // Disconnect from session so that the updates on updatedCategoryGroup are not directly saved in db
        em.detach(updatedCategoryGroup);
        updatedCategoryGroup
            .name(UPDATED_NAME);
        CategoryGroupDTO categoryGroupDTO = categoryGroupMapper.toDto(updatedCategoryGroup);

        restCategoryGroupMockMvc.perform(put("/api/category-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryGroupDTO)))
            .andExpect(status().isOk());

        // Validate the CategoryGroup in the database
        List<CategoryGroup> categoryGroupList = categoryGroupRepository.findAll();
        assertThat(categoryGroupList).hasSize(databaseSizeBeforeUpdate);
        CategoryGroup testCategoryGroup = categoryGroupList.get(categoryGroupList.size() - 1);
        assertThat(testCategoryGroup.getName()).isEqualTo(UPDATED_NAME);

        // Validate the CategoryGroup in Elasticsearch
        verify(mockCategoryGroupSearchRepository, times(1)).save(testCategoryGroup);
    }

    @Test
    @Transactional
    public void updateNonExistingCategoryGroup() throws Exception {
        int databaseSizeBeforeUpdate = categoryGroupRepository.findAll().size();

        // Create the CategoryGroup
        CategoryGroupDTO categoryGroupDTO = categoryGroupMapper.toDto(categoryGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException 
        restCategoryGroupMockMvc.perform(put("/api/category-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryGroup in the database
        List<CategoryGroup> categoryGroupList = categoryGroupRepository.findAll();
        assertThat(categoryGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CategoryGroup in Elasticsearch
        verify(mockCategoryGroupSearchRepository, times(0)).save(categoryGroup);
    }

    @Test
    @Transactional
    public void deleteCategoryGroup() throws Exception {
        // Initialize the database
        categoryGroupRepository.saveAndFlush(categoryGroup);

        int databaseSizeBeforeDelete = categoryGroupRepository.findAll().size();

        // Get the categoryGroup
        restCategoryGroupMockMvc.perform(delete("/api/category-groups/{id}", categoryGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CategoryGroup> categoryGroupList = categoryGroupRepository.findAll();
        assertThat(categoryGroupList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CategoryGroup in Elasticsearch
        verify(mockCategoryGroupSearchRepository, times(1)).deleteById(categoryGroup.getId());
    }

    @Test
    @Transactional
    public void searchCategoryGroup() throws Exception {
        // Initialize the database
        categoryGroupRepository.saveAndFlush(categoryGroup);
        when(mockCategoryGroupSearchRepository.search(queryStringQuery("id:" + categoryGroup.getId())))
            .thenReturn(Collections.singletonList(categoryGroup));
        // Search the categoryGroup
        restCategoryGroupMockMvc.perform(get("/api/_search/category-groups?query=id:" + categoryGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryGroup.class);
        CategoryGroup categoryGroup1 = new CategoryGroup();
        categoryGroup1.setId(1L);
        CategoryGroup categoryGroup2 = new CategoryGroup();
        categoryGroup2.setId(categoryGroup1.getId());
        assertThat(categoryGroup1).isEqualTo(categoryGroup2);
        categoryGroup2.setId(2L);
        assertThat(categoryGroup1).isNotEqualTo(categoryGroup2);
        categoryGroup1.setId(null);
        assertThat(categoryGroup1).isNotEqualTo(categoryGroup2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryGroupDTO.class);
        CategoryGroupDTO categoryGroupDTO1 = new CategoryGroupDTO();
        categoryGroupDTO1.setId(1L);
        CategoryGroupDTO categoryGroupDTO2 = new CategoryGroupDTO();
        assertThat(categoryGroupDTO1).isNotEqualTo(categoryGroupDTO2);
        categoryGroupDTO2.setId(categoryGroupDTO1.getId());
        assertThat(categoryGroupDTO1).isEqualTo(categoryGroupDTO2);
        categoryGroupDTO2.setId(2L);
        assertThat(categoryGroupDTO1).isNotEqualTo(categoryGroupDTO2);
        categoryGroupDTO1.setId(null);
        assertThat(categoryGroupDTO1).isNotEqualTo(categoryGroupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(categoryGroupMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(categoryGroupMapper.fromId(null)).isNull();
    }
}
