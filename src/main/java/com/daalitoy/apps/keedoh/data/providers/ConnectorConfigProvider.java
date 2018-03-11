package com.daalitoy.apps.keedoh.data.providers;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import java.io.IOException;
import java.util.List;

public class ConnectorConfigProvider {

    private final List<ConnectorConfig> configs = Lists.newArrayList();
    private ObjectMapper mapper = new ObjectMapper();

    public ConnectorConfigProvider() {
        try {
            ArrayNode connectors =
                    (ArrayNode) mapper.readTree(Resources.getResource("connector-configs.json"));
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
    }
}
