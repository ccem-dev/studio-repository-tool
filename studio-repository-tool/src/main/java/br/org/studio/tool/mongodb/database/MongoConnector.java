package br.org.studio.tool.mongodb.database;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoConnector {

    private MongoDatabaseUrl databaseUrl;

    private MongoConnector(String host, String port) {
        databaseUrl = new MongoDatabaseUrl();
        databaseUrl.setHost(host);
        databaseUrl.setPort(port);
    }

    public static MongoConnector getConnector(String host, String port) {
        return new MongoConnector(host, port);
    }

    public String getHost() {
        return databaseUrl.getHost();
    }

    public String getPort() {
        return databaseUrl.getPort();
    }
    
    public String getUri() {
    	return databaseUrl.getUrl();
    }
    
    public MongoClient createClient(MongoCredential credential) {
        return new MongoClient(createServerAddress(), Arrays.asList(credential));
    }

	public ServerAddress createServerAddress() {
		return new ServerAddress(getHost(), Integer.parseInt(getPort()));
	}

    public Boolean testConnection() {
        try {
            Socket socket = new Socket(databaseUrl.getHost(), Integer.valueOf(databaseUrl.getPort()));
            socket.close();

            return Boolean.TRUE;

        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }
}
