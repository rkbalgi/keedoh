package com.daalitoy.apps.keedoh.ui.frames;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.providers.ConnectorConfigProvider;
import com.daalitoy.apps.keedoh.data.providers.ListenerConfigProvider;
import com.daalitoy.apps.keedoh.system.SpecManager;
import com.daalitoy.apps.keedoh.ui.CanPopUp;
import com.daalitoy.apps.keedoh.ui.dialog.AboutDialog;
import com.daalitoy.apps.keedoh.ui.dialog.ConnectorConfigDialog;
import com.daalitoy.apps.keedoh.ui.dialog.ListenerConfigDialog;
import com.daalitoy.apps.keedoh.ui.frames.internal.MessageStoreInternalFrame;
import com.daalitoy.apps.keedoh.ui.main.tree.*;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;
import com.daalitoy.apps.keedoh.ui.util.KeedohUtils;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import com.google.inject.Inject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KeedohMainFrame extends JFrame implements ActionListener {

    /** */
    private static final long serialVersionUID = 1L;

    private static KeedohMainFrame instance = null;
    // private JTree msgSpecTree;
    private JDesktopPane desktopPane = new JDesktopPane();
    private JTree specsTree = null;
    private KeedohMutableTreeNode rootNode = new KeedohMutableTreeNode("Keedoh");
    private SpecsTreeNode specsNode;
    private ConnectorsConfigTreeNode connectorsNode;
    private ListenersConfigTreeNode listenersNode;
    private ListenerConfigProvider listenerConfigProvider;
    private ConnectorConfigProvider connectorConfigProvider;
    private SpecManager specManager;
    private MouseAdapter mAdapter =
            new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent arg0) {

                    if (arg0.isPopupTrigger()) {
                        Object obj = specsTree.getLastSelectedPathComponent();
                        if (obj instanceof CanPopUp) {
                            ((CanPopUp) obj).getPopUp().show(specsTree, arg0.getX(), arg0.getY());
                        }
                    }
                }
            };

    public KeedohMainFrame() {

        Rectangle bounds =
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice()
                        .getDefaultConfiguration()
                        .getBounds();
        setSize(bounds.width, bounds.height - 30);
        setTitle("Keedoh! - ATM/POS Financial Messaging Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        instance = this;
    }

    public static KeedohMainFrame getFrame() {
        return (instance);
    }

    public static JComponent getDesktopPane() {
        return (instance.desktopPane);
    }

    @Inject
    public void setSpecManager(SpecManager specManager) {
        this.specManager = specManager;
    }

    @Inject
    public void setConfigProviders(
            ListenerConfigProvider lProvider, ConnectorConfigProvider cProvider) {

        this.listenerConfigProvider = lProvider;
        this.connectorConfigProvider = cProvider;
        connectorsNode = new ConnectorsConfigTreeNode(cProvider);
        connectorsNode.init();
        listenersNode = new ListenersConfigTreeNode(lProvider);
        listenersNode.init();
    }

    public void showFrame() {

        initComponents();
        setVisible(true);
    }

    private void initComponents() {

        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        specsNode = new SpecsTreeNode(specManager);

        pane.setDividerLocation((int) (0.2 * KeedohUtils.getWidth()));
        pane.setLeftComponent(buildLeftComponent());

        // JPanel rightComponent = new JPanel();
        JSplitPane pane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        pane2.setTopComponent(desktopPane);
        pane2.setBottomComponent(new JPanel());

        pane2.setDividerLocation((int) (0.7 * KeedohUtils.getHeight()));
        // rightComponent.add(pane2);

        pane.setRightComponent(pane2);

        setIconImage(UIHelper.getAppImage());
        add(pane);

        createMenus();
    }

    private Component buildLeftComponent() {

        specsTree = new JTree(rootNode);
        specsTree.setCellRenderer(new KeedohTreeCellRenderer());
        specsTree.setFont(UIHelper.STANDARD_FONT);

        rootNode.setOwningTree(specsTree);
        specsNode.setOwningTree(specsTree);
        connectorsNode.setOwningTree(specsTree);
        // connectorsNode.init();
        listenersNode.setOwningTree(specsTree);
        // listenersNode.init();

        rootNode.add(specsNode);
        rootNode.add(connectorsNode);
        rootNode.add(listenersNode);

        specsTree.addMouseListener(mAdapter);

        // add existing specs
        specsNode.addSpecs();

        for (int i = 0; i < specsTree.getRowCount(); i++) {
            specsTree.expandRow(i);
        }

        return (new JScrollPane(specsTree));
    }

    private void createMenus() {

        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = UIHelper.newJMenu("File", KeyEvent.VK_F, this);
        JMenu fileNewMenu = UIHelper.newJMenu("New", KeyEvent.VK_N, this);

        fileNewMenu.add(
                UIHelper.newJMenuItem("Specification", KeyEvent.VK_S, this, "__new_specification__"));
        fileNewMenu.add(
                UIHelper.newJMenuItem("Listener Config", KeyEvent.VK_L, this, "__new_listener__"));
        fileNewMenu.add(
                UIHelper.newJMenuItem("Connector Config", KeyEvent.VK_C, this, "__new_connector__"));

        fileMenu.add(fileNewMenu);
        fileMenu.add(UIHelper.newJMenuItem("Exit", KeyEvent.VK_X, this, "exit"));

        JMenu aboutMenu = UIHelper.newJMenu("About", KeyEvent.VK_A, this);
        aboutMenu.add(UIHelper.newJMenuItem("About Keedoh!", KeyEvent.VK_A, this, "__about__"));

        JMenu viewMenu = UIHelper.newJMenu("View", KeyEvent.VK_V, this);
        viewMenu.add(UIHelper.newJMenuItem("Message Store", KeyEvent.VK_M, this, "__mstore__"));

        bar.add(fileMenu);
        bar.add(viewMenu);
        bar.add(aboutMenu);
        setJMenuBar(bar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("exit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("__new_listener__")) {
            ListenerConfig config = new ListenerConfigDialog(desktopPane).showDialog();
            if (config != null) {
                listenerConfigProvider.newConfig(config);
                ListenerTreeNode node = new ListenerTreeNode(config, listenerConfigProvider);
                listenersNode.addToSelf(node);
            }

        } else if (e.getActionCommand().equals("__new_connector__")) {
            ConnectorConfig config = new ConnectorConfigDialog(desktopPane).showDialog();
            if (config != null) {
                connectorConfigProvider.newConfig(config);
                ConnectorTreeNode node = new ConnectorTreeNode(config, connectorConfigProvider);
                connectorsNode.addToSelf(node);
            }

        } else if (e.getActionCommand().equals("__about__")) {
            new AboutDialog(desktopPane).showDialog();
        } else if (e.getActionCommand().equals("__mstore__")) {
            loadIntoDesktopPane(new MessageStoreInternalFrame());
        }
    }

    public void loadIntoDesktopPane(JInternalFrame frame) {

        try {

            desktopPane.add(frame);
            frame.setMaximum(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
