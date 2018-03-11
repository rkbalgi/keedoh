package com.daalitoy.apps.keedoh.ui.frames.internal;

import com.daalitoy.apps.keedoh.data.common.FILE_TYPE;
import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.data.model.Message;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.daalitoy.apps.keedoh.messaging.KeedohMessageTimeoutException;
import com.daalitoy.apps.keedoh.messaging.MessagingManager;
import com.daalitoy.apps.keedoh.ui.dialog.ConnectorSelectionDialog;
import com.daalitoy.apps.keedoh.ui.dialog.TraceInputDialog;
import com.daalitoy.apps.keedoh.ui.dialog.TraceSelectionDialog;
import com.daalitoy.apps.keedoh.ui.table.data.EditableMessageTableModel;
import com.daalitoy.apps.keedoh.ui.util.Hex;
import com.daalitoy.apps.keedoh.ui.util.IconFactory;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class MessageInternalFrame extends JInternalFrame implements ActionListener {

    private static final Logger log = LogManager.getLogger(MessageInternalFrame.class);
    /** */
    private static final long serialVersionUID = 1L;
    private Message msg;
    private JTable requestMsgDataTable = null;
    private JTable responseMsgDataTable = null;
    private JToolBar toolbar = new JToolBar();

    public MessageInternalFrame(Message msg) {
        super("Specification: " + msg.getSpec().getSpecName() + " Message: " + msg.getMsgName());
        this.msg = msg;
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

        setLayout(new BorderLayout());
        // toolbar
        JButton socketButton = UIHelper.newButton(null, "__sock__", this);
        socketButton.setIcon(IconFactory.getIcon("client.png"));
        socketButton.setToolTipText("Socket Settings");

        // JButton listenerButton = UIHelper.newButton(null, "__list__", this);
        // listenerButton.setIcon(IconFactory.getIcon("server.png"));
        // listenerButton.setToolTipText("Listener Settings");

        JButton importButton = UIHelper.newButton(null, "__import__", this);
        importButton.setIcon(IconFactory.getIcon("import_icon.png"));
        importButton.setToolTipText("Import Trace");

        JButton fromFileButton = UIHelper.newButton(null, "__load_file__", this);
        fromFileButton.setIcon(IconFactory.getIcon("folder-horizontal-open.png"));
        fromFileButton.setToolTipText("Load Trace from File");

        JButton exportButton = UIHelper.newButton(null, "__export__", this);
        exportButton.setIcon(IconFactory.getIcon("save_icon.png"));
        exportButton.setToolTipText("Export Trace");
        JButton sendButton = UIHelper.newButton(null, "__send__", this);
        sendButton.setIcon(IconFactory.getIcon("send.png"));
        sendButton.setToolTipText("Fire Trace");

        JButton clearRequestButton = UIHelper.newButton(null, "__rq_clear__", this);
        clearRequestButton.setIcon(IconFactory.getIcon("ui-text-field-clear.png"));
        clearRequestButton.setToolTipText("Clear Request");

        JButton clearResponseButton = UIHelper.newButton(null, "__rp_clear__", this);
        clearResponseButton.setIcon(IconFactory.getIcon("ui-text-field-clear.png"));
        clearResponseButton.setToolTipText("Clear Response");

        toolbar.add(socketButton);
        // toolbar.add(listenerButton);

        toolbar.add(importButton);
        toolbar.add(fromFileButton);
        toolbar.add(exportButton);
        toolbar.addSeparator();
        toolbar.add(sendButton);
        toolbar.addSeparator();
        toolbar.add(clearRequestButton);
        toolbar.add(clearResponseButton);
        toolbar.setRollover(true);
        add(toolbar, BorderLayout.PAGE_START);

        requestMsgDataTable = new JTable();
        requestMsgDataTable.setModel(new EditableMessageTableModel(msg, true));
        requestMsgDataTable.setFont(UIHelper.STANDARD_FONT);
        requestMsgDataTable.getTableHeader().setFont(UIHelper.STANDARD_BOLD_FONT);

        responseMsgDataTable = new JTable();
        responseMsgDataTable.setModel(new EditableMessageTableModel(msg, false));
        responseMsgDataTable.setFont(UIHelper.STANDARD_FONT);
        responseMsgDataTable.getTableHeader().setFont(UIHelper.STANDARD_BOLD_FONT);

        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        pane.setDividerLocation(0.5);
        pane.setLeftComponent(new JScrollPane(requestMsgDataTable));
        pane.setRightComponent(new JScrollPane(responseMsgDataTable));

        add(new JPanel(), BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.WEST);
        add(pane, BorderLayout.CENTER);
        add(new JPanel(), BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand().equals("__sock__")) {
            ConnectorConfig config = new ConnectorSelectionDialog(this).showDialog();
            if (config != null) {
                msg.setConnectorConfig(config);
            }
        } else if (arg0.getActionCommand().equals("__import__")) {
            String trace = new TraceInputDialog(this).getTrace();
            if (trace != null) {
                MessageData msgData = MessageData.build(msg, trace, true);
                requestMsgDataTable.setModel(new EditableMessageTableModel(msgData));
            }
        } else if (arg0.getActionCommand().equals("__export__")) {
            File file = UIHelper.getUserSelectedFile(this, FILE_TYPE.TEXT, false);
            if (file != null) {
                MessageData msgData =
                        ((EditableMessageTableModel) requestMsgDataTable.getModel()).getMsgData();
                try {
                    byte[] assembledData = msgData.assemble(true);
                    Files.write(Hex.toString(assembledData), file, Charsets.US_ASCII);
                } catch (Exception e) {
                    UIHelper.showErrorDialog(this, "Failed to Export Trace", e);
                }
            }

        } else if (arg0.getActionCommand().equals("__send__")) {
            try {
                MessageData rpMsgData =
                        MessagingManager.getInstance()
                                .dispatch(
                                        ((EditableMessageTableModel) requestMsgDataTable.getModel()).getMsgData());
                EditableMessageTableModel model = new EditableMessageTableModel(rpMsgData);
                responseMsgDataTable.setModel(model);
                model.fireTableDataChanged();

            } catch (KeedohMessageTimeoutException e) {
                UIHelper.showErrorDialog(this, "Message Timed Out", e);
            }
        } else if (arg0.getActionCommand().equals("__rq_clear__")) {
            requestMsgDataTable.setModel(new EditableMessageTableModel(msg, true));
        } else if (arg0.getActionCommand().equals("__rp_clear__")) {
            responseMsgDataTable.setModel(new EditableMessageTableModel(msg, true));
        } else if (arg0.getActionCommand().equals("__load_file__")) {
            File file = UIHelper.getUserSelectedFile(this, FILE_TYPE.TEXT, true);
            if (file != null) {
                try {
                    List<String> traces = Files.readLines(file, Charsets.US_ASCII);
                    String tmp = new TraceSelectionDialog(this, traces).getTrace();
                    if (tmp != null) {
                        MessageData msgData = MessageData.build(msg, tmp, true);
                        requestMsgDataTable.setModel(new EditableMessageTableModel(msgData));
                    }
                } catch (Exception e) {
                    log.error("Failed to load traces", e);
                    UIHelper.showErrorDialog(this, "unable to load traces from file", e);
                }
            }
        }
    }
}
