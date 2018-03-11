package com.daalitoy.apps.keedoh.ui.dialog;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectorSelectionDialog extends JDialog implements
        ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JComboBox cbConfigs = UIHelper.newComboBox();

    private ConnectorConfig selectedConfig;

    public ConnectorSelectionDialog(JComponent parent) {
        // super(parent);
        // setLocationRelativeTo(parent);
        this.setLocation(parent.getX() + (parent.getWidth() / 2), parent.getY()
                + (parent.getHeight() / 2));
        setModal(true);
        setTitle("Select Connector Config");
        initComponents();
        setSize(300, 100);
    }

    public ConnectorConfig showDialog() {
        setVisible(true);
        return (selectedConfig);
    }

    private void initComponents() {

		/*for (ConnectorConfig config : ConnectorConfig.getInstances()) {
			cbConfigs.addItem(config);
		}*/

        JPanel panel2 = new JPanel();
        panel2.add(UIHelper.newLabel("Config"));
        panel2.add(cbConfigs);
        JButton okBtn = UIHelper.newButton("OK", "__ok__", this);
        JButton cancelBtn = UIHelper.newButton("Cancel", "__cancel__", this);
        JPanel panel = new JPanel();

        panel.add(okBtn);
        panel.add(cancelBtn);

        setLayout(new BorderLayout());
        add(panel2, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        add(new JPanel(), BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.WEST);

        setIconImage(UIHelper.getAppImage());

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equals("__ok__")) {
            selectedConfig = (ConnectorConfig) cbConfigs.getSelectedItem();
        } else {
            selectedConfig = null;
        }
        dispose();

    }
}
