package br.com.db1.anymarket.message.service.impl;

import br.com.db1.anymarket.message.service.UnhandledService;
import br.com.db1.anymarket.message.domain.Unhandled;
import br.com.db1.anymarket.message.repository.UnhandledRepository;
import br.com.db1.anymarket.message.repository.search.UnhandledSearchRepository;
import br.com.db1.anymarket.message.service.dto.UnhandledDTO;
import br.com.db1.anymarket.message.service.mapper.UnhandledMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Unhandled.
 */
@Service
@Transactional
public class UnhandledServiceImpl implements UnhandledService {

    private final Logger log = LoggerFactory.getLogger(UnhandledServiceImpl.class);

    private final UnhandledRepository unhandledRepository;

    private final UnhandledMapper unhandledMapper;

    private final UnhandledSearchRepository unhandledSearchRepository;

    public UnhandledServiceImpl(UnhandledRepository unhandledRepository, UnhandledMapper unhandledMapper, UnhandledSearchRepository unhandledSearchRepository) {
        this.unhandledRepository = unhandledRepository;
        this.unhandledMapper = unhandledMapper;
        this.unhandledSearchRepository = unhandledSearchRepository;
    }

    /**
     * Save a unhandled.
     *
     * @param unhandledDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UnhandledDTO save(UnhandledDTO unhandledDTO) {
        log.debug("Request to save Unhandled : {}", unhandledDTO);
        Unhandled unhandled = unhandledMapper.toEntity(unhandledDTO);
        unhandled = unhandledRepository.save(unhandled);
        UnhandledDTO result = unhandledMapper.toDto(unhandled);
        unhandledSearchRepository.save(unhandled);
        return result;
    }

    /**
     * Get all the unhandleds.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UnhandledDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Unhandleds");
        return unhandledRepository.findAll(pageable)
            .map(unhandledMapper::toDto);
    }


    /**
     * Get one unhandled by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UnhandledDTO> findOne(Long id) {
        log.debug("Request to get Unhandled : {}", id);
        return unhandledRepository.findById(id)
            .map(unhandledMapper::toDto);
    }

    /**
     * Delete the unhandled by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Unhandled : {}", id);
        unhandledRepository.deleteById(id);
        unhandledSearchRepository.deleteById(id);
    }

    /**
     * Search for the unhandled corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UnhandledDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Unhandleds for query {}", query);
        return unhandledSearchRepository.search(queryStringQuery(query), pageable)
            .map(unhandledMapper::toDto);
    }
}
