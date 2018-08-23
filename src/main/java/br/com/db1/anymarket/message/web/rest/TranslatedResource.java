package br.com.db1.anymarket.message.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.db1.anymarket.message.service.TranslatedService;
import br.com.db1.anymarket.message.web.rest.errors.BadRequestAlertException;
import br.com.db1.anymarket.message.web.rest.util.HeaderUtil;
import br.com.db1.anymarket.message.service.dto.TranslatedDTO;
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
 * REST controller for managing Translated.
 */
@RestController
@RequestMapping("/api")
public class TranslatedResource {

    private final Logger log = LoggerFactory.getLogger(TranslatedResource.class);

    private static final String ENTITY_NAME = "translated";

    private final TranslatedService translatedService;

    public TranslatedResource(TranslatedService translatedService) {
        this.translatedService = translatedService;
    }

    /**
     * POST  /translateds : Create a new translated.
     *
     * @param translatedDTO the translatedDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new translatedDTO, or with status 400 (Bad Request) if the translated has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/translateds")
    @Timed
    public ResponseEntity<TranslatedDTO> createTranslated(@Valid @RequestBody TranslatedDTO translatedDTO) throws URISyntaxException {
        log.debug("REST request to save Translated : {}", translatedDTO);
        if (translatedDTO.getId() != null) {
            throw new BadRequestAlertException("A new translated cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TranslatedDTO result = translatedService.save(translatedDTO);
        return ResponseEntity.created(new URI("/api/translateds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /translateds : Updates an existing translated.
     *
     * @param translatedDTO the translatedDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated translatedDTO,
     * or with status 400 (Bad Request) if the translatedDTO is not valid,
     * or with status 500 (Internal Server Error) if the translatedDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/translateds")
    @Timed
    public ResponseEntity<TranslatedDTO> updateTranslated(@Valid @RequestBody TranslatedDTO translatedDTO) throws URISyntaxException {
        log.debug("REST request to update Translated : {}", translatedDTO);
        if (translatedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TranslatedDTO result = translatedService.save(translatedDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, translatedDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /translateds : get all the translateds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of translateds in body
     */
    @GetMapping("/translateds")
    @Timed
    public List<TranslatedDTO> getAllTranslateds() {
        log.debug("REST request to get all Translateds");
        return translatedService.findAll();
    }

    /**
     * GET  /translateds/:id : get the "id" translated.
     *
     * @param id the id of the translatedDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the translatedDTO, or with status 404 (Not Found)
     */
    @GetMapping("/translateds/{id}")
    @Timed
    public ResponseEntity<TranslatedDTO> getTranslated(@PathVariable Long id) {
        log.debug("REST request to get Translated : {}", id);
        Optional<TranslatedDTO> translatedDTO = translatedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(translatedDTO);
    }

    /**
     * DELETE  /translateds/:id : delete the "id" translated.
     *
     * @param id the id of the translatedDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/translateds/{id}")
    @Timed
    public ResponseEntity<Void> deleteTranslated(@PathVariable Long id) {
        log.debug("REST request to delete Translated : {}", id);
        translatedService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
