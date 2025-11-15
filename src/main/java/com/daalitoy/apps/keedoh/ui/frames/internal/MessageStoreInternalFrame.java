package com.daalitoy.apps.keedoh.ui.frames.internal;

import com.daalitoy.apps.keedoh.data.common.FILE_TYPE;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.daalitoy.apps.keedoh.messaging.MessageStore;
import com.daalitoy.apps.keedoh.ui.table.data.MessageDataTableModel;
import com.daalitoy.apps.keedoh.ui.table.data.MessageStoreTableModel;
import com.daalitoy.apps.keedoh.ui.util.IconFactory;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

public class MessageStoreInternalFrame extends JInternalFrame implements ActionListener {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  JPanel leftComponent, rightComponent;
  private JTabbedPane pane;
  private JEditorPane leftEditorPane;
  private JSplitPane leftSplitPane;
  private JSplitPane rightSplitPane;
  private JEditorPane rightEditorPane;
  private JTable serverStoreTable;
  private JTable serverMsgDetailTable;
  private JTable clientMsgDetailTable;
  private JTable clientStoreTable;

  public MessageStoreInternalFrame() {
    super("Message Store");

    initComponents();
    setClosable(true);
    setIconifiable(true);
    setMaximizable(true);
    setSize(600, 400);
    setFrameIcon(UIHelper.getAppIcon());
    setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
    setFont(UIHelper.STANDARD_FONT);
    setVisible(true);
  }

  private void initComponents() {
    pane = new JTabbedPane();
    pane.setFont(UIHelper.STANDARD_BOLD_FONT);

    // leftComponent = new JPanel();
    leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    leftSplitPane.setDividerLocation((int) (0.8 * 400));
    initLeftPane();

    rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    rightSplitPane.setDividerLocation((int) (0.8 * 400));
    initRightPane();

    add(new JPanel(), BorderLayout.NORTH);
    add(new JPanel(), BorderLayout.SOUTH);
    add(new JPanel(), BorderLayout.EAST);
    add(new JPanel(), BorderLayout.WEST);

    add(pane, BorderLayout.CENTER);
  }

