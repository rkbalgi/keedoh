package com.daalitoy.apps.keedoh.data.providers;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import java.io.IOException;
import java.util.List;

public class ListenerConfigProvider {
    private final List<ListenerConfig> configs = Lists.newArrayList();
    private ObjectMapper mapper = new ObjectMapper();

    public ListenerConfigProvider() {
        try {
            ArrayNode connectors =
                    (ArrayNode) mapper.readTree(Resources.getResource("listener-configs.json"));
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

    private void write() {
    }

    public void update(ListenerConfig config) {
        ListenerConfig lConfig =
                configs.stream().filter(c -> c.getName().equals(config.getName())).findFirst().get();

        configs.set(configs.indexOf(lConfig), config);
        write();
    }
}
