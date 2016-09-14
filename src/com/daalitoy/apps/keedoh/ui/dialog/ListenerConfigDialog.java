package com.daalitoy.apps.keedoh.ui.dialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.model.MLI;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;

public class ListenerConfigDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tfConfigName = UIHelper.newTextField(20);
	private JTextField tfConfigIp = UIHelper.newTextField(20);
	private JTextField tfConfigPort = UIHelper.newTextField(5);

	private JComboBox cbMliTypes = UIHelper.newComboBox();
	private JTextField tfConfigScript = UIHelper.newTextField(20);

	private ListenerConfig listenerConfig = null;

	public ListenerConfigDialog(JComponent parent) {

		setModal(true);
		//setLocationRelativeTo(parent);
		this.setLocation(parent.getX() + (parent.getWidth() / 2), parent.getY()
				+ (parent.getHeight() / 2));
		initComponents();
		setSize(400, 200);
		this.setIconImage(UIHelper.getAppImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("New Listener Config");
	}

	public ListenerConfigDialog(JComponent parent, ListenerConfig listenerConfig) {
		this.listenerConfig = listenerConfig;
		setModal(true);
		setLocation(UIHelper.getPreferredLocation(parent));
		initComponents();
		setSize(400, 200);
		this.setIconImage(UIHelper.getAppImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Edit Listener Config");
	}

	private void initComponents() {

		// defaults
		for (MLI mli : MLI.getInstances()) {
			System.out.println(mli);
			cbMliTypes.addItem(mli);
		}
		
		tfConfigName.setText("Unnamed Config");
		tfConfigIp.setText("127.0.0.1");
		tfConfigPort.setText("9090");

		if (listenerConfig != null) {
			// implies a edit of existing config
			tfConfigName.setText(listenerConfig.getName());
			tfConfigIp.setText(listenerConfig.getIp());
			tfConfigPort.setText(Integer.toString(listenerConfig.getPort()));
			System.out.println(listenerConfig.getMli());
			cbMliTypes.setSelectedItem(listenerConfig.getMli());
			tfConfigScript.setText(listenerConfig.getProcessorScript());
			
			//cannot change name
			tfConfigName.setEditable(false);
		}
		tfConfigName.setCaretPosition(0);
		

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 2));
		panel.add(UIHelper.newLabel("Config Name"));
		panel.add(tfConfigName);
		panel.add(UIHelper.newLabel("IP"));
		panel.add(tfConfigIp);
		panel.add(UIHelper.newLabel("Port"));
		panel.add(tfConfigPort);
		panel.add(UIHelper.newLabel("MLI"));
		panel.add(cbMliTypes);
		panel.add(UIHelper.newLabel("Script"));
		panel.add(tfConfigScript);

		JPanel panel2 = new JPanel();
		panel2.add(UIHelper.newButton("OK", "__ok__", this));
		panel2.add(UIHelper.newButton("Cancel", "__cancel__", this));

		add(new JPanel(), BorderLayout.NORTH);
		add(new JPanel(), BorderLayout.EAST);
		add(new JPanel(), BorderLayout.WEST);

		add(panel, BorderLayout.CENTER);
		add(panel2, BorderLayout.SOUTH);
	}

	public ListenerConfig showDialog() {
		setVisible(true);
		return (listenerConfig);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("__ok__")) {
			if (listenerConfig == null) {
				listenerConfig = new ListenerConfig();
				listenerConfig.setName(tfConfigName.getText());
			}
			listenerConfig.setIp(tfConfigIp.getText());
			listenerConfig.setPort(Integer.parseInt(tfConfigPort.getText()));
			listenerConfig.setMli((MLI) cbMliTypes.getSelectedItem());
			listenerConfig.setProcessorScript(tfConfigScript.getText());
		}
		dispose();

	}

}
