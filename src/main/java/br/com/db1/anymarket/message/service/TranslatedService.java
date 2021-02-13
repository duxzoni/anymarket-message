package br.com.db1.anymarket.message.service;

import br.com.db1.anymarket.message.service.dto.TranslatedDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Translated.
 */
public interface TranslatedService {

    /**
     * Save a translated.
     *
     * @param translatedDTO the entity to save
     * @return the persisted entity
     */
    TranslatedDTO save(TranslatedDTO translatedDTO);

    /**
     * Get all the translateds.
     *
     * @return the list of entities
     */
    List<TranslatedDTO> findAll();


    /**
     * Get the "id" translated.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TranslatedDTO> findOne(Long id);

    /**
     * Delete the "id" translated.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
