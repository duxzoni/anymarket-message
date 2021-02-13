package br.com.db1.anymarket.message.service.mapper;

import br.com.db1.anymarket.message.domain.*;
import br.com.db1.anymarket.message.service.dto.RegexDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Regex and its DTO RegexDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RegexMapper extends EntityMapper<RegexDTO, Regex> {



    default Regex fromId(Long id) {
        if (id == null) {
            return null;
        }
        Regex regex = new Regex();
        regex.setId(id);
        return regex;
    }
}
