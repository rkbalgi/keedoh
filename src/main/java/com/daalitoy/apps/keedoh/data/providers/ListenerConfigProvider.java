package com.daalitoy.apps.keedoh.data.providers;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.ui.util.KeedohConstants;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ListenerConfigProvider {
  private static final Logger log = LogManager.getLogger(ListenerConfig.class);
  private final List<ListenerConfig> configs = Lists.newArrayList();
  private final File configFile;
  private ObjectMapper mapper = new ObjectMapper();

  public ListenerConfigProvider() {
    try {
      configFile =
          Paths.get(System.getProperty(KeedohConstants.KEEDOH_CONFIG_DIR), "listener-configs.json")
              .toFile();
      ArrayNode connectors = (ArrayNode) mapper.readTree(configFile);
      connectors
          .elements()
          .forEachRemaining(node -> configs.add(mapper.convertValue(node, ListenerConfig.class)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<ListenerConfig> allConfigs() {
    return configs;
  }

  public void newConfig(ListenerConfig config) {
    configs.add(config);
    write();
  }

  public void update(ListenerConfig config) {
    ListenerConfig lConfig =
        configs.stream().filter(c -> c.getName().equals(config.getName())).findFirst().get();

    configs.set(configs.indexOf(lConfig), config);
    write();
  }

  private void write() {
    try {
      mapper.writer(new DefaultPrettyPrinter()).writeValue(configFile, configs);
    } catch (IOException e) {
      log.error("Unable to save connectors config", e);
    }
  }
}
