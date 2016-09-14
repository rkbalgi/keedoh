package com.daalitoy.apps.keedoh.messaging;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;

import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.google.common.collect.Maps;

public class MessageHandlerhelper {

	private static Logger log = Logger.getLogger(MessageHandlerhelper.class);
	private static Map<String, MessageHandler> handlerMap = Maps.newHashMap();

	public static void handle(SpecMsg msg, String scriptName) {

		try {
			log.info(String.format("processing groovy script [%s]", scriptName));
			Class c = new GroovyClassLoader().parseClass(new File("scripts/"
					+ scriptName));
			MessageHandler handler = (MessageHandler) c.newInstance();
			handler.handleMsg(msg);
		} catch (Exception e) {
			log.error("error processing message", e);
		}

	}

}
