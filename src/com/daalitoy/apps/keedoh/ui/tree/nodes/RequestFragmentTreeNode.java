package com.daalitoy.apps.keedoh.ui.tree.nodes;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.MessageFragment;
import com.daalitoy.apps.keedoh.ui.util.KeedohConstants;

public class RequestFragmentTreeNode extends KeedohMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MessageFragment fragment;

	public RequestFragmentTreeNode(MessageFragment fragment) {
		this.fragment = fragment;
	}

	public void init() {
		for (Field field : fragment.getFields()) {
			MessageFieldTreeNode node = new MessageFieldTreeNode(fragment,
					field);
			addToSelf(node);
			node.init();
		}
	}

	public MessageFragment getFragment() {
		return (fragment);
	}

	public boolean contains(Field field) {
		if (fragment.contains(field)) {
			return (true);
		} else {
			return (false);
		}
	}

	public String toString() {
		return (KeedohConstants.REQUEST_FRAGMENT);
	}

}
