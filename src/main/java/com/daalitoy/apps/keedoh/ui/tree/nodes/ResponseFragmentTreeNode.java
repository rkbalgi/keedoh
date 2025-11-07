package com.daalitoy.apps.keedoh.ui.tree.nodes;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.MessageSegment;
import com.daalitoy.apps.keedoh.ui.util.KeedohConstants;

public class ResponseFragmentTreeNode extends KeedohMutableTreeNode {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private MessageSegment fragment;

  public ResponseFragmentTreeNode(MessageSegment fragment) {
    this.fragment = fragment;
  }

  public MessageSegment getFragment() {
    return (fragment);
  }

  public void init() {
    for (Field field : fragment.getFields()) {
      MessageFieldTreeNode node = new MessageFieldTreeNode(fragment, field);
      addToSelf(node);
      node.init();
    }
  }

  public boolean contains(Field field) {
    return fragment.contains(field);
  }

  public String toString() {
    return (KeedohConstants.RESPONSE_FRAGMENT);
  }
}
