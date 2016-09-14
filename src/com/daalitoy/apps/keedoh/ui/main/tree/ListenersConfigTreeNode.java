package com.daalitoy.apps.keedoh.ui.main.tree;

import java.util.List;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;

public class ListenersConfigTreeNode extends KeedohMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ListenersConfigTreeNode() {
		// TODO Auto-generated constructor stub
	}

	public ListenersConfigTreeNode(Object userObject) {
		super(userObject);
		// TODO Auto-generated constructor stub
	}

	public void init() {
		List<ListenerConfig> configs = ListenerConfig.getInstances();
		for (ListenerConfig config : configs) {
			ListenerTreeNode childNode = new ListenerTreeNode(config);
			addToSelf(childNode);
		}
	}

	public ListenersConfigTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		// TODO Auto-generated constructor stub
	}

}
