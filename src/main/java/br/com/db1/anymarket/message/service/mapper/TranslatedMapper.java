package br.com.db1.anymarket.message.service.mapper;

import br.com.db1.anymarket.message.domain.*;
import br.com.db1.anymarket.message.service.dto.TranslatedDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Translated and its DTO TranslatedDTO.
 */
@Mapper(componentModel = "spring", uses = {MessageMapper.class, LanguageMapper.class})
public interface TranslatedMapper extends EntityMapper<TranslatedDTO, Translated> {

    @Mapping(source = "message.id", target = "messageId")
    @Mapping(source = "language.id", target = "languageId")
    TranslatedDTO toDto(Translated translated);

    @Mapping(source = "messageId", target = "message")
    @Mapping(source = "languageId", target = "language")
    Translated toEntity(TranslatedDTO translatedDTO);

    default Translated fromId(Long id) {
        if (id == null) {
            return null;
        }
        Translated translated = new Translated();
        translated.setId(id);
        return translated;
    }
}
