package br.com.db1.anymarket.message.repository;

import br.com.db1.anymarket.message.domain.Unhandled;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Unhandled entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnhandledRepository extends JpaRepository<Unhandled, Long> {

}
