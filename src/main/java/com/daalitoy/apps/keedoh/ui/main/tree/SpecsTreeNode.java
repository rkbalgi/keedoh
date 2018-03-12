package com.daalitoy.apps.keedoh.ui.main.tree;

import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.system.SpecManager;
import com.daalitoy.apps.keedoh.ui.dialog.SimpleTextInputDialog;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

public class SpecsTreeNode extends KeedohMutableTreeNode implements /*CanPopUp,*/ ActionListener {

    /** */
    private static final long serialVersionUID = 1L;
    SpecManager specManager;
    private JPopupMenu popup = new JPopupMenu();

    public SpecsTreeNode(SpecManager specManager) {

        super("Specifications");
        this.specManager = specManager;
        setupPopupMenu();
    }

    public void addSpecs() {
        List<Spec> specs = specManager.allSpecs();
        for (Spec spec : specs) {
            SpecTreeNode node = new SpecTreeNode(spec);
            addToSelf(node);
            node.init();
        }
        reload();
        collapse();
    }

    private void setupPopupMenu() {
        JMenuItem item = UIHelper.newJMenuItem("New", KeyEvent.VK_N, this, "__new__");
        popup.add(item);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equals("__new__")) {
            SimpleTextInputDialog dialog =
                    new SimpleTextInputDialog(getOwningTree(), "New Specification");
            String interfaceName = dialog.showDialog("Specification Name");
            if (interfaceName != null) {
                // add it to the db
                Spec spec = specManager.newSpec(interfaceName);
                SpecTreeNode node = new SpecTreeNode(spec);
                addToSelf(node);
            }
        }
    }
}
