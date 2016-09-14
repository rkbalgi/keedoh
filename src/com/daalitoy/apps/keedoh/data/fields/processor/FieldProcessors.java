package com.daalitoy.apps.keedoh.data.fields.processor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;

import com.daalitoy.apps.keedoh.data.common.FIELD_TYPE;
import com.daalitoy.apps.keedoh.db.mahout.Mahout;
import com.google.common.collect.Maps;

public class FieldProcessors {

	private static final Logger log = Logger.getLogger(FieldProcessors.class);
	private static Map<FIELD_TYPE, FieldProcessor> fieldProcessorMap = Maps
			.newHashMap();

	static {

		Statement statement = null;
		try {
			Connection connection = Mahout.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM FLD_TYPE");
			while (rs.next()) {
				String sFieldType = rs.getString("FLD_TYPE_NAME").trim();
				String processorClassName = rs.getString("FLD_PROCESSOR_CLASS");
				FIELD_TYPE fieldType = FIELD_TYPE.valueOf(sFieldType);
				if (fieldType != null) {
					fieldProcessorMap.put(fieldType, (FieldProcessor) Class
							.forName(processorClassName).newInstance());
				} else {
					log.warn("unsupported field type:" + sFieldType);
				}
			}
			rs.close();
			statement.close();
		} catch (Exception e) {
			log.error("exception loading field types: ", e);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
				}
			}
		}

	}

	public static FieldProcessor getProcessorClass(FIELD_TYPE fieldType) {
		return (fieldProcessorMap.get(fieldType));
	}
}
