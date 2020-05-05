package com.daalitoy.apps.keedoh.ui.dialog;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.data.model.MLI;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ConnectorConfigDialog extends JDialog implements ActionListener {

    /** */
    private static final long serialVersionUID = 1L;

    private JTextField tfConfigName = UIHelper.newTextField(20);
    private JTextField tfConfigIp = UIHelper.newTextField(20);
    private JTextField tfConfigPort = UIHelper.newTextField(5);

    private JComboBox cbMliTypes = UIHelper.newComboBox();
    //private JTextField tfConfigScript = UIHelper.newTextField(20);

    private ConnectorConfig connectorConfig = null;

    public ConnectorConfigDialog(JComponent parent) {

        setModal(true);
        // setLocationRelativeTo(parent);
        this.setLocation(
                parent.getX() + (parent.getWidth() / 2), parent.getY() + (parent.getHeight() / 2));
        initComponents();
        setSize(400, 200);
        this.setIconImage(UIHelper.getAppImage());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("New Connector Config");
    }

    public ConnectorConfigDialog(JComponent parent, ConnectorConfig listenerConfig) {
        this.connectorConfig = listenerConfig;
        setModal(true);
        setLocationRelativeTo(parent);
        initComponents();
        setSize(400, 200);
        this.setIconImage(UIHelper.getAppImage());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Edit Connector Config");
    }

    private void initComponents() {

        Arrays.asList(MLI.values()).stream().forEach(cbMliTypes::addItem);
        tfConfigName.setText("Unnamed Config");
        tfConfigIp.setText("127.0.0.1");
        tfConfigPort.setText("9090");

        if (connectorConfig != null) {
            // implies a edit of existing config
            tfConfigName.setText(connectorConfig.getName());
            tfConfigIp.setText(connectorConfig.getIp());
            tfConfigPort.setText(Integer.toString(connectorConfig.getPort()));
            cbMliTypes.setSelectedItem(connectorConfig.getMli());
            //tfConfigScript.setText(connectorConfig.getProcessorScript());

            // cannot change name
            tfConfigName.setEditable(false);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.add(UIHelper.newLabel("Config Name"));
        panel.add(tfConfigName);
        panel.add(UIHelper.newLabel("IP"));
        panel.add(tfConfigIp);
        panel.add(UIHelper.newLabel("Port"));
        panel.add(tfConfigPort);
        panel.add(UIHelper.newLabel("MLI"));
        panel.add(cbMliTypes);
        //panel.add(UIHelper.newLabel("Script"));
        //panel.add(tfConfigScript);

        JPanel panel2 = new JPanel();
        panel2.add(UIHelper.newButton("OK", "__ok__", this));
        panel2.add(UIHelper.newButton("Cancel", "__cancel__", this));

        add(new JPanel(), BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.WEST);

        add(panel, BorderLayout.CENTER);
        add(panel2, BorderLayout.SOUTH);
    }

    public ConnectorConfig showDialog() {
        setVisible(true);
        return (connectorConfig);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equals("__ok__")) {
            if (connectorConfig == null) {
                connectorConfig = new ConnectorConfig();
                connectorConfig.setName(tfConfigName.getText());
            }
            connectorConfig.setIp(tfConfigIp.getText());
            connectorConfig.setPort(Integer.parseInt(tfConfigPort.getText()));
            connectorConfig.setMli((MLI) cbMliTypes.getSelectedItem());
            //connectorConfig.setProcessorScript(tfConfigScript.getText());
        }
        dispose();
    }
}
