package br.com.db1.anymarket.message.service.impl;

import br.com.db1.anymarket.message.service.TranslatedService;
import br.com.db1.anymarket.message.domain.Translated;
import br.com.db1.anymarket.message.repository.TranslatedRepository;
import br.com.db1.anymarket.message.service.dto.TranslatedDTO;
import br.com.db1.anymarket.message.service.mapper.TranslatedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing Translated.
 */
@Service
@Transactional
public class TranslatedServiceImpl implements TranslatedService {

    private final Logger log = LoggerFactory.getLogger(TranslatedServiceImpl.class);

    private final TranslatedRepository translatedRepository;

    private final TranslatedMapper translatedMapper;

    public TranslatedServiceImpl(TranslatedRepository translatedRepository, TranslatedMapper translatedMapper) {
        this.translatedRepository = translatedRepository;
        this.translatedMapper = translatedMapper;
    }

    /**
     * Save a translated.
     *
     * @param translatedDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TranslatedDTO save(TranslatedDTO translatedDTO) {
        log.debug("Request to save Translated : {}", translatedDTO);
        Translated translated = translatedMapper.toEntity(translatedDTO);
        translated = translatedRepository.save(translated);
        return translatedMapper.toDto(translated);
    }

    /**
     * Get all the translateds.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TranslatedDTO> findAll() {
        log.debug("Request to get all Translateds");
        return translatedRepository.findAll().stream()
            .map(translatedMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one translated by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TranslatedDTO> findOne(Long id) {
        log.debug("Request to get Translated : {}", id);
        return translatedRepository.findById(id)
            .map(translatedMapper::toDto);
    }

    /**
     * Delete the translated by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Translated : {}", id);
        translatedRepository.deleteById(id);
    }
}
