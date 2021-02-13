package br.com.db1.anymarket.message.repository;

import br.com.db1.anymarket.message.domain.Regex;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Regex entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegexRepository extends JpaRepository<Regex, Long> {

}
