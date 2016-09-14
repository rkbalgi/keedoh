package com.daalitoy.apps.keedoh.data.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import com.daalitoy.apps.keedoh.db.mahout.Mahout;
import com.google.common.collect.Lists;

public class Encodings {

	private static Logger log = Logger.getLogger(Encodings.class);
	private static List<ENCODING_TYPE> encodingTypes = null;

	public static List<ENCODING_TYPE> getEncodingTypes() {
		if (encodingTypes != null) {
			return (encodingTypes);
		} else {

			try {
				encodingTypes = Lists.newArrayList();
				Connection c = Mahout.getConnection();
				Statement st = c.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM ENCODING");
				while (rs.next()) {
					String sEncodingType = rs.getString("ENCODING_TYPE").trim();
					ENCODING_TYPE encodingType = ENCODING_TYPE
							.valueOf(sEncodingType);
					if (encodingType != null) {
						encodingTypes.add(encodingType);
					} else {
						log.warn("unsupported encoding type:" + sEncodingType);
					}
				}
				rs.close();
				st.close();
			} catch (Exception e) {
				encodingTypes = null;
				log.error("error processing encodings", e);
			}
		}

		return (encodingTypes);
	}
}
