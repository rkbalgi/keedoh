package com.daalitoy.apps.keedoh.ui.dialog;

import com.daalitoy.apps.keedoh.ui.util.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleTextInputDialog extends JDialog implements ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JTextField field = UIHelper.newTextField(20);
    private String response = null;

    public SimpleTextInputDialog(JComponent parent, String title) {

        setLocation(parent.getX() + 300, parent.getY() + 200);
        //setLocationRelativeTo(parent);
        setModal(true);
    }

    public String showDialog(String msg) {

        JPanel panel = new JPanel();
        JButton button1 = UIHelper.newButton("OK", "__ok__", this);
        JButton button2 = UIHelper.newButton("Cancel", "__cancel__",
                this);

        panel.add(UIHelper.newLabel(msg));
        panel.add(field);

        JPanel lPanel = new JPanel();
        lPanel.add(button1);
        lPanel.add(button2);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.add(panel, BorderLayout.CENTER);
        panel2.add(lPanel, BorderLayout.SOUTH);

        add(panel2);
        setSize(400, 100);
        setVisible(true);

        return (response);

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equals("__ok__")) {
            response = field.getText();
        } else {
            response = null;
        }
        dispose();

    }

}
