package utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import oracle.jdbc.pool.OracleDataSource;

/**
 * This class contains methods to read/write data from/to Oracle database
 * 
 * @author Gurman
 *
 */
public class Database {
	private Connection connection;

	public Database(String connectionURL, String username, String passkey) {
		try {
			OracleDataSource ods = new OracleDataSource();
			ods.setURL(connectionURL);
			ods.setUser(username);
			ods.setPassword(passkey);
			connection = ods.getConnection();
		} catch (SQLException exception) {
			connection = null;
			exception.printStackTrace();
		}
	}

	/**
	 * This method is used to close the connection to the Database
	 * 
	 * @return false only if connection could not be closed, else true
	 */
	public boolean close() {
		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.close();
					return true;
				} else
					return true;
			} catch (SQLException exception) {
				exception.printStackTrace();
				return false;
			}
		} else
			return true;
	}

	/**
	 * This method is used to read the data from the Database by using the given DDL
	 * query
	 * 
	 * @param query      - query to be executed to fetch the data
	 * @param parameters - paramters required to construct the query
	 * @return resultset data in form of List of Mappings between column name and
	 *         values
	 */
	public List<Map<String, Object>> read(String query, Object... parameters) {
		List<Map<String, Object>> ddlData = new ArrayList<Map<String, Object>>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			for (int i = 0; i < parameters.length; i++)
				preparedStatement.setObject(i + 1, parameters[i]);
			ResultSet resultSet = preparedStatement.executeQuery();
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			while (resultSet.next()) {
				Map<String, Object> currentRowData = new LinkedHashMap<String, Object>();
				for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++)
					currentRowData.put(resultSetMetaData.getColumnLabel(i), resultSet.getObject(i));
				ddlData.add(currentRowData);
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
		return ddlData;
	}

	/**
	 * This method is used to write data to the database using the given DML query
	 * 
	 * @param query      - query to be executed to write data to the database
	 * @param parameters - parameters required to prepare the SQL statement
	 */
	public void write(String query, Object[] parameters) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			if (parameters != null)
				for (int i = 0; i < parameters.length; i++)
					preparedStatement.setObject(i + 1, parameters[i]);
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * This method is used to write data to the database using the given DML Query.
	 * It prepares the SQL statement multiple times using different parameters
	 * provided
	 * 
	 * @param query          - query to be executef to write the data to the
	 *                       database
	 * @param parametersList - list of parameter sets. If the list's size is 'n',
	 *                       then SQL statement will be prepared 'n' times using the
	 *                       provided parameters in each element of the list
	 */
	public void write(String query, List<Object[]> parametersList) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			if (parametersList != null) {
				for (Object[] parameters : parametersList) {
					if (parameters != null)
						for (int i = 0; i < parameters.length; i++)
							preparedStatement.setObject(i + 1, parameters[i]);
					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();
			} else
				preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * This method is used to write data to database by executing multiple provided
	 * DML statements with the capability of executing each statement with different
	 * sets of parameters provided in form of List of Objects array
	 * 
	 * @param queriesAndParametersList - unique queries and list of paramters to
	 *                                 construct SQL statement for the queries
	 */
	public void write(Map<String, List<Object[]>> queriesAndParametersList) {
		for (Entry<String, List<Object[]>> queryAndParametersList : queriesAndParametersList.entrySet())
			write(queryAndParametersList.getKey(), queryAndParametersList.getValue());
	}

}
