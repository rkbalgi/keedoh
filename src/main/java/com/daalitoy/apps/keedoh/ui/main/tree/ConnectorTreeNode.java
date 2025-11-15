package com.daalitoy.apps.keedoh.ui.main.tree;

import com.daalitoy.apps.keedoh.data.model.ConnectorConfig;
import com.daalitoy.apps.keedoh.data.providers.ConnectorConfigProvider;
import com.daalitoy.apps.keedoh.net.client.Connectors;
import com.daalitoy.apps.keedoh.ui.CanPopUp;
import com.daalitoy.apps.keedoh.ui.dialog.ConnectorConfigDialog;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JPopupMenu;

public class ConnectorTreeNode extends KeedohMutableTreeNode implements CanPopUp, ActionListener {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private final JPopupMenu pMenu = new JPopupMenu();
  private ConnectorConfigProvider provider;

  public ConnectorTreeNode(ConnectorConfig config, ConnectorConfigProvider provider) {
    super(config);
    setupPopupMenu();
    this.provider = provider;
  }

  private void setupPopupMenu() {
    pMenu.setFont(UIHelper.STANDARD_FONT);
    pMenu.add(UIHelper.newJMenuItem("Edit", KeyEvent.VK_E, this, "__edit__"));
    pMenu.addSeparator();
    pMenu.add(UIHelper.newJMenuItem("Connect", KeyEvent.VK_C, this, "__connect__"));
    pMenu.add(UIHelper.newJMenuItem("Disconnect", KeyEvent.VK_D, this, "__disconnect__"));
  }

  @Override
  public JPopupMenu getPopUp() {
    return (pMenu);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (arg0.getActionCommand().equals("__edit__")) {
      ConnectorConfig config =
          new ConnectorConfigDialog(getOwningTree(), (ConnectorConfig) getUserObject())
              .showDialog();
      provider.update(config);
    } else if (arg0.getActionCommand().equals("__connect__")) {
      Connectors.connect((ConnectorConfig) getUserObject());
    } else if (arg0.getActionCommand().equals("__disconnect__")) {
      Connectors.disconnect((ConnectorConfig) getUserObject());
    }
  }
}
