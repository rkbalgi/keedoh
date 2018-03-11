package com.daalitoy.apps.keedoh.ui.tree.nodes;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.MessageSegment;
import com.daalitoy.apps.keedoh.ui.util.KeedohConstants;

public class RequestFragmentTreeNode extends KeedohMutableTreeNode {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private MessageSegment fragment;

    public RequestFragmentTreeNode(MessageSegment fragment) {
        this.fragment = fragment;
    }

    public void init() {
        for (Field field : fragment.getFields()) {
            MessageFieldTreeNode node = new MessageFieldTreeNode(fragment,
                    field);
            addToSelf(node);
            node.init();
        }
    }

    public MessageSegment getFragment() {
        return (fragment);
    }

    public boolean contains(Field field) {
        return fragment.contains(field);
    }

    public String toString() {
        return (KeedohConstants.REQUEST_FRAGMENT);
    }

}
