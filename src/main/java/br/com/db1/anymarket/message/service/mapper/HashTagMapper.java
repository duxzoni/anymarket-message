package br.com.db1.anymarket.message.service.mapper;

import br.com.db1.anymarket.message.domain.*;
import br.com.db1.anymarket.message.service.dto.HashTagDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity HashTag and its DTO HashTagDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HashTagMapper extends EntityMapper<HashTagDTO, HashTag> {



    default HashTag fromId(Long id) {
        if (id == null) {
            return null;
        }
        HashTag hashTag = new HashTag();
        hashTag.setId(id);
        return hashTag;
    }
}
