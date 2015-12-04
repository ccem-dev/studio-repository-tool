package br.org.studio.tool.mongodb.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.After;
import org.junit.Test;

import br.org.studio.tool.mongodb.database.MongoClientFactory;

import com.mongodb.MongoClient;

public class MongoClientFactoryTest {

	private MongoClient mongoClient;

	@After
	public void teardown() {
		mongoClient.close();
	}

	@Test
	public void createClient_method_should_return_an_instance_of_MongoClient() {
		mongoClient = MongoClientFactory.createClient();
		assertThat(mongoClient, instanceOf(MongoClient.class));
	}

}