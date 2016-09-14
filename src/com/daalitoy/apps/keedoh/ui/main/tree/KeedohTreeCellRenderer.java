package com.daalitoy.apps.keedoh.ui.main.tree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.daalitoy.apps.keedoh.data.model.BitmappedField;
import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.FixedField;
import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.model.TerminatedField;
import com.daalitoy.apps.keedoh.data.model.VariableField;
import com.daalitoy.apps.keedoh.net.client.Connectors;
import com.daalitoy.apps.keedoh.net.server.Listeners;
import com.daalitoy.apps.keedoh.ui.tree.nodes.FieldTreeNode;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;
import com.daalitoy.apps.keedoh.ui.tree.nodes.MessageFieldTreeNode;
import com.daalitoy.apps.keedoh.ui.tree.nodes.MessageSpecFieldDictTreeNode;
import com.daalitoy.apps.keedoh.ui.tree.nodes.MessageTreeNode;
import com.daalitoy.apps.keedoh.ui.tree.nodes.MessagesTreeNode;
import com.daalitoy.apps.keedoh.ui.tree.nodes.RequestFragmentTreeNode;
import com.daalitoy.apps.keedoh.ui.tree.nodes.ResponseFragmentTreeNode;
import com.daalitoy.apps.keedoh.ui.util.IconFactory;

public class KeedohTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final ImageIcon SPECS_NODE = new ImageIcon(
			ClassLoader.getSystemResource("keedoh_icon.png"));
	private static final ImageIcon MESSAGES_NODE = new ImageIcon(
			ClassLoader.getSystemResource("mails.png"));
	private static final ImageIcon KEEDOH_NODE = new ImageIcon(
			ClassLoader.getSystemResource("keedoh_icon.png"));

	private static final ImageIcon FIXED_FIELD_NODE = new ImageIcon(
			ClassLoader.getSystemResource("fixed_field.gif"));
	private static final ImageIcon VARIABLE_FIELD_NODE = new ImageIcon(
			ClassLoader.getSystemResource("variable_field.gif"));
	private static final ImageIcon TERMINATED_FIELD_NODE = new ImageIcon(
			ClassLoader.getSystemResource("terminated_field.gif"));

	private static final ImageIcon SPEC_NODE = new ImageIcon(
			ClassLoader.getSystemResource("spec_icon.png"));
	private static final Icon REQ_FRAGMENT_NODE = new ImageIcon(
			ClassLoader.getSystemResource("request_fragment.gif"));
	private static final Icon REP_FRAGMENT_NODE = new ImageIcon(
			ClassLoader.getSystemResource("response_fragment.gif"));
	private static final Icon MESSAGE_NODE = new ImageIcon(
			ClassLoader.getSystemResource("mail-medium.png"));
	private static final Icon BITMAPPED_FIELD_NODE = new ImageIcon(
			ClassLoader.getSystemResource("bitmapped_field.gif"));

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object nodeObj,
			boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {

		super.getTreeCellRendererComponent(tree, nodeObj, arg2, arg3, arg4,
				arg5, arg6);

		if (nodeObj instanceof SpecTreeNode) {
			setIcon(IconFactory.getIcon("blue-document-template.png"));
		} else if (nodeObj instanceof MessageSpecFieldDictTreeNode) {
			setIcon(SPEC_NODE);
		} else if (nodeObj instanceof SpecsTreeNode) {
			setIcon(IconFactory.getIcon("blue-documents-stack.png"));
		} else if (nodeObj instanceof MessagesTreeNode) {
			setIcon(IconFactory.getIcon("mails.png"));
		} else if (nodeObj instanceof SpecMessagesTreeNode) {
			setIcon(IconFactory.getIcon("mails.png"));
		} else if (nodeObj instanceof FieldTreeNode) {
			Field field = ((FieldTreeNode) nodeObj).getField();
			if (field instanceof FixedField) {
				setIcon(FIXED_FIELD_NODE);
			} else if (field instanceof VariableField) {
				setIcon(VARIABLE_FIELD_NODE);
			} else if (field instanceof TerminatedField) {
				setIcon(TERMINATED_FIELD_NODE);
			} else if (field instanceof BitmappedField) {
				setIcon(BITMAPPED_FIELD_NODE);
			}
		} else if (nodeObj instanceof MessageFieldTreeNode) {
			Field field = ((MessageFieldTreeNode) nodeObj).getField();
			if (field instanceof FixedField) {
				setIcon(FIXED_FIELD_NODE);
			} else if (field instanceof VariableField) {
				setIcon(VARIABLE_FIELD_NODE);
			} else if (field instanceof TerminatedField) {
				setIcon(TERMINATED_FIELD_NODE);
			} else if (field instanceof BitmappedField) {
				setIcon(BITMAPPED_FIELD_NODE);
			}
		} else if (nodeObj instanceof RequestFragmentTreeNode) {
			setIcon(IconFactory.getIcon("request_fragment_icon.png"));
		} else if (nodeObj instanceof ResponseFragmentTreeNode) {
			setIcon(IconFactory.getIcon("response_fragment_icon.png"));
		} else if (nodeObj instanceof MessageTreeNode) {
			setIcon(MESSAGE_NODE);
		} else if (nodeObj instanceof SpecMessageTreeNode) {
			setIcon(MESSAGE_NODE);
		} else if (nodeObj instanceof ListenerTreeNode) {
			if (Listeners.isOpen((ListenerConfig) ((ListenerTreeNode) nodeObj)
					.getUserObject())) {

				setIcon(IconFactory.getIcon("network-status-busy.png"));
			} else {
				setIcon(IconFactory.getIcon("network-status-offline.png"));
			}

		} else if (nodeObj instanceof ConnectorTreeNode) {
			if (Connectors
					.isConnected((ConnectorConfig) ((ConnectorTreeNode) nodeObj)
							.getUserObject())) {

				setIcon(IconFactory.getIcon("network-status-busy.png"));
			} else {
				setIcon(IconFactory.getIcon("network-status-offline.png"));
			}

		} else if (nodeObj instanceof ListenersConfigTreeNode) {
			setIcon(IconFactory.getIcon("listener_conf_icon.png"));

		} else if (nodeObj instanceof ConnectorsConfigTreeNode) {
			setIcon(IconFactory.getIcon("connector_conf_icon.png"));

		} else {
			if (((KeedohMutableTreeNode) nodeObj).getUserObject().equals(
					"Keedoh")) {
				setIcon(IconFactory.getIcon("home.png"));
			} else {
				setIcon(KEEDOH_NODE);
			}
		}

		return (this);

	}

}
