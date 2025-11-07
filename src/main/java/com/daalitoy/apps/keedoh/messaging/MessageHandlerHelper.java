package com.daalitoy.apps.keedoh.messaging;

import com.daalitoy.apps.keedoh.ui.util.KeedohConstants;
import com.google.common.collect.Maps;
import groovy.lang.GroovyClassLoader;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageHandlerHelper {

  private static final Map<String, MessageHandler> handlerMap = Maps.newHashMap();
  private static Logger log = LogManager.getLogger(MessageHandlerHelper.class);

  public static void handle(SpecMsg msg, String scriptName) {

    try {
      log.info(String.format("processing groovy script [%s]", scriptName));
      Class c =
          new GroovyClassLoader()
              .parseClass(
                  Paths.get(
                          System.getProperty(KeedohConstants.KEEDOH_CONFIG_DIR),
                          "scripts",
                          scriptName)
                      .toFile());
      MessageHandler handler = (MessageHandler) c.newInstance();
      handler.handleMsg(msg);
    } catch (Exception e) {
      log.error("error processing message", e);
    }
  }
}
