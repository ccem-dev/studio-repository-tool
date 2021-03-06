package br.org.studio.tool.mongodb.database;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.MongoSocketOpenException;
import com.mongodb.ServerAddress;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MongoConnector.class })
public class MongoConnectorTest {

	private static final String HOST = "127.0.0.1";
	private static final String PORT = "27017";
	private static final String DRIVER = "mongodb";
	private static final String URI = DRIVER + "://" + HOST + ":" + PORT + "/";
	private static final String USER = "user";
	private static final String PASSWORD = "password";
	private static final String DB = "admin";

	@Mock
	public MongoClientURI clientUri;
	@Mock
	private MongoClient client;
	@Mock
	private MongoConnector connector;
	private MongoCredential credential;
	
	
	private MongoCredential mongoCredential;
	private MongoClient mongoClient;

	@Before
	public void setup() throws Exception {
		credential = MongoCredential.createCredential(USER, DB, PASSWORD.toCharArray());
		whenNew(MongoClientURI.class).withArguments(URI).thenReturn(clientUri);
		whenNew(MongoClient.class).withArguments(ServerAddress.class, List.class).thenReturn(client);
	}

	@Test
	public void getConnector_method_should_return_an_instance_with_host_info_equal_to_host_parameter() {
		MongoConnector connector = MongoConnector.getConnector(HOST, PORT);

		assertThat(connector.getHost(), equalTo(HOST));
	}

	@Test
	public void getConnector_method_should_return_an_instance_with_host_port_info_equal_to_port_parameter() {
		MongoConnector connector = MongoConnector.getConnector(HOST, PORT);

		assertThat(connector.getPort(), equalTo(PORT));
	}

	@Test
	public void getUri_method__should_return_an_string_with_connection_uri_info() {
		MongoConnector connector = MongoConnector.getConnector(HOST, PORT);

		assertThat(connector.getUri(), equalTo(URI));
	}

	@Test
	public void getUri_should_an_String_that_contains_the_host_info() {
		MongoConnector connector = MongoConnector.getConnector(HOST, PORT);

		assertThat(connector.getUri(), containsString(HOST));
	}

	@Test
	public void getUri_should_an_String_that_contains_the_port_info() {
		MongoConnector connector = MongoConnector.getConnector(HOST, PORT);

		assertThat(connector.getUri(), containsString(PORT));
	}

	@Test
	public void createClient_method_should_return_an_instance_of_MongoClient_when_db_server_is_accessible() {
		
		mongoCredential = MongoCredential.createCredential("test", "test", "test".toCharArray());
		mongoClient = connector.createClient(mongoCredential);

		assertThat(client, instanceOf(MongoClient.class));
	}

	@Test(expected = MongoSocketOpenException.class)
	@Ignore
	@SuppressWarnings("unchecked")
	public void createClient_method_should_return_null_when_db_server_is_not_accessible() throws Exception {
		whenNew(MongoClient.class).withArguments(clientUri).thenThrow(MongoSocketOpenException.class);
		MongoConnector connector = MongoConnector.getConnector(HOST, PORT);

		assertThat(connector.createClient(credential), nullValue());
	}
}