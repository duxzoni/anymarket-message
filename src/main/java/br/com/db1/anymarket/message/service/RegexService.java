package br.com.db1.anymarket.message.service;

import br.com.db1.anymarket.message.service.dto.RegexDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Regex.
 */
public interface RegexService {

    /**
     * Save a regex.
     *
     * @param regexDTO the entity to save
     * @return the persisted entity
     */
    RegexDTO save(RegexDTO regexDTO);

    /**
     * Get all the regexes.
     *
     * @return the list of entities
     */
    List<RegexDTO> findAll();


    /**
     * Get the "id" regex.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<RegexDTO> findOne(Long id);

    /**
     * Delete the "id" regex.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
