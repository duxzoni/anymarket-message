package br.com.db1.anymarket.message.repository;

import br.com.db1.anymarket.message.domain.HashTag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HashTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {

}
