package com.daalitoy.apps.keedoh.ui.frames.internal;

import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.daalitoy.apps.keedoh.ui.table.data.EditableMessageTableModel;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ResponseMsgDialog extends JDialog {

  private final MessageData responseMsgData;
  private final JComponent parent;

  public ResponseMsgDialog(JComponent parent, MessageData rpMsgData) {
    super();

    setTitle(
        "Response: "
            + rpMsgData.getMessage().getSpec().getSpecName()
            + "//"
            + rpMsgData.getMessage().getMsgName());
    this.responseMsgData = rpMsgData;
    this.parent = parent;
    initComponents();
  }

  private void initComponents() {

    setSize(400, 400);
    setLocationRelativeTo(parent);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BorderLayout());
    JPanel tmpPanel = new JPanel();
    JLabel label = UIHelper.newLabel(" RESPONSE  ");
    tmpPanel.setBackground(Color.GREEN);
    tmpPanel.add(label);
    bottomPanel.add(tmpPanel, BorderLayout.NORTH);

    JTable responseMsgDataTable = new JTable();
    responseMsgDataTable.setModel(
        new EditableMessageTableModel(responseMsgData.getMessage(), false));
    responseMsgDataTable.setFont(UIHelper.STANDARD_FONT);
    responseMsgDataTable.getTableHeader().setFont(UIHelper.STANDARD_BOLD_FONT);

    bottomPanel.add(new JScrollPane(responseMsgDataTable), BorderLayout.CENTER);

    EditableMessageTableModel model = new EditableMessageTableModel(responseMsgData);
    responseMsgDataTable.setModel(model);
    model.fireTableDataChanged();

    setLayout(new BorderLayout());
    add(new JScrollPane(responseMsgDataTable), BorderLayout.CENTER);
    add(new JPanel(), BorderLayout.WEST);
    add(new JPanel(), BorderLayout.EAST);

    JPanel panel = new JPanel();
    JButton btn =
        UIHelper.newButton(
            "OK",
            "",
            (event) -> {
              this.dispose();
            });
    panel.add(btn);
    add(panel, BorderLayout.SOUTH);
  }

  public void showDialog() {

    setModal(true);
    this.setVisible(true);
  }
}
