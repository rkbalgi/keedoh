package com.daalitoy.apps.keedoh.ui.tree.nodes;

import com.daalitoy.apps.keedoh.data.model.Message;
import com.daalitoy.apps.keedoh.ui.CanPopUp;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JPopupMenu;

public class MessageTreeNode extends KeedohMutableTreeNode implements CanPopUp, ActionListener {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private JPopupMenu popupMenu = new JPopupMenu();
  private Message msg;

  public MessageTreeNode(Message msg) {
    super(msg.getMsgName());
    this.msg = msg;
    setupPopupMenu();
  }

  public void init() {
    // add children
    RequestFragmentTreeNode rqFragNode = new RequestFragmentTreeNode(msg.getRequestSegment());
    addToSelf(rqFragNode);
    rqFragNode.init();
    ResponseFragmentTreeNode rpFragNode = new ResponseFragmentTreeNode(msg.getResponseSegment());
    addToSelf(rpFragNode);
    rpFragNode.init();
  }

  private void setupPopupMenu() {
    popupMenu.add(UIHelper.newJMenuItem("Save", KeyEvent.VK_S, this, "__save__"));
    popupMenu.add(UIHelper.newJMenuItem("Delete", KeyEvent.VK_D, this, "__delete__"));
  }

  @Override
  public JPopupMenu getPopUp() {
    return (popupMenu);
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    if (arg0.getActionCommand().equals("__save__")) {
      // Message.save(msg);
    } else if (arg0.getActionCommand().equals("__delete__")) {
      // Message.delete(msg);
      removeSelf();
    }
  }
}
