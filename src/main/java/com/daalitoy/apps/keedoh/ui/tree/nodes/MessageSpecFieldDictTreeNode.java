package com.daalitoy.apps.keedoh.ui.tree.nodes;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.ui.CanPopUp;
import com.daalitoy.apps.keedoh.ui.dialog.FieldDialog;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.tree.TreeNode;

public class MessageSpecFieldDictTreeNode extends KeedohMutableTreeNode
    implements CanPopUp, ActionListener {

  /** */
  private static final long serialVersionUID = 1L;

  private JPopupMenu popupMenu = new JPopupMenu();
  private Spec spec;
  private JComponent container;

  public MessageSpecFieldDictTreeNode(JComponent container, Spec spec) {
    this.container = container;
    this.spec = spec;
    setupPopupMenu();
  }

  public void init() {

    for (Field field : spec.getFields()) {
      FieldTreeNode fieldNode = new FieldTreeNode(field);
      addToSelf(fieldNode);
      // init
      fieldNode.init();
    }
  }

  private void setupPopupMenu() {
    popupMenu.add(UIHelper.newJMenuItem("Add Field", KeyEvent.VK_F, this, "__new_field__"));
  }

  public String toString() {
    return (spec.getSpecName());
  }

  @Override
  public JPopupMenu getPopUp() {
    return popupMenu;
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (arg0.getActionCommand().equals("__new_field__")) {

      FieldDialog dialog = new FieldDialog(spec, container);
      Field field = null;
      if (dialog.showDialog() == 0) {
        field = dialog.getField();
      }
      if (field != null) {
        field.setSpec(spec);
        // if there is a parent, you should add this to the parent
        if (field.getParent() != null) {
          Enumeration<TreeNode> e = children();
          while (e.hasMoreElements()) {
            FieldTreeNode node = (FieldTreeNode) e.nextElement();
            if (field.getParent() == node.getField()) {
              // we have a match
              node.addToSelf(new FieldTreeNode(field));
            }
          }
        } else {
          addToSelf(new FieldTreeNode(field));
        }

        try {
          // Field.newField(field);
          spec.add(field);
        } catch (Exception e) {
          log.error("failed to add field", e);
          JOptionPane.showMessageDialog(container, "failed to add field");
        }
      }
    } /*
       * else if (arg0.getActionCommand().equals("__set_name__")) { String
       * response = new SimpleTextInputDialog(getOwningTree(),
       * "Set Specification Name").showDialog("Name"); if (response != null) {
       * specName = response; } }
       */
  }
}
