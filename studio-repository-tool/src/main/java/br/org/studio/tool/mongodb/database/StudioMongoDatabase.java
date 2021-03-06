package br.org.studio.tool.mongodb.database;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.org.studio.tool.base.database.MetaDatabase;
import br.org.studio.tool.base.repository.configuration.RepositoryConfiguration;

public class StudioMongoDatabase extends MetaDatabase {

    public static final String PROTOCOL = "mongodb://";
    private MongoClient client;
    private MongoConnector connector;
    private MongoDatabase database;
    private RepositoryConfiguration configuration;

    public StudioMongoDatabase(RepositoryConfiguration configuration) {
        super(configuration);
        this.configuration = configuration;
        connector = MongoConnector.getConnector(getHost(), getPort());
    }

    @Override
    public Boolean hasError() {
        return false;
    }

    @Override
    public String getDriver() {
        return "mongodb";
    }

    public Boolean isAccessible() {
        return connector.testConnection();
    }

    public void create() {
        load();
        createMetaInformation();
        createAdminUser();
    }

    public void load() {
        client = connector.createClient(createCredential());
        database = client.getDatabase(configuration.getDatabaseName());
    }

	private MongoCredential createCredential() {
		return MongoCredential.createCredential(getConfiguration().getRepositoryConnectionDataDescriptor().getUsername()
				, getConfiguration().getRepositoryConnectionDataDescriptor().getDatabase()
				, getConfiguration().getRepositoryConnectionDataDescriptor().getPassword().toCharArray());
	}

    public void drop() {
        if (client == null)
            load();
        client.dropDatabase(configuration.getDatabaseName());
    }

    public void close() {
        if (client != null)
            client.close();
    }

    public MongoDatabase getConnection() {
        return database;
    }

    private void createMetaInformation() {
        MongoCollection<Document> info = database.getCollection(MetaInformation.COLLECTION.getValue());

        Document document = new Document();
        document.append(MetaInformation.DBNAME.getValue(), getName());
        document.append(MetaInformation.HOST.getValue(), getHost());
        document.append(MetaInformation.PORT.getValue(), getPort());
        document.append(MetaInformation.CREATION_DATE.getValue(), new Date());

        info.insertOne(document);
        
    }

    private void createAdminUser() {
        Map<String, Object> commandArguments = new BasicDBObject();
        commandArguments.put("createUser", configuration.getUserName());
        commandArguments.put("pwd", configuration.getPassword());
        String[] roles = { "dbOwner" };
        commandArguments.put("roles", roles);
        BasicDBObject command = new BasicDBObject(commandArguments);
        database.runCommand(command);
    }

    @SuppressWarnings("deprecation")
	public List<String> getDatabaseNames() {
        return client.getDatabaseNames();
    }

}
