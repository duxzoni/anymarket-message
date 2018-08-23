package br.com.db1.anymarket.message.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.db1.anymarket.message.service.RegexService;
import br.com.db1.anymarket.message.web.rest.errors.BadRequestAlertException;
import br.com.db1.anymarket.message.web.rest.util.HeaderUtil;
import br.com.db1.anymarket.message.service.dto.RegexDTO;
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
 * REST controller for managing Regex.
 */
@RestController
@RequestMapping("/api")
public class RegexResource {

    private final Logger log = LoggerFactory.getLogger(RegexResource.class);

    private static final String ENTITY_NAME = "regex";

    private final RegexService regexService;

    public RegexResource(RegexService regexService) {
        this.regexService = regexService;
    }

    /**
     * POST  /regexes : Create a new regex.
     *
     * @param regexDTO the regexDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new regexDTO, or with status 400 (Bad Request) if the regex has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/regexes")
    @Timed
    public ResponseEntity<RegexDTO> createRegex(@Valid @RequestBody RegexDTO regexDTO) throws URISyntaxException {
        log.debug("REST request to save Regex : {}", regexDTO);
        if (regexDTO.getId() != null) {
            throw new BadRequestAlertException("A new regex cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegexDTO result = regexService.save(regexDTO);
        return ResponseEntity.created(new URI("/api/regexes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /regexes : Updates an existing regex.
     *
     * @param regexDTO the regexDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated regexDTO,
     * or with status 400 (Bad Request) if the regexDTO is not valid,
     * or with status 500 (Internal Server Error) if the regexDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/regexes")
    @Timed
    public ResponseEntity<RegexDTO> updateRegex(@Valid @RequestBody RegexDTO regexDTO) throws URISyntaxException {
        log.debug("REST request to update Regex : {}", regexDTO);
        if (regexDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegexDTO result = regexService.save(regexDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, regexDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /regexes : get all the regexes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of regexes in body
     */
    @GetMapping("/regexes")
    @Timed
    public List<RegexDTO> getAllRegexes() {
        log.debug("REST request to get all Regexes");
        return regexService.findAll();
    }

    /**
     * GET  /regexes/:id : get the "id" regex.
     *
     * @param id the id of the regexDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the regexDTO, or with status 404 (Not Found)
     */
    @GetMapping("/regexes/{id}")
    @Timed
    public ResponseEntity<RegexDTO> getRegex(@PathVariable Long id) {
        log.debug("REST request to get Regex : {}", id);
        Optional<RegexDTO> regexDTO = regexService.findOne(id);
        return ResponseUtil.wrapOrNotFound(regexDTO);
    }

    /**
     * DELETE  /regexes/:id : delete the "id" regex.
     *
     * @param id the id of the regexDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/regexes/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegex(@PathVariable Long id) {
        log.debug("REST request to delete Regex : {}", id);
        regexService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
