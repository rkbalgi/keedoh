package com.daalitoy.apps.keedoh.messaging;

import com.google.common.collect.Maps;
import groovy.lang.GroovyClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;

public class MessageHandlerHelper {

    private static final Map<String, MessageHandler> handlerMap = Maps.newHashMap();
    private static Logger log = LogManager.getLogger(MessageHandlerHelper.class);

    public static void handle(SpecMsg msg, String scriptName) {

        try {
            log.info(String.format("processing groovy script [%s]", scriptName));
            Class c = new GroovyClassLoader().parseClass(new File("scripts/" + scriptName));
            MessageHandler handler = (MessageHandler) c.newInstance();
            handler.handleMsg(msg);
        } catch (Exception e) {
            log.error("error processing message", e);
        }
    }
}
