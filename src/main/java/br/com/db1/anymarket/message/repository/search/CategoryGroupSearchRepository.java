package br.com.db1.anymarket.message.repository.search;

import br.com.db1.anymarket.message.domain.CategoryGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CategoryGroup entity.
 */
public interface CategoryGroupSearchRepository extends ElasticsearchRepository<CategoryGroup, Long> {
}
