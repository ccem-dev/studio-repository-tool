package br.org.studio.tool.mongodb.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.org.studio.tool.base.database.DatabaseUrl;
import br.org.studio.tool.base.repository.DefaultRepositoryDescriptor;
import br.org.studio.tool.base.repository.RepositoryConnectionData;
import br.org.studio.tool.base.repository.RepositoryType;
import br.org.studio.tool.base.repository.configuration.RepositoryConfiguration;

@RunWith(MockitoJUnitRunner.class)
public class MongoRepositoryConfigurationTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String PORT = "5432";
    private static final String HOST = "localhost";
    private static final String DBNAME = "repository_name";
    private static final String MONGO_PROTOCOL = "mongodb://";
    private static final String CONNECTION_URL = MONGO_PROTOCOL + HOST + ":" + PORT + "/" + DBNAME;

    private RepositoryConfiguration repositoryConfiguration;
    private DefaultRepositoryDescriptor descriptor;
    private RepositoryConnectionData repositoryConnectionData;

    @Mock
    private DatabaseUrl databaseUrl;

    @Before
    public void setup() {
    	createRepositoryConnectionData();
        createDescriptor();
        createRepositoryConfiguration();
    }
    
    private void createRepositoryConnectionData() {
    	repositoryConnectionData = new RepositoryConnectionData();
    	repositoryConnectionData.setHost(HOST);
    	repositoryConnectionData.setPort(PORT);
    	repositoryConnectionData.setPassword(PASSWORD);
    	repositoryConnectionData.setUsername(USER);
    	repositoryConnectionData.setDatabase(DBNAME);
    }
    

    private void createDescriptor() {
        descriptor = new DefaultRepositoryDescriptor();
        descriptor.setDatabaseName(DBNAME);
        descriptor.setUser(USER);
        descriptor.setPassword(PASSWORD);
        descriptor.setRepositoryConnectionDataDescriptor(repositoryConnectionData);
    }

    private void createRepositoryConfiguration() {
        repositoryConfiguration = MongoRepositoryConfiguration.create(descriptor);
    }

    @Test
    public void forMongo_method_should_return_an_instance_of_RepositoryConfiguration() {
        assertThat(repositoryConfiguration, instanceOf(RepositoryConfiguration.class));
    }

    @Test
    public void an_instance_of_RepositoryConfiguration_should_has_a_db_name() {
        assertThat(repositoryConfiguration.getDatabaseName(), equalTo(DBNAME));
    }
    
    @Test
    public void an_instance_of_RepositoryConfiguration_should_has_a_host() {
        assertThat(repositoryConfiguration.getRepositoryConnectionDataDescriptor().getHost(), equalTo(HOST));
    }

    @Test
    public void an_instance_of_RepositoryConfiguration_should_has_a_host_port() {
        assertThat(repositoryConfiguration.getRepositoryConnectionDataDescriptor().getPort(), equalTo(PORT));
    }

    @Test
    public void an_instance_of_RepositoryConfiguration_should_has_a_db_user() {
        assertThat(repositoryConfiguration.getUserName(), equalTo(USER));
    }

    @Test
    public void an_instance_of_RepositoryConfiguration_should_has_a_db_type_equal_to_MONGODB() {
        assertThat(repositoryConfiguration.getRepositoryType(), equalTo(RepositoryType.MONGODB));
    }

    @Test
    public void an_instance_of_RepositoryConfiguration_should_has_a_user_password() {
        assertThat(repositoryConfiguration.getPassword(), equalTo(PASSWORD));
    }

    @Ignore
    @Test
    public void getUrl_method_should_return_an_url() {
        assertThat(repositoryConfiguration.getUrl(), equalTo(CONNECTION_URL));
    }

}
