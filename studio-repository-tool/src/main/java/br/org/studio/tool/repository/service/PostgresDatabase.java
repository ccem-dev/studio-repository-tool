package br.org.studio.tool.repository.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import br.org.studio.tool.repository.RepositoryConfiguration;

public class PostgresDatabase implements Database {

	public static final String DRIVER = "org.postgresql.Driver";
	public static final String DIALECT = "org.hibernate.dialect.PostgreSQLDialect";
	private static final String JDBC_POSTGRES = "jdbc:postgresql://";

	private String name;
	private String host;
	private String port;
	private String user;	 // esse usuário deve ser fornecido pelo administrador do banco de dados
	private String password; // essa senha deve ser fornecida pelo administrador do banco de dados

	private SQLWarning warnings;
	private Connection connection;

	// TODO: retirar usuário e senha hardcode
	public PostgresDatabase(RepositoryConfiguration configuration) {
		this.name = configuration.getName();
		this.host = configuration.getHost();
		this.port = configuration.getPort();
		this.password = "postgres"; 
		this.user = "postgres";
	}

	@Override
	public Boolean hasError() {
		return (warnings == null) ? false : true;
	}

	@Override
	public Connection getConnection() throws Exception {
		Class.forName(DRIVER);
		connection = DriverManager.getConnection(getUrl(), getUser(), getPassword());

		if (connection.isClosed()) {
			throw new Exception();
		}

		return connection;
	}

	private String getUrlConnection() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(JDBC_POSTGRES);
		stringBuilder.append(getHost());
		stringBuilder.append(":");
		stringBuilder.append(getPort());
		stringBuilder.append("/postgres");
		return stringBuilder.toString();
	}

	public void createDatabase() throws SQLException {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("CREATE DATABASE ");
		stringBuilder.append(name);
		stringBuilder.append(" WITH OWNER = postgres");
		stringBuilder.append(" ENCODING = 'UTF8' ");
		stringBuilder.append(" TABLESPACE = pg_default ");
		stringBuilder.append(" LC_COLLATE = 'pt_BR.UTF-8' ");
		stringBuilder.append(" LC_CTYPE = 'pt_BR.UTF-8' ");
		stringBuilder.append(" CONNECTION LIMIT = -1;");
		String sql = stringBuilder.toString();
		executeStatement(sql);
	}

	public void dropDatabase() throws SQLException {
		executeStatement("DROP DATABASE " + name);
	}

	private void executeStatement(String sql) throws SQLException {
		Statement statement = null;

		try {
			statement = getConnection().createStatement();
			statement.execute(sql);
		} catch (Exception exception) {
			warnings = statement.getWarnings();
			connection.close();
		} finally {
			if (statement != null) {
				statement.close();
				connection.close();
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getDriver() {
		return DRIVER;
	}

	@Override
	public String getDialect() {
		return DIALECT;
	}

	@Override
	public String getUrl() {
		return getUrlConnection();
	}

}
