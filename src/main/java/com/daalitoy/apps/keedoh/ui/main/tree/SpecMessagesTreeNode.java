package com.daalitoy.apps.keedoh.ui.main.tree;

import com.daalitoy.apps.keedoh.data.model.Message;

import com.daalitoy.apps.keedoh.system.SpecManager;
import com.daalitoy.apps.keedoh.ui.tree.nodes.KeedohMutableTreeNode;
import com.google.inject.Inject;
import io.github.rkbalgi.iso4k.MessageSegment;
import io.github.rkbalgi.iso4k.Spec;

import java.util.List;

public class SpecMessagesTreeNode extends KeedohMutableTreeNode {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Spec spec;
    @Inject
    private SpecManager specManager;

    public SpecMessagesTreeNode(Spec spec) {
        super("Messages");
        this.spec = spec;
    }

    public void init() {
        List<MessageSegment> messages = spec.messages();
        for (MessageSegment msg : messages) {
            SpecMessageTreeNode msgNode = new SpecMessageTreeNode(msg);
            addToSelf(msgNode);
            msgNode.init();
        }
        setUserObject("Messages (" + messages.size() + ")");
        refresh();

    }
}
