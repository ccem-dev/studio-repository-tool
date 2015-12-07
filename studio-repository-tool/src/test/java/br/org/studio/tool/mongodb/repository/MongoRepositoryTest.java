package br.org.studio.tool.mongodb.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.org.studio.tool.base.repository.Repository;
import br.org.studio.tool.base.repository.configuration.RepositoryConfiguration;
import br.org.studio.tool.mongodb.database.MongoClientFactory;
import br.org.studio.tool.mongodb.database.StudioMongoDatabase;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MongoRepository.class, MongoClientFactory.class })
public class MongoRepositoryTest {

	private static final String DBNAME = "dbname";

	@Mock
	private RepositoryConfiguration repositoryConfiguration;
	@Mock
	private StudioMongoDatabase database;

	private MongoRepository repository;

	@Before
	public void setup() throws Exception {
		when(repositoryConfiguration.getName()).thenReturn(DBNAME);

		repository = new MongoRepository(repositoryConfiguration);
	}

	@Test
	public void a_MongoRepository_should_be_an_instance_of_Repository() {
		assertThat(repository, instanceOf(Repository.class));
	}

	@Test
	public void a_MongoRepository_instance_should_has_a_configuration() {
		assertThat(repository.getConfiguration(), instanceOf(RepositoryConfiguration.class));
	}

	@Test
	public void initialize_method_should_calls() {
	}

	@Ignore
	@Test
	public void close_method_should_call_close_from_MongoDatabase() {
		repository.initialize();

		repository.close();

		verify(database).close();
	}

}