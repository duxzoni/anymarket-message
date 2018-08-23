package br.com.db1.anymarket.message.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CategoryGroupSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CategoryGroupSearchRepositoryMockConfiguration {

    @MockBean
    private CategoryGroupSearchRepository mockCategoryGroupSearchRepository;

}
