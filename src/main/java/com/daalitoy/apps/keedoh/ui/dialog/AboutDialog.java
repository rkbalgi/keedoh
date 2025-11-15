package com.daalitoy.apps.keedoh.ui.dialog;

import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import com.google.common.io.Resources;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AboutDialog extends JDialog implements ActionListener {

  /** */
  private static final long serialVersionUID = 1L;

  private static String aboutContent = "";

  static {
    try {
      /*InputSupplier<InputStreamReader> supplier = Resources.
      .newReaderSupplier(
      		Resources
      				.getResource("com/daalitoy/apps/keedoh/resources/about.txt"),
      		Charsets.US_ASCII);*/
      byte[] fileData = Resources.toByteArray(Resources.getResource("about.txt"));
      aboutContent = new String(fileData);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public AboutDialog(JComponent parent) {
    // super(parent);
    // setLocationRelativeTo(parent);
    this.setLocation(
        parent.getX() + (parent.getWidth() / 2), parent.getY() + (parent.getHeight() / 2));
    setModal(true);
    setTitle("About Keedoh!");
    initComponents();
    setSize(400, 200);
  }

  public void showDialog() {
    setVisible(true);
  }

  private void initComponents() {

    JTextArea aboutArea = UIHelper.newTextArea(10, 20);
    aboutArea.setText(aboutContent);
    aboutArea.setCaretPosition(0);
    aboutArea.setEditable(false);
    JButton okBtn = UIHelper.newButton("OK", "__", this);
    JPanel panel = new JPanel();
    panel.add(okBtn);

    setLayout(new BorderLayout());
    add(new JScrollPane(aboutArea), BorderLayout.CENTER);
    add(panel, BorderLayout.SOUTH);
    add(new JPanel(), BorderLayout.NORTH);
    add(new JPanel(), BorderLayout.EAST);
    add(new JPanel(), BorderLayout.WEST);

    setIconImage(UIHelper.getAppImage());
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    dispose();
  }
}
