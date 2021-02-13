package br.com.db1.anymarket.message.service.mapper;

import br.com.db1.anymarket.message.domain.*;
import br.com.db1.anymarket.message.service.dto.UnhandledDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Unhandled and its DTO UnhandledDTO.
 */
@Mapper(componentModel = "spring", uses = {OriginMapper.class})
public interface UnhandledMapper extends EntityMapper<UnhandledDTO, Unhandled> {

    @Mapping(source = "origin.id", target = "originId")
    UnhandledDTO toDto(Unhandled unhandled);

    @Mapping(source = "originId", target = "origin")
    Unhandled toEntity(UnhandledDTO unhandledDTO);

    default Unhandled fromId(Long id) {
        if (id == null) {
            return null;
        }
        Unhandled unhandled = new Unhandled();
        unhandled.setId(id);
        return unhandled;
    }
}
