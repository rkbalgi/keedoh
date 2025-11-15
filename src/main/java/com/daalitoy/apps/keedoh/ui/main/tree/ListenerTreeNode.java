package com.daalitoy.apps.keedoh.ui.main.tree;

import com.daalitoy.apps.keedoh.data.model.ListenerConfig;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.data.providers.ListenerConfigProvider;
import com.daalitoy.apps.keedoh.guice.GuiceInjector;
import com.daalitoy.apps.keedoh.net.server.Listeners;
import com.daalitoy.apps.keedoh.ui.CanPopUp;
import com.daalitoy.apps.keedoh.ui.dialog.ListenerConfigDialog;
import com.daalitoy.apps.keedoh.ui.dialog.SpecSelectionDialog;
import com.daalitoy.apps.keedoh.ui.frames.KeedohMainFrame;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JPopupMenu;

public class ListenerTreeNode extends KeedohMutableTreeNode implements CanPopUp, ActionListener {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private final JPopupMenu pMenu = new JPopupMenu();
  private final ListenerConfigProvider provider;

  public ListenerTreeNode(ListenerConfig config, ListenerConfigProvider provider) {
    super(config);
    setupPopupMenu();
    this.provider = provider;
  }

  private void setupPopupMenu() {
    pMenu.setFont(UIHelper.STANDARD_FONT);
    pMenu.add(UIHelper.newJMenuItem("Edit", KeyEvent.VK_E, this, "__edit__"));
    pMenu.add(UIHelper.newJMenuItem("Start", KeyEvent.VK_S, this, "__start__"));
    pMenu.add(UIHelper.newJMenuItem("Stop", KeyEvent.VK_P, this, "__shutdown__"));
  }

  @Override
  public JPopupMenu getPopUp() {
    return (pMenu);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (arg0.getActionCommand().equals("__edit__")) {
      ListenerConfig config =
          new ListenerConfigDialog(
                  KeedohMainFrame.getDesktopPane(), (ListenerConfig) getUserObject())
              .showDialog();
      provider.update(config);
    } else if (arg0.getActionCommand().equals("__start__")) {
      // TODO::
      final SpecSelectionDialog dialog = new SpecSelectionDialog(KeedohMainFrame.getDesktopPane());
      GuiceInjector.getInjector().injectMembers(dialog);
      Spec spec = dialog.showDialog();
      if (spec != null) {
        Listeners.register((ListenerConfig) getUserObject(), spec);
        Listeners.start((ListenerConfig) getUserObject());
      }
    } else if (arg0.getActionCommand().equals("__shutdown__")) {
      Listeners.stop((ListenerConfig) getUserObject());
    }
  }
}
