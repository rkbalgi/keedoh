package com.daalitoy.apps.keedoh.ui.main.tree;

import java.util.List;

import com.daalitoy.apps.keedoh.data.model.Message;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;

public class SpecMessagesTreeNode extends KeedohMutableTreeNode
		  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Spec spec;

	public SpecMessagesTreeNode(Spec spec) {
		super("Messages");
		this.spec = spec;
	}

	public void init() {
		List<Message> messages = Message.allMessages(spec);
		for (Message msg : messages) {
			SpecMessageTreeNode msgNode = new SpecMessageTreeNode(msg);
			addToSelf(msgNode);
			msgNode.init();
		}
		setUserObject("Messages (" + messages.size() + ")");
		refresh();

	}
}
