package br.com.db1.anymarket.message.service.impl;

import br.com.db1.anymarket.message.service.CategoryGroupService;
import br.com.db1.anymarket.message.domain.CategoryGroup;
import br.com.db1.anymarket.message.repository.CategoryGroupRepository;
import br.com.db1.anymarket.message.repository.search.CategoryGroupSearchRepository;
import br.com.db1.anymarket.message.service.dto.CategoryGroupDTO;
import br.com.db1.anymarket.message.service.mapper.CategoryGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CategoryGroup.
 */
@Service
@Transactional
public class CategoryGroupServiceImpl implements CategoryGroupService {

    private final Logger log = LoggerFactory.getLogger(CategoryGroupServiceImpl.class);

    private final CategoryGroupRepository categoryGroupRepository;

    private final CategoryGroupMapper categoryGroupMapper;

    private final CategoryGroupSearchRepository categoryGroupSearchRepository;

    public CategoryGroupServiceImpl(CategoryGroupRepository categoryGroupRepository, CategoryGroupMapper categoryGroupMapper, CategoryGroupSearchRepository categoryGroupSearchRepository) {
        this.categoryGroupRepository = categoryGroupRepository;
        this.categoryGroupMapper = categoryGroupMapper;
        this.categoryGroupSearchRepository = categoryGroupSearchRepository;
    }

    /**
     * Save a categoryGroup.
     *
     * @param categoryGroupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CategoryGroupDTO save(CategoryGroupDTO categoryGroupDTO) {
        log.debug("Request to save CategoryGroup : {}", categoryGroupDTO);
        CategoryGroup categoryGroup = categoryGroupMapper.toEntity(categoryGroupDTO);
        categoryGroup = categoryGroupRepository.save(categoryGroup);
        CategoryGroupDTO result = categoryGroupMapper.toDto(categoryGroup);
        categoryGroupSearchRepository.save(categoryGroup);
        return result;
    }

    /**
     * Get all the categoryGroups.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryGroupDTO> findAll() {
        log.debug("Request to get all CategoryGroups");
        return categoryGroupRepository.findAll().stream()
            .map(categoryGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one categoryGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryGroupDTO> findOne(Long id) {
        log.debug("Request to get CategoryGroup : {}", id);
        return categoryGroupRepository.findById(id)
            .map(categoryGroupMapper::toDto);
    }

    /**
     * Delete the categoryGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CategoryGroup : {}", id);
        categoryGroupRepository.deleteById(id);
        categoryGroupSearchRepository.deleteById(id);
    }

    /**
     * Search for the categoryGroup corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryGroupDTO> search(String query) {
        log.debug("Request to search CategoryGroups for query {}", query);
        return StreamSupport
            .stream(categoryGroupSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(categoryGroupMapper::toDto)
            .collect(Collectors.toList());
    }
}
