package com.daalitoy.apps.keedoh.ui.tree.nodes;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.daalitoy.apps.keedoh.data.model.Field;

public class KeedohMutableTreeNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final Logger log = Logger.getLogger(getClass());
	private JTree owningTree;

	public KeedohMutableTreeNode() {
		// TODO Auto-generated constructor stub
	}

	public KeedohMutableTreeNode(Object userObject) {
		super(userObject);
		// TODO Auto-generated constructor stub
	}

	public KeedohMutableTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		// TODO Auto-generated constructor stub
	}

	public JTree getOwningTree() {
		return owningTree;
	}

	public void setOwningTree(JTree owningTree) {
		this.owningTree = owningTree;
	}

	public void init() {

	}

	public boolean contains() {
		return (false);
	}

	public void addToSelf(KeedohMutableTreeNode childNode) {
		DefaultTreeModel model = (DefaultTreeModel) getOwningTree().getModel();
		childNode.setOwningTree(getOwningTree());
		model.insertNodeInto(childNode, this, this.getChildCount());
		getOwningTree().scrollPathToVisible(new TreePath(childNode.getPath()));

	}

	public void removeSelf() {
		DefaultTreeModel model = (DefaultTreeModel) getOwningTree().getModel();
		model.removeNodeFromParent(this);
	}

	public void refresh() {
		DefaultTreeModel model = (DefaultTreeModel) getOwningTree().getModel();
		model.reload(this);
	}

	public void reload() {
		DefaultTreeModel model = (DefaultTreeModel) getOwningTree().getModel();
		model.reload();
	}

	public void valueChanged() {
	}

	public void collapse() {
		getOwningTree().scrollPathToVisible(new TreePath(this));
	}

	public void removeNode(KeedohMutableTreeNode node) {
		DefaultTreeModel model = (DefaultTreeModel) getOwningTree().getModel();
		model.removeNodeFromParent(node);

	}

	protected void addNodeAt(MessageFieldTreeNode node,
			KeedohMutableTreeNode parentNode, int location) {
		DefaultTreeModel model = (DefaultTreeModel) getOwningTree().getModel();
		model.insertNodeInto(node, parentNode, location);
		getOwningTree().scrollPathToVisible(new TreePath(node.getPath()));

	}

	protected void showMe() {
		getOwningTree().scrollPathToVisible(new TreePath(this.getPath()));

	}

	public void changeParent(Field f, Field fParent, Field cParent) {
		FieldTreeNode node1 = null, node2 = null;
		FieldTreeNode node = getNodeForField(f);
		DefaultTreeModel model = (DefaultTreeModel) getOwningTree().getModel();
		if (fParent != null) {
			// get the node that represents this field
			node1 = getNodeForField(fParent);
		}
		if (cParent != null) {
			// get the node that represents this field
			node2 = getNodeForField(cParent);
		}

		if (node1 == null) {
			// implies that the field was directly below
			// the rootNode
			((MessageSpecFieldDictTreeNode) model.getRoot()).remove(node);
		} else {
			node1.remove(node);
		}
		if (node2 != null) {
			node2.add(node);
		} else {
			((MessageSpecFieldDictTreeNode) model.getRoot()).remove(node);
		}

	}

	@SuppressWarnings("unchecked")
	private FieldTreeNode getNodeForField(Field field) {
		FieldTreeNode node = null;
		DefaultTreeModel model = (DefaultTreeModel) getOwningTree().getModel();
		MessageSpecFieldDictTreeNode rootNode = (MessageSpecFieldDictTreeNode) model
				.getRoot();
		Enumeration<KeedohMutableTreeNode> enumeration = rootNode
				.breadthFirstEnumeration();
		while (enumeration.hasMoreElements()) {
			KeedohMutableTreeNode tmpNode = enumeration.nextElement();
			if (tmpNode instanceof FieldTreeNode) {
				if (((FieldTreeNode) tmpNode).getField() == field) {
					node = (FieldTreeNode) tmpNode;
					return (node);
				}
			}
		}

		return null;
	}

	public void structureChanged() {
		DefaultTreeModel model = (DefaultTreeModel) getOwningTree().getModel();
		model.reload();
		getOwningTree().scrollPathToVisible(new TreePath(this.getPath()));

	}
}
