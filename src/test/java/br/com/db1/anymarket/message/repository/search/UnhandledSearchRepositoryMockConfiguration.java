package br.com.db1.anymarket.message.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of UnhandledSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class UnhandledSearchRepositoryMockConfiguration {

    @MockBean
    private UnhandledSearchRepository mockUnhandledSearchRepository;

}
