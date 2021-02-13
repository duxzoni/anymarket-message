package br.com.db1.anymarket.message.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.db1.anymarket.message.service.CategoryGroupService;
import br.com.db1.anymarket.message.web.rest.errors.BadRequestAlertException;
import br.com.db1.anymarket.message.web.rest.util.HeaderUtil;
import br.com.db1.anymarket.message.service.dto.CategoryGroupDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CategoryGroup.
 */
@RestController
@RequestMapping("/api")
public class CategoryGroupResource {

    private final Logger log = LoggerFactory.getLogger(CategoryGroupResource.class);

    private static final String ENTITY_NAME = "categoryGroup";

    private final CategoryGroupService categoryGroupService;

    public CategoryGroupResource(CategoryGroupService categoryGroupService) {
        this.categoryGroupService = categoryGroupService;
    }

    /**
     * POST  /category-groups : Create a new categoryGroup.
     *
     * @param categoryGroupDTO the categoryGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new categoryGroupDTO, or with status 400 (Bad Request) if the categoryGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/category-groups")
    @Timed
    public ResponseEntity<CategoryGroupDTO> createCategoryGroup(@Valid @RequestBody CategoryGroupDTO categoryGroupDTO) throws URISyntaxException {
        log.debug("REST request to save CategoryGroup : {}", categoryGroupDTO);
        if (categoryGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoryGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategoryGroupDTO result = categoryGroupService.save(categoryGroupDTO);
        return ResponseEntity.created(new URI("/api/category-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /category-groups : Updates an existing categoryGroup.
     *
     * @param categoryGroupDTO the categoryGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated categoryGroupDTO,
     * or with status 400 (Bad Request) if the categoryGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the categoryGroupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/category-groups")
    @Timed
    public ResponseEntity<CategoryGroupDTO> updateCategoryGroup(@Valid @RequestBody CategoryGroupDTO categoryGroupDTO) throws URISyntaxException {
        log.debug("REST request to update CategoryGroup : {}", categoryGroupDTO);
        if (categoryGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CategoryGroupDTO result = categoryGroupService.save(categoryGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, categoryGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /category-groups : get all the categoryGroups.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of categoryGroups in body
     */
    @GetMapping("/category-groups")
    @Timed
    public List<CategoryGroupDTO> getAllCategoryGroups() {
        log.debug("REST request to get all CategoryGroups");
        return categoryGroupService.findAll();
    }

    /**
     * GET  /category-groups/:id : get the "id" categoryGroup.
     *
     * @param id the id of the categoryGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the categoryGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/category-groups/{id}")
    @Timed
    public ResponseEntity<CategoryGroupDTO> getCategoryGroup(@PathVariable Long id) {
        log.debug("REST request to get CategoryGroup : {}", id);
        Optional<CategoryGroupDTO> categoryGroupDTO = categoryGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryGroupDTO);
    }

    /**
     * DELETE  /category-groups/:id : delete the "id" categoryGroup.
     *
     * @param id the id of the categoryGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/category-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteCategoryGroup(@PathVariable Long id) {
        log.debug("REST request to delete CategoryGroup : {}", id);
        categoryGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/category-groups?query=:query : search for the categoryGroup corresponding
     * to the query.
     *
     * @param query the query of the categoryGroup search
     * @return the result of the search
     */
    @GetMapping("/_search/category-groups")
    @Timed
    public List<CategoryGroupDTO> searchCategoryGroups(@RequestParam String query) {
        log.debug("REST request to search CategoryGroups for query {}", query);
        return categoryGroupService.search(query);
    }

}
