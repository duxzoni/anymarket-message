package br.com.db1.anymarket.message.service.mapper;

import br.com.db1.anymarket.message.domain.*;
import br.com.db1.anymarket.message.service.dto.CategoryGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CategoryGroup and its DTO CategoryGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoryGroupMapper extends EntityMapper<CategoryGroupDTO, CategoryGroup> {



    default CategoryGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        CategoryGroup categoryGroup = new CategoryGroup();
        categoryGroup.setId(id);
        return categoryGroup;
    }
}
