package br.com.db1.anymarket.message.service.impl;

import br.com.db1.anymarket.message.service.HashTagService;
import br.com.db1.anymarket.message.domain.HashTag;
import br.com.db1.anymarket.message.repository.HashTagRepository;
import br.com.db1.anymarket.message.service.dto.HashTagDTO;
import br.com.db1.anymarket.message.service.mapper.HashTagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing HashTag.
 */
@Service
@Transactional
public class HashTagServiceImpl implements HashTagService {

    private final Logger log = LoggerFactory.getLogger(HashTagServiceImpl.class);

    private final HashTagRepository hashTagRepository;

    private final HashTagMapper hashTagMapper;

    public HashTagServiceImpl(HashTagRepository hashTagRepository, HashTagMapper hashTagMapper) {
        this.hashTagRepository = hashTagRepository;
        this.hashTagMapper = hashTagMapper;
    }

    /**
     * Save a hashTag.
     *
     * @param hashTagDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public HashTagDTO save(HashTagDTO hashTagDTO) {
        log.debug("Request to save HashTag : {}", hashTagDTO);
        HashTag hashTag = hashTagMapper.toEntity(hashTagDTO);
        hashTag = hashTagRepository.save(hashTag);
        return hashTagMapper.toDto(hashTag);
    }

    /**
     * Get all the hashTags.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<HashTagDTO> findAll() {
        log.debug("Request to get all HashTags");
        return hashTagRepository.findAll().stream()
            .map(hashTagMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one hashTag by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HashTagDTO> findOne(Long id) {
        log.debug("Request to get HashTag : {}", id);
        return hashTagRepository.findById(id)
            .map(hashTagMapper::toDto);
    }

    /**
     * Delete the hashTag by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete HashTag : {}", id);
        hashTagRepository.deleteById(id);
    }
}
