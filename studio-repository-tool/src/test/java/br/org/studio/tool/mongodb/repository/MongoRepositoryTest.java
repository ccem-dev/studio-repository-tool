package br.org.studio.tool.mongodb.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.org.studio.tool.base.repository.Repository;
import br.org.studio.tool.base.repository.RepositoryDescriptor;
import br.org.studio.tool.base.repository.configuration.RepositoryConfiguration;
import br.org.studio.tool.mongodb.database.StudioMongoDatabase;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MongoRepository.class })
public class MongoRepositoryTest {

    private static final String DBNAME = "dbname";

    @Mock
    private RepositoryConfiguration repositoryConfiguration;
    @Mock
    private StudioMongoDatabase database;

    private MongoRepository repository;

    @Before
    public void setup() throws Exception {
        whenNew(StudioMongoDatabase.class).withArguments(repositoryConfiguration).thenReturn(database);
        when(repositoryConfiguration.getDatabaseName()).thenReturn(DBNAME);
        repository = new MongoRepository(repositoryConfiguration);
    }

    @Test
    public void a_MongoRepository_should_be_an_instance_of_Repository() {
        assertThat(repository, instanceOf(Repository.class));
    }

    @Test
    public void a_MongoRepository_instance_should_has_a_descriptor() {
        assertThat(repository.getDescriptor(), instanceOf(RepositoryDescriptor.class));
    }

    @Test
    public void initialize_method_should_call_create_method_from_StudioMongoDatabase() {
        repository.create();

        verify(database).create();
    }

    @Test
    public void load_method_should_call_load_method_from_StudioMongoDatabase() {
        repository.load();

        verify(database).load();
    }

    @Test
    public void delete_method_should_call_drop_method_from_StudioMongoDatabase() throws Exception {
        repository.delete();

        verify(database).drop();
    }

    @Test
    public void close_method_should_call_close_from_MongoDatabase() {
        repository.create();

        repository.close();

        verify(database).close();
    }

    @Test
    public void isAccessible_method_should_return_false_when_server_is_accessible() {
        when(database.isAccessible()).thenReturn(true);

        assertThat(repository.isAccessible(), equalTo(true));
    }

    @Test
    public void isAccessible_method_should_return_true_when_server_is_accessible() {
        when(database.isAccessible()).thenReturn(false);

        assertThat(repository.isAccessible(), equalTo(false));
    }

}
