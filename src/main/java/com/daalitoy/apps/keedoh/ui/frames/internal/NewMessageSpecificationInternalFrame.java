package com.daalitoy.apps.keedoh.ui.frames.internal;

import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.ui.CanPopUp;
import com.daalitoy.apps.keedoh.ui.main.tree.KeedohTreeCellRenderer;
import com.daalitoy.apps.keedoh.ui.tree.dnd.MessageDefinitionTreeTransferHandler;
import com.daalitoy.apps.keedoh.ui.tree.dnd.MessageSpecDictTreeTransferHandler;
import com.daalitoy.apps.keedoh.ui.tree.nodes.MessageSpecFieldDictTreeNode;
import com.daalitoy.apps.keedoh.ui.tree.nodes.MessagesTreeNode;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NewMessageSpecificationInternalFrame extends JInternalFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Spec spec;

    public NewMessageSpecificationInternalFrame(Spec spec) {

        setTitle("New Message Specification");
        setSize(600, 600);
        this.spec = spec;

        JPanel panel = createSpecSetupPanel();

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        setClosable(true);
        setMaximizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setFrameIcon(UIHelper.getAppIcon());

        setVisible(true);
    }

    private JPanel createSpecSetupPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JSplitPane pane = new JSplitPane();
        pane.setDividerLocation(0.50);

        setLeftComponent(pane);
        setRightComponent(pane);

        panel.add(pane, BorderLayout.CENTER);

        return (panel);

    }

    private void setLeftComponent(JSplitPane pane) {
        MessageSpecFieldDictTreeNode rootNode = new MessageSpecFieldDictTreeNode(
                this, spec);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        final JTree tree = new JTree();
        rootNode.setOwningTree(tree);
        tree.setModel(model);
        tree.setFont(UIHelper.STANDARD_FONT);
        tree.setTransferHandler(new MessageSpecDictTreeTransferHandler());
        tree.setDragEnabled(true);
        tree.setCellRenderer(new KeedohTreeCellRenderer());

        tree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent arg0) {
                if (arg0.isPopupTrigger()) {

                    Object obj = tree.getLastSelectedPathComponent();
                    if (obj instanceof CanPopUp) {
                        ((CanPopUp) obj).getPopUp().show(tree, arg0.getX(),
                                arg0.getY());
                    }

                }

            }
        });
        // do any initializations, like adding children etc
        rootNode.init();
        pane.setLeftComponent(new JScrollPane(tree));

    }

    private void setRightComponent(JSplitPane pane) {
        final MessagesTreeNode rootNode = new MessagesTreeNode(spec);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        final JTree tree = new JTree();
        rootNode.setOwningTree(tree);
        tree.setModel(model);
        tree.setFont(UIHelper.STANDARD_FONT);
        tree.setTransferHandler(new MessageDefinitionTreeTransferHandler());
        tree.setDragEnabled(true);
        tree.setDropMode(DropMode.ON);
        tree.setCellRenderer(new KeedohTreeCellRenderer());

        tree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent arg0) {
                if (arg0.isPopupTrigger()) {

                    Object obj = tree.getLastSelectedPathComponent();
                    if (obj instanceof CanPopUp) {
                        ((CanPopUp) obj).getPopUp().show(tree, arg0.getX(),
                                arg0.getY());
                    }

                }

            }
        });

        rootNode.init();
        pane.setRightComponent(new JScrollPane(tree));

    }
}
