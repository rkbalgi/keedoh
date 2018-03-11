package com.daalitoy.apps.keedoh.ui.main.tree;

import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.ui.CanPopUp;
import com.daalitoy.apps.keedoh.ui.frames.KeedohMainFrame;
import com.daalitoy.apps.keedoh.ui.frames.internal.NewMessageSpecificationInternalFrame;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class SpecTreeNode extends KeedohMutableTreeNode implements CanPopUp,
        ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Spec spec;
    private JPopupMenu popup = new JPopupMenu();

    public SpecTreeNode(Spec spec) {
        super(spec.getSpecName());
        this.spec = spec;
        setupPopupMenu();
    }

    public void init() {
        SpecMessagesTreeNode childNode = new SpecMessagesTreeNode(spec);
        addToSelf(childNode);
        childNode.init();
    }

    public Spec getSpec() {
        return spec;
    }

    private void setupPopupMenu() {
        JMenuItem item = UIHelper.newJMenuItem("Edit", KeyEvent.VK_E,
                this, "__edit__");
        popup.add(item);

    }

    @Override
    public JPopupMenu getPopUp() {

        return (popup);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getActionCommand().equals("__edit__")) {
            JInternalFrame frame = new NewMessageSpecificationInternalFrame(
                    spec);
            KeedohMainFrame.getFrame().loadIntoDesktopPane(frame);
        }

    }

}
