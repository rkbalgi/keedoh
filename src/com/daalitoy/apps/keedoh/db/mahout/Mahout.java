package com.daalitoy.apps.keedoh.db.mahout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.daalitoy.apps.keedoh.system.Properties;

public class Mahout {

	private static final Logger log = Logger.getLogger(Mahout.class);
	private static final String dbConnectionStr = Properties.getProperty(
			"DB_URL", null);
	private static Connection connection = null;

	static {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			log.error(e);
		}
	}

	public static Connection getConnection() throws MahoutException {

		try {
			if (connection != null && !connection.isClosed()) {
				return (connection);
			} else {

				connection = DriverManager.getConnection(dbConnectionStr, "sa",
						"");
				return (connection);

			}
		} catch (SQLException e) {
			throw new MahoutException(e);
		}

	}

	/**
	 * caller should close this connection
	 * 
	 * @return
	 * @throws MahoutException
	 */
	public static Connection getNewConnection() throws MahoutException {
		Connection connection;
		try {
			connection = DriverManager.getConnection(dbConnectionStr, "SA", "");
		} catch (SQLException e) {
			throw new MahoutException(e);
		}
		return (connection);
	}

	public static void main(String[] args) throws MahoutException, SQLException {
		Connection c = getConnection();
		ResultSet rs = c.createStatement().executeQuery(
				"select * from fld_type");
		while (rs.next()) {
			System.out.println(rs.getString("fld_type_name") + ":"
					+ rs.getString("fld_processor_class"));
		}
		c.close();
	}

	public static int getIdentity() throws MahoutException {

		try {
			PreparedStatement st = getConnection().prepareStatement(
					"CALL IDENTITY()");
			ResultSet rs = st.executeQuery();
			rs.next();
			int id = rs.getInt(1);
			rs.close();
			st.close();
			return (id);
		} catch (Exception e) {
			log.error("unable to get identity", e);
			throw new MahoutException(e);
		}

	}
}
