package br.com.db1.anymarket.message.service;

import br.com.db1.anymarket.message.service.dto.CategoryGroupDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing CategoryGroup.
 */
public interface CategoryGroupService {

    /**
     * Save a categoryGroup.
     *
     * @param categoryGroupDTO the entity to save
     * @return the persisted entity
     */
    CategoryGroupDTO save(CategoryGroupDTO categoryGroupDTO);

    /**
     * Get all the categoryGroups.
     *
     * @return the list of entities
     */
    List<CategoryGroupDTO> findAll();


    /**
     * Get the "id" categoryGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CategoryGroupDTO> findOne(Long id);

    /**
     * Delete the "id" categoryGroup.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the categoryGroup corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<CategoryGroupDTO> search(String query);
}
