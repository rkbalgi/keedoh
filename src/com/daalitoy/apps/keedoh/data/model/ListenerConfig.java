package com.daalitoy.apps.keedoh.data.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.daalitoy.apps.keedoh.db.mahout.Mahout;
import com.google.common.collect.Lists;

public class ListenerConfig extends Model {

	private String name;
	private String ip;
	private int port;
	private String processorScript;
	private MLI mli;
	private static List<ListenerConfig> instances = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProcessorScript() {
		return processorScript;
	}

	public void setProcessorScript(String processorScript) {
		this.processorScript = processorScript;
	}

	public MLI getMli() {
		return mli;
	}

	public void setMli(MLI mli) {
		this.mli = mli;
	}

	public String toString() {
		return (this.name);
	}

	/*
	 * CREATE TABLE LST_CONFIG( LST_CONF_NAME CHAR(50) NOT NULL, LST_CONF_PORT
	 * INTEGER NOT NULL, LST_CONF_IP VARCHAR(100) NOT NULL, LST_CONF_SCRIPT
	 * VARCHAR(200) NOT NULL, LST_CONF_MLI CHAR(10) NOT NULL, CONSTRAINT
	 * PK_LST_CONFIG PRIMARY KEY (LST_CONF_NAME), CONSTRAINT FK_LST_CONFIG_MLI
	 * FOREIGN KEY ( LST_CONF_MLI ) REFERENCES MLI( MLI_NAME ) ON DELETE NO
	 * ACTION ON UPDATE NO ACTION );
	 */

	public static List<ListenerConfig> getInstances() {
		if (instances != null) {
			return (instances);
		}
		try {
			PreparedStatement st = Mahout.getConnection().prepareStatement(
					"select * from LST_CONFIG");
			ResultSet rs = st.executeQuery();
			instances = Lists.newArrayList();
			while (rs.next()) {
				ListenerConfig config = new ListenerConfig();
				config.setName(rs.getString("LST_CONF_NAME"));
				config.setIp(rs.getString("LST_CONF_IP"));
				config.setPort(rs.getInt("LST_CONF_PORT"));
				config.setProcessorScript(rs.getString("LST_CONF_SCRIPT"));
				config.setMli(MLI
						.getByName(rs.getString("LST_CONF_MLI").trim()));
				instances.add(config);
			}

			return (instances);
		} catch (Exception e) {
			log.error("unable to load MLI", e);
			throw new KeedohModelException(e);
		}

	}

	public static void newConfig(ListenerConfig config) {
		try {
			PreparedStatement st = Mahout.getConnection().prepareStatement(
					"insert into LST_CONFIG values(?,?,?,?,?)");
			st.setString(1, config.getName());
			st.setInt(2, config.getPort());
			st.setString(3, config.getIp());

			st.setString(4, config.getProcessorScript());
			st.setString(5, config.getMli().getName());

			st.executeUpdate();
		} catch (Exception e) {
			log.error("unable to create ListenerConfig", e);
			throw new KeedohModelException(e);
		}

	}

	public static void update(ListenerConfig config) {
		try {
			PreparedStatement st = Mahout
					.getConnection()
					.prepareStatement(
							"update LST_CONFIG set LST_CONF_IP=?,LST_CONF_PORT=?,LST_CONF_SCRIPT=?,LST_CONF_MLI=? where LST_CONF_NAME=?");

			st.setString(1, config.getIp());
			st.setInt(2, config.getPort());
			st.setString(3, config.getProcessorScript());
			st.setString(4, config.getMli().getName());

			st.setString(5, config.getName());

			st.executeUpdate();
		} catch (Exception e) {
			log.error("unable to update ListenerConfig", e);
			throw new KeedohModelException(e);
		}

	}
}
