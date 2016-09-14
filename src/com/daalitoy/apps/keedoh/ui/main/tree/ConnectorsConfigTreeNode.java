package com.daalitoy.apps.keedoh.ui.main.tree;

import java.util.List;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;

public class ConnectorsConfigTreeNode extends KeedohMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectorsConfigTreeNode() {
		// TODO Auto-generated constructor stub
	}

	public ConnectorsConfigTreeNode(Object userObject) {
		super(userObject);
		// TODO Auto-generated constructor stub
	}
	
	public void init() {
		List<ConnectorConfig> configs = ConnectorConfig.getInstances();
		for (ConnectorConfig config : configs) {
			ConnectorTreeNode childNode = new ConnectorTreeNode(config);
			addToSelf(childNode);
		}
	}

	public ConnectorsConfigTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		// TODO Auto-generated constructor stub
	}

}
