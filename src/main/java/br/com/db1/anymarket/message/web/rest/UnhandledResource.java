package br.com.db1.anymarket.message.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.db1.anymarket.message.service.UnhandledService;
import br.com.db1.anymarket.message.web.rest.errors.BadRequestAlertException;
import br.com.db1.anymarket.message.web.rest.util.HeaderUtil;
import br.com.db1.anymarket.message.web.rest.util.PaginationUtil;
import br.com.db1.anymarket.message.service.dto.UnhandledDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
 * REST controller for managing Unhandled.
 */
@RestController
@RequestMapping("/api")
public class UnhandledResource {

    private final Logger log = LoggerFactory.getLogger(UnhandledResource.class);

    private static final String ENTITY_NAME = "unhandled";

    private final UnhandledService unhandledService;

    public UnhandledResource(UnhandledService unhandledService) {
        this.unhandledService = unhandledService;
    }

    /**
     * POST  /unhandleds : Create a new unhandled.
     *
     * @param unhandledDTO the unhandledDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new unhandledDTO, or with status 400 (Bad Request) if the unhandled has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/unhandleds")
    @Timed
    public ResponseEntity<UnhandledDTO> createUnhandled(@Valid @RequestBody UnhandledDTO unhandledDTO) throws URISyntaxException {
        log.debug("REST request to save Unhandled : {}", unhandledDTO);
        if (unhandledDTO.getId() != null) {
            throw new BadRequestAlertException("A new unhandled cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UnhandledDTO result = unhandledService.save(unhandledDTO);
        return ResponseEntity.created(new URI("/api/unhandleds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /unhandleds : Updates an existing unhandled.
     *
     * @param unhandledDTO the unhandledDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated unhandledDTO,
     * or with status 400 (Bad Request) if the unhandledDTO is not valid,
     * or with status 500 (Internal Server Error) if the unhandledDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/unhandleds")
    @Timed
    public ResponseEntity<UnhandledDTO> updateUnhandled(@Valid @RequestBody UnhandledDTO unhandledDTO) throws URISyntaxException {
        log.debug("REST request to update Unhandled : {}", unhandledDTO);
        if (unhandledDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UnhandledDTO result = unhandledService.save(unhandledDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, unhandledDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /unhandleds : get all the unhandleds.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of unhandleds in body
     */
    @GetMapping("/unhandleds")
    @Timed
    public ResponseEntity<List<UnhandledDTO>> getAllUnhandleds(Pageable pageable) {
        log.debug("REST request to get a page of Unhandleds");
        Page<UnhandledDTO> page = unhandledService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/unhandleds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /unhandleds/:id : get the "id" unhandled.
     *
     * @param id the id of the unhandledDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the unhandledDTO, or with status 404 (Not Found)
     */
    @GetMapping("/unhandleds/{id}")
    @Timed
    public ResponseEntity<UnhandledDTO> getUnhandled(@PathVariable Long id) {
        log.debug("REST request to get Unhandled : {}", id);
        Optional<UnhandledDTO> unhandledDTO = unhandledService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unhandledDTO);
    }

    /**
     * DELETE  /unhandleds/:id : delete the "id" unhandled.
     *
     * @param id the id of the unhandledDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/unhandleds/{id}")
    @Timed
    public ResponseEntity<Void> deleteUnhandled(@PathVariable Long id) {
        log.debug("REST request to delete Unhandled : {}", id);
        unhandledService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/unhandleds?query=:query : search for the unhandled corresponding
     * to the query.
     *
     * @param query the query of the unhandled search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/unhandleds")
    @Timed
    public ResponseEntity<List<UnhandledDTO>> searchUnhandleds(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Unhandleds for query {}", query);
        Page<UnhandledDTO> page = unhandledService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/unhandleds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
