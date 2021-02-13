package br.com.db1.anymarket.message.service.mapper;

import br.com.db1.anymarket.message.domain.*;
import br.com.db1.anymarket.message.service.dto.MessageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Message and its DTO MessageDTO.
 */
@Mapper(componentModel = "spring", uses = {RegexMapper.class, OriginMapper.class, CategoryMapper.class, HashTagMapper.class})
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {

    @Mapping(source = "regex.id", target = "regexId")
    @Mapping(source = "origin.id", target = "originId")
    @Mapping(source = "category.id", target = "categoryId")
    MessageDTO toDto(Message message);

    @Mapping(source = "regexId", target = "regex")
    @Mapping(target = "translateds", ignore = true)
    @Mapping(source = "originId", target = "origin")
    @Mapping(source = "categoryId", target = "category")
    Message toEntity(MessageDTO messageDTO);

    default Message fromId(Long id) {
        if (id == null) {
            return null;
        }
        Message message = new Message();
        message.setId(id);
        return message;
    }
}
