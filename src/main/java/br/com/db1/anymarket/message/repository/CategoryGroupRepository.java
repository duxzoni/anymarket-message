package br.com.db1.anymarket.message.repository;

import br.com.db1.anymarket.message.domain.CategoryGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CategoryGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {

}
