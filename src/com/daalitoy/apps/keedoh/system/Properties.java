package com.daalitoy.apps.keedoh.system;

import java.io.IOException;

import org.apache.log4j.Logger;

public class Properties {

	private static final Logger log = Logger.getLogger(Properties.class);
	private static java.util.Properties props = new java.util.Properties();
	static {
		try {
			props.load(ClassLoader.getSystemResource("keedoh.properties")
					.openStream());
			log.info("keedoh.propeties" + props.toString());
		} catch (IOException e) {
			log.error("error reading properties", e);
		}
	}

	public static String getProperty(String propertyName, String defaultValue) {

		if (props.containsKey(propertyName)) {
			return (props.getProperty(propertyName));
		} else {
			return (defaultValue);
		}
	}

	public static int getIntProperty(String propertyName, int defaultValue) {
		if (props.containsKey(propertyName)) {
			return (Integer.parseInt(props.getProperty(propertyName)));
		} else {
			return (defaultValue);
		}
	}
}
