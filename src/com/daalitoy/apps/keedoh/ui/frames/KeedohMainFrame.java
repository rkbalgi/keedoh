package com.daalitoy.apps.keedoh.ui.frames;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.ui.CanPopUp;
import com.daalitoy.apps.keedoh.ui.dialog.AboutDialog;
import com.daalitoy.apps.keedoh.ui.dialog.ConnectorConfigDialog;
import com.daalitoy.apps.keedoh.ui.dialog.ListenerConfigDialog;
import com.daalitoy.apps.keedoh.ui.frames.internal.MessageStoreInternalFrame;
import com.daalitoy.apps.keedoh.ui.main.tree.ConnectorTreeNode;
import com.daalitoy.apps.keedoh.ui.main.tree.ConnectorsConfigTreeNode;
import com.daalitoy.apps.keedoh.ui.main.tree.KeedohTreeCellRenderer;
import com.daalitoy.apps.keedoh.ui.main.tree.ListenerTreeNode;
import com.daalitoy.apps.keedoh.ui.main.tree.ListenersConfigTreeNode;
import com.daalitoy.apps.keedoh.ui.main.tree.SpecsTreeNode;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;
import com.daalitoy.apps.keedoh.ui.util.KeedohUtils;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;

public class KeedohMainFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private JTree msgSpecTree;
	private JDesktopPane desktopPane = new JDesktopPane();

	private JTree specsTree = null;
	private KeedohMutableTreeNode rootNode = new KeedohMutableTreeNode("Keedoh");
	private SpecsTreeNode specsNode = new SpecsTreeNode();
	private ConnectorsConfigTreeNode connectorsNode = new ConnectorsConfigTreeNode(
			"Connector Configurations");
	private ListenersConfigTreeNode listenersNode = new ListenersConfigTreeNode(
			"Listener Configuration");

	private static KeedohMainFrame instance = null;

	private MouseAdapter mAdapter = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent arg0) {
			if (arg0.isPopupTrigger()) {

				Object obj = specsTree.getLastSelectedPathComponent();
				if (obj instanceof CanPopUp) {
					((CanPopUp) obj).getPopUp().show(specsTree, arg0.getX(),
							arg0.getY());
				}
			}
		}

	};

	public KeedohMainFrame() {

		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		setSize(bounds.width, bounds.height - 30);
		setTitle("Keedoh! - ATM/POS Financial Messaging Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initComponents();
		setVisible(true);
		instance = this;

	}

	private void initComponents() {

		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

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
		connectorsNode.init();
		listenersNode.setOwningTree(specsTree);
		listenersNode.init();

		rootNode.add(specsNode);
		rootNode.add(connectorsNode);
		rootNode.add(listenersNode);

		specsTree.addMouseListener(mAdapter);

		// add existing specs
		specsNode.addSpecs();

		return (new JScrollPane(specsTree));

	}

	private void createMenus() {

		JMenuBar bar = new JMenuBar();
		JMenu fileMenu = UIHelper.newJMenu("File", KeyEvent.VK_F, this);
		JMenu fileNewMenu = UIHelper.newJMenu("New", KeyEvent.VK_N, this);

		fileNewMenu.add(UIHelper.newJMenuItem("Specification", KeyEvent.VK_S,
				this, "__new_specification__"));
		fileNewMenu.add(UIHelper.newJMenuItem("Listener Config", KeyEvent.VK_L,
				this, "__new_listener__"));
		fileNewMenu.add(UIHelper.newJMenuItem("Connector Config",
				KeyEvent.VK_C, this, "__new_connector__"));

		fileMenu.add(fileNewMenu);
		fileMenu.add(UIHelper.newJMenuItem("Exit", KeyEvent.VK_X, this, "exit"));

		JMenu aboutMenu = UIHelper.newJMenu("About", KeyEvent.VK_A, this);
		aboutMenu.add(UIHelper.newJMenuItem("About Keedoh!", KeyEvent.VK_A,
				this, "__about__"));

		JMenu viewMenu = UIHelper.newJMenu("View", KeyEvent.VK_V, this);
		viewMenu.add(UIHelper.newJMenuItem("Message Store", KeyEvent.VK_M,
				this, "__mstore__"));

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
			ListenerConfig config = new ListenerConfigDialog(desktopPane)
					.showDialog();
			if (config != null) {
				ListenerConfig.newConfig(config);
				ListenerTreeNode node = new ListenerTreeNode(config);
				listenersNode.addToSelf(node);

			}

		} else if (e.getActionCommand().equals("__new_connector__")) {
			ConnectorConfig config = new ConnectorConfigDialog(desktopPane)
					.showDialog();
			if (config != null) {
				ConnectorConfig.newConfig(config);
				ConnectorTreeNode node = new ConnectorTreeNode(config);
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

	public static KeedohMainFrame getFrame() {
		return (instance);
	}

	public static JComponent getDesktopPane() {
		return (instance.desktopPane);
	}

}
