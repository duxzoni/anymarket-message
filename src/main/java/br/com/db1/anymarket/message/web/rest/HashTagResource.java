package br.com.db1.anymarket.message.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.db1.anymarket.message.service.HashTagService;
import br.com.db1.anymarket.message.web.rest.errors.BadRequestAlertException;
import br.com.db1.anymarket.message.web.rest.util.HeaderUtil;
import br.com.db1.anymarket.message.service.dto.HashTagDTO;
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

/**
 * REST controller for managing HashTag.
 */
@RestController
@RequestMapping("/api")
public class HashTagResource {

    private final Logger log = LoggerFactory.getLogger(HashTagResource.class);

    private static final String ENTITY_NAME = "hashTag";

    private final HashTagService hashTagService;

    public HashTagResource(HashTagService hashTagService) {
        this.hashTagService = hashTagService;
    }

    /**
     * POST  /hash-tags : Create a new hashTag.
     *
     * @param hashTagDTO the hashTagDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hashTagDTO, or with status 400 (Bad Request) if the hashTag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hash-tags")
    @Timed
    public ResponseEntity<HashTagDTO> createHashTag(@Valid @RequestBody HashTagDTO hashTagDTO) throws URISyntaxException {
        log.debug("REST request to save HashTag : {}", hashTagDTO);
        if (hashTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new hashTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HashTagDTO result = hashTagService.save(hashTagDTO);
        return ResponseEntity.created(new URI("/api/hash-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hash-tags : Updates an existing hashTag.
     *
     * @param hashTagDTO the hashTagDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hashTagDTO,
     * or with status 400 (Bad Request) if the hashTagDTO is not valid,
     * or with status 500 (Internal Server Error) if the hashTagDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hash-tags")
    @Timed
    public ResponseEntity<HashTagDTO> updateHashTag(@Valid @RequestBody HashTagDTO hashTagDTO) throws URISyntaxException {
        log.debug("REST request to update HashTag : {}", hashTagDTO);
        if (hashTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HashTagDTO result = hashTagService.save(hashTagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hashTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hash-tags : get all the hashTags.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hashTags in body
     */
    @GetMapping("/hash-tags")
    @Timed
    public List<HashTagDTO> getAllHashTags() {
        log.debug("REST request to get all HashTags");
        return hashTagService.findAll();
    }

    /**
     * GET  /hash-tags/:id : get the "id" hashTag.
     *
     * @param id the id of the hashTagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hashTagDTO, or with status 404 (Not Found)
     */
    @GetMapping("/hash-tags/{id}")
    @Timed
    public ResponseEntity<HashTagDTO> getHashTag(@PathVariable Long id) {
        log.debug("REST request to get HashTag : {}", id);
        Optional<HashTagDTO> hashTagDTO = hashTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hashTagDTO);
    }

    /**
     * DELETE  /hash-tags/:id : delete the "id" hashTag.
     *
     * @param id the id of the hashTagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hash-tags/{id}")
    @Timed
    public ResponseEntity<Void> deleteHashTag(@PathVariable Long id) {
        log.debug("REST request to delete HashTag : {}", id);
        hashTagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
