package com.daalitoy.apps.keedoh.ui.tree.nodes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JPopupMenu;

import com.daalitoy.apps.keedoh.data.model.Message;
import com.daalitoy.apps.keedoh.data.model.MessageFragment;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.ui.CanPopUp;
import com.daalitoy.apps.keedoh.ui.dialog.SimpleTextInputDialog;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import com.daalitoy.apps.keedoh.ui.util.KeedohConstants;

public class MessagesTreeNode extends KeedohMutableTreeNode implements
		CanPopUp, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu pMenu = new JPopupMenu();
	private Spec spec;

	public MessagesTreeNode(Spec spec) {
		super("Messages");
		this.spec = spec;
		setupPopupMenu();
	}

	public void init() {
		List<Message> messages = Message.allMessages(spec);
		for (Message msg : messages) {
			MessageTreeNode msgNode = new MessageTreeNode(msg);
			addToSelf(msgNode);
			msgNode.init();
		}
		setUserObject("Messages (" + messages.size() + ")");
		refresh();

	}

	private void setupPopupMenu() {
		pMenu.setFont(UIHelper.STANDARD_FONT);
		pMenu.add(UIHelper.newJMenuItem("New Message", KeyEvent.VK_N,
				this, KeedohConstants.NEW_MESSAGE));

	}

	@Override
	public JPopupMenu getPopUp() {
		return (pMenu);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(KeedohConstants.NEW_MESSAGE)) {
			SimpleTextInputDialog dialog = new SimpleTextInputDialog(
					getOwningTree(), "New Message");
			String msgName = dialog.showDialog("Message Name");
			if (msgName != null) {
				Message msg = new Message(spec, msgName);
				msg.setRequestFragment(new MessageFragment());
				msg.setResponseFragment(new MessageFragment());
				MessageTreeNode childNode = new MessageTreeNode(msg);
				addToSelf(childNode);
				childNode.init();

			}
		}

	}

}
