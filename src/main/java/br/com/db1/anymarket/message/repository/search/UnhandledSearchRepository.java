package br.com.db1.anymarket.message.repository.search;

import br.com.db1.anymarket.message.domain.Unhandled;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Unhandled entity.
 */
public interface UnhandledSearchRepository extends ElasticsearchRepository<Unhandled, Long> {
}
