package com.daalitoy.apps.keedoh.ui.tree.nodes;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.MessageSegment;
import com.daalitoy.apps.keedoh.ui.CanPopUp;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class MessageFieldTreeNode extends KeedohMutableTreeNode
    implements CanPopUp, ActionListener {

  private static final long serialVersionUID = 1L;
  private Field field;

  private JPopupMenu popupMenu = new JPopupMenu();
  private MessageSegment fragment;

  public MessageFieldTreeNode(MessageSegment fragment, Field field) {
    super(field);
    this.field = field;
    this.fragment = fragment;
    setupPopupMenu();
  }

  public void init() {
    // add children
    List<Field> children = field.getChildren();
    for (Field f : children) {
      addToSelf(new MessageFieldTreeNode(fragment, f));
    }
  }

  private void setupPopupMenu() {
    popupMenu.add(UIHelper.newJMenuItem("Move Up", KeyEvent.VK_U, this, "__up__"));
    popupMenu.add(UIHelper.newJMenuItem("Move Down", KeyEvent.VK_D, this, "__down__"));
    popupMenu.add(UIHelper.newJMenuItem("Remove", KeyEvent.VK_R, this, "__remove__"));
  }

  public Field getField() {
    return field;
  }

  @Override
  public JPopupMenu getPopUp() {
    popupMenu.getComponent(0).setEnabled(true);
    popupMenu.getComponent(1).setEnabled(true);

    if (getParent().getIndex(this) == 0) {
      popupMenu.getComponent(0).setEnabled(false);
    } else if (getParent().getIndex(this) == (getParent().getChildCount() - 1)) {
      popupMenu.getComponent(1).setEnabled(false);
    }
    return (popupMenu);
  }

  public boolean contains() {
    return (false);
  }

  public void valueChanged() {
    DefaultTreeModel model = (DefaultTreeModel) getOwningTree().getModel();
    model.valueForPathChanged(new TreePath(this), field);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (arg0.getActionCommand().equals("__up__")) {

      int thisNode = getParent().getIndex(this);
      // remove the above node
      MessageFieldTreeNode prevNode = (MessageFieldTreeNode) getParent().getChildAt(thisNode - 1);
      removeNode(prevNode);

      // add the above node after this node
      addNodeAt(prevNode, (KeedohMutableTreeNode) getParent(), thisNode);
      ((KeedohMutableTreeNode) getParent()).refresh();
      fragment.swapFields(thisNode, thisNode - 1);

    } else if (arg0.getActionCommand().equals("__down__")) {
      int thisNode = getParent().getIndex(this);
      // remove the below node
      MessageFieldTreeNode nextNode = (MessageFieldTreeNode) getParent().getChildAt(thisNode + 1);
      removeNode(nextNode);

      // add the above node after this node
      addNodeAt(nextNode, (KeedohMutableTreeNode) getParent(), thisNode);
      ((KeedohMutableTreeNode) getParent()).refresh();
      fragment.swapFields(thisNode, thisNode + 1);

    } else if (arg0.getActionCommand().equals("__remove__")) {
      removeSelf();
      fragment.remove(field);
    }
  }
}
