package com.daalitoy.apps.keedoh.ui.tree.dnd;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.ui.tree.nodes.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.*;

public class MessageDefinitionTreeTransferHandler extends TransferHandler {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private static final String TARGET_CLASS =
      "com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode";
  private static DataFlavor DATA_FLAVOR = null;

  static {
    try {
      DATA_FLAVOR =
          new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + TARGET_CLASS);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public boolean canImport(TransferSupport supp) {

    if (!supp.isDataFlavorSupported(DATA_FLAVOR)) {
      return false;
    }

    // Fetch the drop location
    DropLocation loc = supp.getDropLocation();

    // Return whether we accept the location
    return shouldAcceptDropLocation(loc);
  }

  private boolean shouldAcceptDropLocation(DropLocation location) {
    JTree.DropLocation loc = (javax.swing.JTree.DropLocation) location;
    KeedohMutableTreeNode node = (KeedohMutableTreeNode) loc.getPath().getLastPathComponent();

    return (node instanceof RequestFragmentTreeNode) || (node instanceof ResponseFragmentTreeNode);
  }

  public boolean importData(TransferSupport supp) {
    if (!canImport(supp)) {
      return false;
    }

    // Fetch the Transferable and its data
    Transferable t = supp.getTransferable();
    try {
      KeedohMutableTreeNode node = (KeedohMutableTreeNode) t.getTransferData(DATA_FLAVOR);
      // Fetch the drop location

      // Insert the data at this location

      JTree.DropLocation loc = (javax.swing.JTree.DropLocation) supp.getDropLocation();
      if (loc.getChildIndex() == -1) {
        // insert at end
        KeedohMutableTreeNode parentNode =
            (KeedohMutableTreeNode) loc.getPath().getLastPathComponent();
        Field field = ((FieldTreeNode) node).getField();

        if (parentNode instanceof RequestFragmentTreeNode) {
          RequestFragmentTreeNode fragment = (RequestFragmentTreeNode) parentNode;
          if (!fragment.contains(field)) {
            fragment.getFragment().add(field);
            MessageFieldTreeNode newNode = new MessageFieldTreeNode(fragment.getFragment(), field);

            parentNode.addToSelf(newNode);
            newNode.init();
          }
        } else if (parentNode instanceof ResponseFragmentTreeNode) {
          ResponseFragmentTreeNode fragment = (ResponseFragmentTreeNode) parentNode;
          if (!fragment.contains(field)) {
            fragment.getFragment().add(field);
            MessageFieldTreeNode newNode = new MessageFieldTreeNode(fragment.getFragment(), field);
            parentNode.addToSelf(newNode);
            newNode.init();
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return true;
  }
}