  private void initRightPane() {

    serverMsgDetailTable = new JTable();
    serverMsgDetailTable.setFont(UIHelper.STANDARD_FONT);
    serverMsgDetailTable.getTableHeader().setFont(UIHelper.STANDARD_FONT);

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(new JPanel(), BorderLayout.NORTH);
    panel.add(new JPanel(), BorderLayout.EAST);
    panel.add(new JPanel(), BorderLayout.WEST);

    serverStoreTable = new JTable(new MessageStoreTableModel(true));
    serverStoreTable.setFont(UIHelper.STANDARD_FONT);
    serverStoreTable.getTableHeader().setFont(UIHelper.STANDARD_BOLD_FONT);
    serverStoreTable.addMouseListener(
        new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            // clear
            int selectedRow = serverStoreTable.getSelectedRow();
            MessageData requestMsgData = MessageStore.getServerMsg(selectedRow);
            MessageDataTableModel model =
                new MessageDataTableModel(
                    requestMsgData, requestMsgData.getMessage().getRequestSegment());
            serverMsgDetailTable.setModel(model);
            model.fireTableDataChanged();
          }
        });

    panel.add(new JScrollPane(serverStoreTable), BorderLayout.CENTER);
    rightSplitPane.setTopComponent(panel);
    JPanel panel2 = new JPanel();
    panel2.setLayout(new BorderLayout());
    panel2.add(new JScrollPane(serverMsgDetailTable), BorderLayout.CENTER);

    JToolBar toolbar = new JToolBar();
    toolbar.setFont(UIHelper.STANDARD_FONT);

    JButton showRequestBtn = UIHelper.newButton(null, "__sshow_req__", this);
    showRequestBtn.setIcon(IconFactory.getIcon("export_icon.gif"));

    JButton showResponseBtn = UIHelper.newButton(null, "__sshow_resp__", this);
    showResponseBtn.setIcon(IconFactory.getIcon("import_icon.gif"));

    JButton exportBtn = UIHelper.newButton(null, "__sexp__", this);
    exportBtn.setIcon(IconFactory.getIcon("save_icon.png"));

    toolbar.add(showRequestBtn);
    toolbar.add(showResponseBtn);
    toolbar.add(exportBtn);
    panel2.add(toolbar, BorderLayout.PAGE_START);

    rightSplitPane.setBottomComponent(panel2);
    pane.addTab("Server Message Store", rightSplitPane);
  }

  private void initLeftPane() {
    clientMsgDetailTable = new JTable();
    clientMsgDetailTable.setFont(UIHelper.STANDARD_FONT);
    clientMsgDetailTable.getTableHeader().setFont(UIHelper.STANDARD_FONT);

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(new JPanel(), BorderLayout.NORTH);
    panel.add(new JPanel(), BorderLayout.EAST);
    panel.add(new JPanel(), BorderLayout.WEST);

    clientStoreTable = new JTable(new MessageStoreTableModel(true));
    clientStoreTable.setFont(UIHelper.STANDARD_FONT);
    clientStoreTable.getTableHeader().setFont(UIHelper.STANDARD_BOLD_FONT);
    clientStoreTable.addMouseListener(
        new MouseAdapter() {
          public void mouseClicked(MouseEvent e) {
            // clear
            int selectedRow = clientStoreTable.getSelectedRow();
            MessageData requestMsgData = MessageStore.getClientMsg(selectedRow);
            MessageDataTableModel model =
                new MessageDataTableModel(
                    requestMsgData, requestMsgData.getMessage().getRequestSegment());
            clientMsgDetailTable.setModel(model);
            model.fireTableDataChanged();
          }
        });

    panel.add(new JScrollPane(clientStoreTable), BorderLayout.CENTER);
    leftSplitPane.setTopComponent(panel);
    JPanel panel2 = new JPanel();
    panel2.setLayout(new BorderLayout());
    panel2.add(new JScrollPane(clientMsgDetailTable), BorderLayout.CENTER);

    JToolBar toolbar = new JToolBar();
    toolbar.setFont(UIHelper.STANDARD_FONT);

    JButton showRequestBtn = UIHelper.newButton(null, "__cshow_req__", this);
    showRequestBtn.setIcon(IconFactory.getIcon("export_icon.gif"));

    JButton showResponseBtn = UIHelper.newButton(null, "__cshow_resp__", this);
    showResponseBtn.setIcon(IconFactory.getIcon("import_icon.gif"));

    JButton exportBtn = UIHelper.newButton(null, "__cexp__", this);
    exportBtn.setIcon(IconFactory.getIcon("save_icon.png"));

    toolbar.add(showRequestBtn);
    toolbar.add(showResponseBtn);
    toolbar.add(exportBtn);
    panel2.add(toolbar, BorderLayout.PAGE_START);

    leftSplitPane.setBottomComponent(panel2);
    pane.addTab("Client Message Store", leftSplitPane);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {

    if (arg0.getActionCommand().equals("__sshow_req__")) {
      int row = serverStoreTable.getSelectedRow();
      MessageData requestMsgData = MessageStore.getServerMsg(row);
      MessageDataTableModel model =
          new MessageDataTableModel(
              requestMsgData, requestMsgData.getMessage().getRequestSegment());
      serverMsgDetailTable.setModel(model);
      model.fireTableDataChanged();
    } else if (arg0.getActionCommand().equals("__sshow_resp__")) {
      int row = serverStoreTable.getSelectedRow();
      MessageData requestMsgData = MessageStore.getServerMsg(row);
      MessageData responseMsgData = MessageStore.getServerResponseLeg(requestMsgData);
      MessageDataTableModel model =
          new MessageDataTableModel(
              responseMsgData, requestMsgData.getMessage().getResponseSegment());
      serverMsgDetailTable.setModel(model);
      model.fireTableDataChanged();
    } else if (arg0.getActionCommand().equals("__sexp__")) {
      MessageDataTableModel model = (MessageDataTableModel) serverMsgDetailTable.getModel();
      File file = UIHelper.getUserSelectedFile(this, FILE_TYPE.TEXT, false);
      if (file != null) {
        try {
          Files.append(model.toString(), file, Charsets.US_ASCII);
        } catch (IOException e) {
          UIHelper.showErrorDialog(this, "failed to export", e);
        }
      }

    } else if (arg0.getActionCommand().equals("__cshow_req__")) {
      int row = clientStoreTable.getSelectedRow();
      MessageData requestMsgData = MessageStore.getClientMsg(row);
      MessageDataTableModel model =
          new MessageDataTableModel(
              requestMsgData, requestMsgData.getMessage().getRequestSegment());
      clientMsgDetailTable.setModel(model);
      model.fireTableDataChanged();
    } else if (arg0.getActionCommand().equals("__cshow_resp__")) {
      int row = clientStoreTable.getSelectedRow();
      MessageData requestMsgData = MessageStore.getClientMsg(row);
      MessageData responseMsgData = MessageStore.getClientResponseLeg(requestMsgData);
      System.out.println(requestMsgData + " /" + responseMsgData);
      MessageDataTableModel model =
          new MessageDataTableModel(
              responseMsgData, requestMsgData.getMessage().getResponseSegment());
      clientMsgDetailTable.setModel(model);
      model.fireTableDataChanged();
    } else if (arg0.getActionCommand().equals("__cexp__")) {
      MessageDataTableModel model = (MessageDataTableModel) clientMsgDetailTable.getModel();
      File file = UIHelper.getUserSelectedFile(this, FILE_TYPE.TEXT, false);
      if (file != null) {
        try {
          Files.append(model.toString(), file, Charsets.US_ASCII);
        } catch (IOException e) {
          UIHelper.showErrorDialog(this, "failed to export", e);
        }
      }
    }
  }
}
