package com.daalitoy.apps.keedoh.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.daalitoy.apps.keedoh.ui.util.UIHelper;

public class TraceSelectionDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList lstTraces = null;
	private String trace;

	public TraceSelectionDialog(JInternalFrame owner, List<String> traces) {

		Vector v = new Vector();
		v.addAll(traces);
		lstTraces = UIHelper.newList(v);

		setTitle("Select Trace");
		setSize(400, 200);
		initComponents();
		setModal(true);
		setIconImage(UIHelper.getAppImage());
		setLocationRelativeTo(owner);

	}

	private void initComponents() {

		setLayout(new BorderLayout());

		JButton okButton = UIHelper.newButton("OK", "__ok__", this);
		JButton cancelButton = UIHelper.newButton("Cancel", "__cancel__", this);
		JPanel panel = new JPanel();
		panel.add(okButton);
		panel.add(cancelButton);

		add(new JScrollPane(lstTraces), BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);
		add(new JPanel(), BorderLayout.NORTH);
		add(new JPanel(), BorderLayout.EAST);
		add(new JPanel(), BorderLayout.WEST);

	}

	public String getTrace() {
		setVisible(true);
		return (trace);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("__ok__")) {
			String tmp = (String) lstTraces.getSelectedValue();
			if (tmp != null && tmp.trim().length() > 0) {
				trace = tmp.trim();
			} else {
				trace = null;
			}
		} else {
			trace = null;
		}
		dispose();

	}
}
