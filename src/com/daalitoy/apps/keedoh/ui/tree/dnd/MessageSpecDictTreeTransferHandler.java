package com.daalitoy.apps.keedoh.ui.tree.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;

import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;

public class MessageSpecDictTreeTransferHandler extends TransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TARGET_CLASS = "com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode";
	private static DataFlavor DATA_FLAVOR = null;

	static {
		try {
			DATA_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
					+ ";class=" + TARGET_CLASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int getSourceActions(JComponent c) {
		return COPY;
	}

	public Transferable createTransferable(JComponent c) {
		
		JTree tree = (JTree) c;
		final KeedohMutableTreeNode obj = (KeedohMutableTreeNode) tree
				.getLastSelectedPathComponent();
		return new Transferable() {

			@Override
			public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException, IOException {
				return (obj);
			}

			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DATA_FLAVOR };
			}

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				if (flavor.equals(DATA_FLAVOR)) {
					return (true);
				} else {
					return (false);
				}
			}

		};
	}

	public void exportDone(JComponent c, Transferable t, int action) {
		// nothing to do
	}

}
