package com.daalitoy.apps.keedoh.ui.main.tree;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.providers.ListenerConfigProvider;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;

import java.util.List;

public class ListenersConfigTreeNode extends KeedohMutableTreeNode {

    /** */
    private static final long serialVersionUID = 1L;

    private final ListenerConfigProvider provider;

    public ListenersConfigTreeNode(ListenerConfigProvider provider) {
        super("Listeners");
        this.provider = provider;
        // init();
    }

    public void init() {
        List<ListenerConfig> configs = provider.allConfigs();
        for (ListenerConfig config : configs) {
            ListenerTreeNode childNode = new ListenerTreeNode(config, provider);
            add(childNode);
        }
    }
}
