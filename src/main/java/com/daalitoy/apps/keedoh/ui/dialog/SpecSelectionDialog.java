package com.daalitoy.apps.keedoh.ui.dialog;

import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.system.SpecManager;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import com.google.inject.Inject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SpecSelectionDialog extends JDialog implements ActionListener {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private JComboBox cbSpecs = UIHelper.newComboBox();

  private Spec selectedSpec;
  @Inject private SpecManager specManager;

  public SpecSelectionDialog(JComponent parent) {
    setLocation(UIHelper.getPreferredLocation(parent));
    setModal(true);
    setTitle("Select Spec");

    setSize(400, 120);
  }

  public Spec showDialog() {
    initComponents();
    setVisible(true);
    return (selectedSpec);
  }

  private void initComponents() {

    for (Spec spec : specManager.allSpecs()) {
      cbSpecs.addItem(spec);
    }

    JPanel panel2 = new JPanel();
    panel2.add(UIHelper.newLabel("Specification"));
    panel2.add(cbSpecs);
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
      selectedSpec = (Spec) cbSpecs.getSelectedItem();
    } else {
      selectedSpec = null;
    }
    dispose();
  }
}
