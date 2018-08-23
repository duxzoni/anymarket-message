package br.com.db1.anymarket.message.service;

import br.com.db1.anymarket.message.service.dto.UnhandledDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Unhandled.
 */
public interface UnhandledService {

    /**
     * Save a unhandled.
     *
     * @param unhandledDTO the entity to save
     * @return the persisted entity
     */
    UnhandledDTO save(UnhandledDTO unhandledDTO);

    /**
     * Get all the unhandleds.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UnhandledDTO> findAll(Pageable pageable);


    /**
     * Get the "id" unhandled.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UnhandledDTO> findOne(Long id);

    /**
     * Delete the "id" unhandled.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the unhandled corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UnhandledDTO> search(String query, Pageable pageable);
}
