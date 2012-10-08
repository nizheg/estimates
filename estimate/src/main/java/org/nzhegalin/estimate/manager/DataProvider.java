package org.nzhegalin.estimate.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.nzhegalin.estimate.entity.builder.EntityBuilder;

public class DataProvider {

	private static DataProvider instance;
	private DataSource ds;
	private Connection connection;
	private boolean isActiveTransaction;

	private DataProvider() throws SQLException {
		try {
			InitialContext cxt = new InitialContext();
			ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/postgres");
		} catch (NamingException e) {
			throw new SQLException(e);
		}
		if (ds == null) {
			throw new SQLException("Data source not found!");
		}
	}

	public static DataProvider instance() throws SQLException {
		if (instance == null) {
			instance = new DataProvider();
		}
		return instance;
	}

	public void beginTransaction() throws SQLException {
		getConnection().setAutoCommit(false);
		isActiveTransaction = true;
	}

	public void commitTransaction() throws SQLException {
		try {
			getConnection().commit();
		} finally {
			isActiveTransaction = false;
			closeConnection();
		}

	}

	public void rollbackTransaction() throws SQLException {
		try {
			getConnection().rollback();
		} finally {
			isActiveTransaction = false;
			closeConnection();
		}
	}

	public <T> T queryObject(String query, EntityBuilder<T> builder)
			throws SQLException {
		Connection conn = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			conn = getConnection();
			statement = conn.createStatement();
			result = statement.executeQuery(query);
			if (result.next()) {
				return builder.createInstance(result);
			}
			return null;
		} finally {
			closeConnection();
		}
	}

	public <T> Collection<T> queryCollection(String query,
			EntityBuilder<T> builder) throws SQLException {
		Connection conn = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			conn = getConnection();
			statement = conn.createStatement();
			result = statement.executeQuery(query);
			Collection<T> values = new ArrayList<T>();
			while (result.next()) {
				values.add(builder.createInstance(result));
			}
			return values;
		} finally {
			closeConnection();
		}
	}

	public boolean update(String query) throws SQLException {
		Connection conn = null;
		Statement statement = null;
		int result;
		try {
			conn = getConnection();
			statement = conn.createStatement();
			result = statement.executeUpdate(query,
					Statement.RETURN_GENERATED_KEYS);
			return result > 0;
		} finally {
			closeConnection();
		}
	}

	private void closeConnection() throws SQLException {
		if (this.connection != null && !isActiveTransaction) {
			this.connection.close();
			this.connection = null;
		}
	}

	private Connection getConnection() throws SQLException {
		if (this.connection == null) {
			this.connection = ds.getConnection();
		}
		return this.connection;
	}
}
