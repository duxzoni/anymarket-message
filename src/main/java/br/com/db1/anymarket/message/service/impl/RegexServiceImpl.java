package br.com.db1.anymarket.message.service.impl;

import br.com.db1.anymarket.message.service.RegexService;
import br.com.db1.anymarket.message.domain.Regex;
import br.com.db1.anymarket.message.repository.RegexRepository;
import br.com.db1.anymarket.message.service.dto.RegexDTO;
import br.com.db1.anymarket.message.service.mapper.RegexMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing Regex.
 */
@Service
@Transactional
public class RegexServiceImpl implements RegexService {

    private final Logger log = LoggerFactory.getLogger(RegexServiceImpl.class);

    private final RegexRepository regexRepository;

    private final RegexMapper regexMapper;

    public RegexServiceImpl(RegexRepository regexRepository, RegexMapper regexMapper) {
        this.regexRepository = regexRepository;
        this.regexMapper = regexMapper;
    }

    /**
     * Save a regex.
     *
     * @param regexDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RegexDTO save(RegexDTO regexDTO) {
        log.debug("Request to save Regex : {}", regexDTO);
        Regex regex = regexMapper.toEntity(regexDTO);
        regex = regexRepository.save(regex);
        return regexMapper.toDto(regex);
    }

    /**
     * Get all the regexes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RegexDTO> findAll() {
        log.debug("Request to get all Regexes");
        return regexRepository.findAll().stream()
            .map(regexMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one regex by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RegexDTO> findOne(Long id) {
        log.debug("Request to get Regex : {}", id);
        return regexRepository.findById(id)
            .map(regexMapper::toDto);
    }

    /**
     * Delete the regex by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Regex : {}", id);
        regexRepository.deleteById(id);
    }
}
