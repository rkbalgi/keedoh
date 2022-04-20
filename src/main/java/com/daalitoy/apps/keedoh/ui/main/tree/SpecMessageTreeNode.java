package com.daalitoy.apps.keedoh.ui.main.tree;

import com.daalitoy.apps.keedoh.data.model.Message;
import com.daalitoy.apps.keedoh.ui.CanPopUp;
import com.daalitoy.apps.keedoh.ui.frames.KeedohMainFrame;
import com.daalitoy.apps.keedoh.ui.frames.internal.MessageInternalFrame;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import io.github.rkbalgi.iso4k.MessageSegment;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class SpecMessageTreeNode extends KeedohMutableTreeNode implements CanPopUp, ActionListener {

    /** */
    private static final long serialVersionUID = 1L;

    private JPopupMenu popupMenu = new JPopupMenu();
    private MessageSegment msg;

    public SpecMessageTreeNode(MessageSegment msg) {
        super(msg.getName());
        this.msg = msg;
        setupPopupMenu();
    }

    public void init() {
    }

    private void setupPopupMenu() {
        popupMenu.add(UIHelper.newJMenuItem("Open", KeyEvent.VK_O, this, "__open__"));
    }

    @Override
    public JPopupMenu getPopUp() {
        return (popupMenu);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equals("__open__")) {
            JInternalFrame frame = new MessageInternalFrame(msg);
            //log.debug(() -> msg.dumpToString());
            KeedohMainFrame.getFrame().loadIntoDesktopPane(frame);
        }
    }
}
