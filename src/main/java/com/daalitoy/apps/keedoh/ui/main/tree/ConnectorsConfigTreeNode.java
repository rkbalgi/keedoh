package com.daalitoy.apps.keedoh.ui.main.tree;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.data.providers.ConnectorConfigProvider;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;

import java.util.List;

public class ConnectorsConfigTreeNode extends KeedohMutableTreeNode {

    /** */
    private static final long serialVersionUID = 1L;

    private final ConnectorConfigProvider provider;

    public ConnectorsConfigTreeNode(ConnectorConfigProvider provider) {
        super("Connectors");
        this.provider = provider;

        //init();
    }

    public void init() {
        List<ConnectorConfig> configs = provider.allConfigs();
        for (ConnectorConfig config : configs) {
            ConnectorTreeNode childNode = new ConnectorTreeNode(config, provider);
            add(childNode);
        }
    }
}
