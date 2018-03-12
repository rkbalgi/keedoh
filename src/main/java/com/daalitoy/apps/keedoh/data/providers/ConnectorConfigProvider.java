package com.daalitoy.apps.keedoh.data.providers;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.ui.util.KeedohConstants;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class ConnectorConfigProvider {

    private static final Logger log = LogManager.getLogger(ConnectorConfig.class);
    private final List<ConnectorConfig> configs = Lists.newArrayList();
    private final File configFile;
    private ObjectMapper mapper = new ObjectMapper();

    public ConnectorConfigProvider() {
        try {
            configFile =
                    Paths.get(System.getProperty(KeedohConstants.KEEDOH_CONFIG_DIR), "connector-configs.json")
                            .toFile();
            ArrayNode connectors = (ArrayNode) mapper.readTree(configFile);
            connectors
                    .elements()
                    .forEachRemaining(node -> configs.add(mapper.convertValue(node, ConnectorConfig.class)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ConnectorConfig> allConfigs() {
        return configs;
    }

    public void newConfig(ConnectorConfig config) {
        configs.add(config);
        write();
    }

    public void update(ConnectorConfig config) {
        ConnectorConfig lConfig =
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
