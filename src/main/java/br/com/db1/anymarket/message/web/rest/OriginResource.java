package br.com.db1.anymarket.message.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.db1.anymarket.message.service.OriginService;
import br.com.db1.anymarket.message.web.rest.errors.BadRequestAlertException;
import br.com.db1.anymarket.message.web.rest.util.HeaderUtil;
import br.com.db1.anymarket.message.service.dto.OriginDTO;
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
 * REST controller for managing Origin.
 */
@RestController
@RequestMapping("/api")
public class OriginResource {

    private final Logger log = LoggerFactory.getLogger(OriginResource.class);

    private static final String ENTITY_NAME = "origin";

    private final OriginService originService;

    public OriginResource(OriginService originService) {
        this.originService = originService;
    }

    /**
     * POST  /origins : Create a new origin.
     *
     * @param originDTO the originDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new originDTO, or with status 400 (Bad Request) if the origin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/origins")
    @Timed
    public ResponseEntity<OriginDTO> createOrigin(@Valid @RequestBody OriginDTO originDTO) throws URISyntaxException {
        log.debug("REST request to save Origin : {}", originDTO);
        if (originDTO.getId() != null) {
            throw new BadRequestAlertException("A new origin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OriginDTO result = originService.save(originDTO);
        return ResponseEntity.created(new URI("/api/origins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /origins : Updates an existing origin.
     *
     * @param originDTO the originDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated originDTO,
     * or with status 400 (Bad Request) if the originDTO is not valid,
     * or with status 500 (Internal Server Error) if the originDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/origins")
    @Timed
    public ResponseEntity<OriginDTO> updateOrigin(@Valid @RequestBody OriginDTO originDTO) throws URISyntaxException {
        log.debug("REST request to update Origin : {}", originDTO);
        if (originDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OriginDTO result = originService.save(originDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, originDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /origins : get all the origins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of origins in body
     */
    @GetMapping("/origins")
    @Timed
    public List<OriginDTO> getAllOrigins() {
        log.debug("REST request to get all Origins");
        return originService.findAll();
    }

    /**
     * GET  /origins/:id : get the "id" origin.
     *
     * @param id the id of the originDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the originDTO, or with status 404 (Not Found)
     */
    @GetMapping("/origins/{id}")
    @Timed
    public ResponseEntity<OriginDTO> getOrigin(@PathVariable Long id) {
        log.debug("REST request to get Origin : {}", id);
        Optional<OriginDTO> originDTO = originService.findOne(id);
        return ResponseUtil.wrapOrNotFound(originDTO);
    }

    /**
     * DELETE  /origins/:id : delete the "id" origin.
     *
     * @param id the id of the originDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/origins/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrigin(@PathVariable Long id) {
        log.debug("REST request to delete Origin : {}", id);
        originService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
