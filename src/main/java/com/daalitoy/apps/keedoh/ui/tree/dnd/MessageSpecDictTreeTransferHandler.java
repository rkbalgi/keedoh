package com.daalitoy.apps.keedoh.ui.tree.dnd;

import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.*;

public class MessageSpecDictTreeTransferHandler extends TransferHandler {

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

  public int getSourceActions(JComponent c) {
    return COPY;
  }

  public Transferable createTransferable(JComponent c) {

    JTree tree = (JTree) c;
    final KeedohMutableTreeNode obj = (KeedohMutableTreeNode) tree.getLastSelectedPathComponent();
    return new Transferable() {

      @Override
      public Object getTransferData(DataFlavor flavor) {
        return (obj);
      }

      @Override
      public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {DATA_FLAVOR};
      }

      @Override
      public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(DATA_FLAVOR);
      }
    };
  }

  public void exportDone(JComponent c, Transferable t, int action) {
    // nothing to do
  }
}
