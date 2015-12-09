package br.org.studio.tool.mongodb.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.org.studio.tool.base.repository.configuration.RepositoryConfiguration;
import br.org.studio.tool.mongodb.repository.MongoRepositoryConfiguration;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MongoConnector.class })
public class StudioMongoDatabaseTest {

    private static final String INFO_COLLECTION_NAME = "database_info";
    private static final String MONGO_PROTOCOL = "mongodb://";
    private static final String HOST = "localhost";
    private static final String PORT = "27017";
    private static final String NAME = "repository_name";
    private static final String CONNECTION_URL = MONGO_PROTOCOL + HOST + ":" + PORT + "/" + NAME;

    private StudioMongoDatabase studioDatabase;
    private RepositoryConfiguration configuration;

    @Mock
    private MongoClient client;
    @Mock
    private MongoConnector connector;
    @Mock
    private MongoDatabase database;
    @Mock
    private MongoCollection<Document> collection;
    @Mock
    private FindIterable<Document> iterable;
    @Mock
    private Document document;
    @Mock
    private FindIterable<Document> info;

    @Before
    public void setup() {
        mockStatic(MongoConnector.class);
        when(MongoConnector.getConnector(HOST, PORT)).thenReturn(connector);
        when(connector.createClient()).thenReturn(client);
        when(client.getDatabase(NAME)).thenReturn(database);
        when(database.getCollection(MetaInformation.COLLECTION.getValue())).thenReturn(collection);

        configuration = createRepositoryConfiguration();
        studioDatabase = new StudioMongoDatabase(configuration);

        /* Mocked database behaviours */
        when(database.getCollection(MetaInformation.COLLECTION.getValue()).find()).thenReturn(iterable);
        when(iterable.limit(1)).thenReturn(info);
    }

    private RepositoryConfiguration createRepositoryConfiguration() {
        return MongoRepositoryConfiguration.create(NAME, HOST, PORT, NAME, NAME);
    }

    @Test
    public void an_instance_of_StudioMongoDatabase_should_has_a_host() {
        assertThat(studioDatabase.getHost(), equalTo(HOST));
    }

    @Test
    public void if_host_is_not_specified_on_constructor_then_host_should_be_equal_to_LOCALHOST() {
        assertThat(studioDatabase.getHost(), equalTo(HOST));
    }

    @Test
    public void an_instance_of_StudioMongoDatabase_should_has_a_host_port() {
        assertThat(studioDatabase.getPort(), equalTo(PORT));
    }

    @Test
    public void if_host_port_is_not_specified_on_constructor_then_host_port_should_be_equal_to_27017() {
        assertThat(studioDatabase.getPort(), equalTo(PORT));
    }

    @Test
    public void an_instance_of_StudioMongoDatabase_should_has_an_name() {
        assertThat(studioDatabase.getName(), equalTo(NAME));
    }

    @Test
    public void an_instance_of_StudioMongoDatabase_should_has_an_user() {
        assertThat(studioDatabase.getUser(), equalTo(NAME));
    }

    @Test
    public void getUrl_method_should_return_an_url_connection() {
        assertThat(studioDatabase.getUrl(), equalTo(CONNECTION_URL));
    }

    @Test
    public void getConnection_method_should_return_an_instance_of_MongoDatabase_when_database_exist() throws Exception {
        studioDatabase.load();

        MongoDatabase mongo = studioDatabase.getConnection();

        assertThat(mongo, instanceOf(MongoDatabase.class));
    }

    @Test
    public void a_new_MongoDatabase_instance_should_call_getDatabase_method_from_MongoClient() {
        studioDatabase.load();

        verify(client).getDatabase(NAME);
    }

    @Test
    public void create_method__should_call_getCollection_method_from_MongoDatabase() {
        studioDatabase.create();

        verify(database, Mockito.times(2)).getCollection(INFO_COLLECTION_NAME);
    }

    @Test
    public void load_method_should_call_createClient_method_from_MongoClientFactory() {
        studioDatabase.load();

        verify(connector).createClient();
    }

    @Test
    public void close_method_should_call_close_method_from_MongoClient() {
        studioDatabase.load();

        studioDatabase.close();

        verify(client).close();
    }

    @Test
    public void drop_method_should_call_dropDatabase_method_from_MongoClientFactory() {
        studioDatabase.drop();

        verify(client).dropDatabase(NAME);
    }

    @Test
    public void isAccessible_method_should_return_true_when_db_server_is_accessible() {
        when(connector.testConnection()).thenReturn(true);
        
        assertThat(studioDatabase.isAccessible(), equalTo(true));
    }
    
    @Test
    public void isAccessible_method_should_return_false_when_db_server_is_not_accessible() {
        when(connector.testConnection()).thenReturn(false);
        
        assertThat(studioDatabase.isAccessible(), equalTo(false));
    }

}