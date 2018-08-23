package br.com.db1.anymarket.message.repository;

import br.com.db1.anymarket.message.domain.Translated;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Translated entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TranslatedRepository extends JpaRepository<Translated, Long> {

}
