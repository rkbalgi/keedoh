package com.daalitoy.apps.keedoh.ui.frames.internal;

import com.daalitoy.apps.keedoh.data.common.FILE_TYPE;
import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.data.model.Message;
import com.daalitoy.apps.keedoh.data.transit.MessageData;
import com.daalitoy.apps.keedoh.guice.GuiceInjector;
import com.daalitoy.apps.keedoh.messaging.KeedohMessageTimeoutException;
import com.daalitoy.apps.keedoh.messaging.MessagingManager;
import com.daalitoy.apps.keedoh.net.KeedohNetException;
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
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Message msg;
    private JTable requestMsgDataTable = null;
    private final JToolBar toolbar = new JToolBar();

    public MessageInternalFrame(Message msg) {
        super("Specification: " + msg.getSpec().getSpecName() + " Message: " + msg.getMsgName());
        this.msg = msg;
        initComponents();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
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
        socketButton.setToolTipText("Connector Settings");

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
        clearRequestButton.setIcon(IconFactory.getIcon("eraser.png"));
        clearRequestButton.setToolTipText("Clear Request");

        toolbar.add(socketButton);
        toolbar.add(importButton);
        toolbar.add(fromFileButton);
        toolbar.add(exportButton);
        toolbar.addSeparator();
        toolbar.add(sendButton);
        toolbar.addSeparator();
        toolbar.add(clearRequestButton);
        toolbar.setRollover(true);
        add(toolbar, BorderLayout.PAGE_START);

        requestMsgDataTable = new JTable();
        requestMsgDataTable.setModel(new EditableMessageTableModel(msg, true));
        requestMsgDataTable.setFont(UIHelper.STANDARD_FONT);
        requestMsgDataTable.getTableHeader().setFont(UIHelper.STANDARD_BOLD_FONT);


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JPanel tmpPanel = new JPanel();
        tmpPanel.setBackground(Color.GREEN);
        JLabel label = UIHelper.newLabel(" REQUEST  ");
        label.setBackground(Color.GREEN);

        tmpPanel.add(label);
        topPanel.add(tmpPanel, BorderLayout.NORTH);
        topPanel.add(new JScrollPane(requestMsgDataTable), BorderLayout.CENTER);

        topPanel.setMinimumSize(new Dimension(200, 200));

        add(topPanel, BorderLayout.CENTER);

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        switch (arg0.getActionCommand()) {
            case "__sock__":
                final ConnectorSelectionDialog dialog = new ConnectorSelectionDialog(this);
                GuiceInjector.getInjector().injectMembers(dialog);
                ConnectorConfig config = dialog.showDialog();
                if (config != null) {
                    msg.setConnectorConfig(config);
                }
                break;
            case "__import__":
                String trace = new TraceInputDialog(this).getTrace();
                if (trace != null) {
                    MessageData msgData = MessageData.build(msg, trace, true);
                    requestMsgDataTable.setModel(new EditableMessageTableModel(msgData));
                }
                break;
            case "__export__": {
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

                break;
            }
            case "__send__":
                try {
                    MessageData msgData =
                            ((EditableMessageTableModel) requestMsgDataTable.getModel()).getMsgData();
                    if (msgData.getMessage().getConnectorConfig() == null) {
                        UIHelper.showErrorDialog(this, "Please select a connector settings");
                        return;
                    }

                    MessageData rpMsgData = MessagingManager.getInstance().dispatch(msgData);
                    new ResponseMsgDialog(this, rpMsgData).showDialog();

                } catch (KeedohMessageTimeoutException e) {
                    UIHelper.showErrorDialog(this, "Message Timed Out", e);
                } catch (KeedohNetException e) {
                    UIHelper.showErrorDialog(this, "Network Error", e);
                }
                break;
            case "__rq_clear__":
                requestMsgDataTable.setModel(new EditableMessageTableModel(msg, true));
                break;
            case "__load_file__": {
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
                break;
            }
        }
    }
}
