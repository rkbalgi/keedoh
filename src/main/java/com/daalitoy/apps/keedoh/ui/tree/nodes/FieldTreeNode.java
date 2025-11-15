package com.daalitoy.apps.keedoh.ui.tree.nodes;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.ui.CanPopUp;
import com.daalitoy.apps.keedoh.ui.dialog.FieldDialog;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class FieldTreeNode extends KeedohMutableTreeNode implements CanPopUp, ActionListener {

  private static final long serialVersionUID = 1L;
  private Field field;

  private JPopupMenu popupMenu = new JPopupMenu();

  public FieldTreeNode(Field field) {
    super(field);
    this.field = field;
    setupPopupMenu();
  }

  public void init() {
    // add children
    List<Field> children = field.getChildren();
    for (Field f : children) {
      addToSelf(new FieldTreeNode(f));
    }
  }

  private void setupPopupMenu() {
    popupMenu.add(UIHelper.newJMenuItem("Edit", KeyEvent.VK_E, this, "__edit__"));
    popupMenu.add(UIHelper.newJMenuItem("Delete", KeyEvent.VK_D, this, "__delete__"));
  }

  public Field getField() {
    return field;
  }

  @Override
  public JPopupMenu getPopUp() {
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
    if (arg0.getActionCommand().equals("__edit__")) {
      Field formerParent = field.getParent();
      FieldDialog dialog = new FieldDialog(field, getOwningTree());
      if (dialog.showDialog() == 0) {
        Field currentParent = field.getParent();
        changeParent(field, formerParent, currentParent);
      }

      reload();
      showMe();

    } else if (arg0.getActionCommand().equals("__delete__")) {
      // Field.remove(field);
      removeSelf();
    }
  }
}
