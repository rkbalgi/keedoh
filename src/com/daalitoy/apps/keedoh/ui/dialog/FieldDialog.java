package com.daalitoy.apps.keedoh.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.daalitoy.apps.keedoh.data.common.BMP_FIELD_TYPE;
import com.daalitoy.apps.keedoh.data.common.ENCODING_TYPE;
import com.daalitoy.apps.keedoh.data.common.FIELD_TYPE;
import com.daalitoy.apps.keedoh.data.model.BitmappedField;
import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.model.FixedField;
import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.data.model.TerminatedField;
import com.daalitoy.apps.keedoh.data.model.VariableField;
import com.daalitoy.apps.keedoh.ui.util.UIHelper;

public class FieldDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean edit = false;

	private JTextField tfFieldName = UIHelper.newTextField(20);
	private JTextField tfFieldLength = UIHelper.newTextField(5);
	private JTextField tfIndicatorLength = UIHelper.newTextField(5);
	private JTextField tfFieldSequence = UIHelper.newTextField(5);
	private JTextField tfTerminatorCharacter = UIHelper.newTextField(2);

	private JComboBox cbFieldType = UIHelper.newComboBox();
	private JComboBox cbBmpFieldEncoding = UIHelper.newComboBox();
	private JComboBox cbBmpFieldType = UIHelper.newComboBox();
	private JComboBox cbFixedFieldEncoding = UIHelper.newComboBox();
	private JComboBox cbVarFieldEncoding = UIHelper.newComboBox();
	private JComboBox cbTerminatedFieldEncoding = UIHelper.newComboBox();

	private JComboBox cbIndicatorEncoding = UIHelper.newComboBox();
	private JComboBox cbParent = UIHelper.newComboBox();

	private JCheckBox cxMti = UIHelper.newCheckBox("MTI");
	private JCheckBox cxFlightKey = UIHelper.newCheckBox("Flight Key");

	private JButton btnOk = UIHelper.newButton("OK", "__ok__", this);
	private JButton btnCancel = UIHelper
			.newButton("Cancel", "__cancel__", this);
	private Component owner;

	private Field field;
	private Spec spec;
	private int responseType;

	public FieldDialog(Spec spec, JComponent parent) {
		this.owner = parent;
		this.spec = spec;
		initComponents();
		setTitle("New Field Dialog");
		setIconImage(UIHelper.getAppImage());
	}

	public FieldDialog(Field field, JComponent parent) {
		this.owner = parent;
		this.field = field;
		this.spec = field.getSpec();
		edit = true;
		initComponents();
		setTitle("Edit Field Dialog");
		// override with
		setFieldData();
		setIconImage(UIHelper.getAppImage());
	}

	private void setFieldData() {

		cbFieldType.setSelectedItem(field.getFieldType());
		cbFieldType.setEnabled(false);
		tfFieldName.setText(field.getFieldName());
		tfFieldSequence.setText(Integer.toString(field.getSequence()));

		if (field.isMti()) {
			cxMti.setSelected(true);
		} else {
			cxMti.setSelected(false);
		}
		if (field.isFlightKey()) {
			cxFlightKey.setSelected(true);
		} else {
			cxFlightKey.setSelected(false);
		}

		if (field.getParent() != null) {
			cbParent.setSelectedItem(field.getParent());
		} else {
			cbParent.setSelectedItem("NONE");
		}

		switch (field.getFieldType()) {
		case FIXED: {
			tfFieldLength.setText(Integer.toString(field.getLength()));
			cbFixedFieldEncoding.setSelectedItem(field.getEncodingType());
			break;
		}
		case VARIABLE: {

			VariableField vField = (VariableField) field;
			tfIndicatorLength.setText(Integer.toString(vField
					.getIndicatorLength()));
			cbIndicatorEncoding.setSelectedItem(vField
					.getIndicatorEncodingType());
			cbVarFieldEncoding.setSelectedItem(field.getEncodingType());
			break;
		}
		case TERMINATED: {
			tfTerminatorCharacter.setText(Integer
					.toHexString(((TerminatedField) field).getTerminator()));
			cbTerminatedFieldEncoding.setSelectedItem(field.getEncodingType());
			break;
		}
		case BITMAPPED: {
			cbBmpFieldEncoding.setSelectedItem(field.getEncodingType());
			cbBmpFieldType.setSelectedItem(((BitmappedField) field)
					.getBmpFieldType());
			break;
		}

		}

	}

	private void initComponents() {

		populateStaticData();

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		final JTabbedPane pane = new JTabbedPane();
		pane.setFont(UIHelper.STANDARD_FONT);

		pane.addTab("Basic", createBasicPanel());
		pane.addTab("Bitmapped", createBitmappedPanel());
		pane.addTab("Fixed", createFixedPanel());
		pane.addTab("Terminated", createTerminatedPanel());
		pane.addTab("Variable", createVariablePanel());

		pane.setEnabledAt(0, true);
		pane.setEnabledAt(1, true);
		pane.setEnabledAt(2, false);
		pane.setEnabledAt(3, false);
		pane.setEnabledAt(4, false);

		JPanel bPanel = new JPanel();
		bPanel.add(btnOk);
		bPanel.add(btnCancel);

		panel.add(pane, BorderLayout.CENTER);
		panel.add(bPanel, BorderLayout.SOUTH);

		add(panel);

		cbFieldType.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {

				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					FIELD_TYPE fieldType = (FIELD_TYPE) arg0.getItem();
					switch (fieldType) {
					case BITMAPPED:
						pane.setEnabledAt(1, true);
						pane.setEnabledAt(2, false);
						pane.setEnabledAt(3, false);
						pane.setEnabledAt(4, false);
						break;
					case FIXED:
						pane.setEnabledAt(1, false);
						pane.setEnabledAt(2, true);
						pane.setEnabledAt(3, false);
						pane.setEnabledAt(4, false);
						break;
					case TERMINATED:
						pane.setEnabledAt(1, false);
						pane.setEnabledAt(2, false);
						pane.setEnabledAt(3, true);
						pane.setEnabledAt(4, false);
						break;
					case VARIABLE:
						pane.setEnabledAt(1, false);
						pane.setEnabledAt(2, false);
						pane.setEnabledAt(3, false);
						pane.setEnabledAt(4, true);
						break;

					}
				}

			}

		});

		setLocation(owner.getX() + 200, owner.getY() + 200);

	}

	private Component createBitmappedPanel() {
		JPanel container = new JPanel();

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));

		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 3));
		panel1.add(UIHelper.newLabel("Field Encoding"));
		panel1.add(new JLabel(""));
		panel1.add(cbBmpFieldEncoding);
		panel.add(panel1);

		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 3));
		panel1.add(UIHelper.newLabel("Bitmap Type"));
		panel1.add(new JLabel(""));
		panel1.add(cbBmpFieldType);
		panel.add(panel1);

		container.add(panel);
		return (container);
	}

	private Component createTerminatedPanel() {
		JPanel container = new JPanel();
		// container.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));

		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 3));
		panel1.add(UIHelper.newLabel("Field Encoding"));
		panel1.add(new JLabel(""));
		panel1.add(cbTerminatedFieldEncoding);
		panel.add(panel1);

		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 3));
		panel1.add(UIHelper.newLabel("Terminator Character"));
		panel1.add(new JLabel(""));
		panel1.add(tfTerminatorCharacter);
		panel.add(panel1);

		container.add(panel);
		return (container);
	}

	private Component createVariablePanel() {
		JPanel container = new JPanel();
		// container.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1));

		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 3));
		panel1.add(UIHelper.newLabel("Field Encoding"));
		panel1.add(new JLabel(""));
		panel1.add(cbVarFieldEncoding);
		panel.add(panel1);

		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 2));
		panel1.add(UIHelper.newLabel("LL Length"));
		panel1.add(new JLabel(""));
		panel1.add(tfIndicatorLength);
		panel.add(panel1);

		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 2));
		panel1.add(UIHelper.newLabel("LL Encoding"));
		panel1.add(new JLabel(""));
		panel1.add(cbIndicatorEncoding);
		panel.add(panel1);

		container.add(panel);
		return (container);
	}

	private Component createFixedPanel() {

		JPanel container = new JPanel();
		// container.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));

		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 3));
		panel1.add(UIHelper.newLabel("Field Length"));
		panel1.add(new JLabel(""));
		panel1.add(tfFieldLength);
		panel.add(panel1);

		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 3));
		panel1.add(UIHelper.newLabel("Field Encoding"));
		panel1.add(new JLabel(""));
		panel1.add(cbFixedFieldEncoding);
		panel.add(panel1);

		container.add(panel);
		return (container);

	}

	private Component createBasicPanel() {

		JPanel container = new JPanel();

		container.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 1));

		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 2));
		panel1.add(UIHelper.newLabel("Field Type"));
		panel1.add(cbFieldType);
		panel.add(panel1);

		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 2));
		panel1.add(UIHelper.newLabel("Field Name"));
		panel1.add(tfFieldName);
		panel.add(panel1);

		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 2));
		panel1.add(UIHelper.newLabel("Parent"));
		panel1.add(cbParent);
		panel.add(panel1);

		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 2));
		panel1.add(UIHelper.newLabel("Field Sequence"));
		panel1.add(tfFieldSequence);
		panel.add(panel1);

		panel1 = new JPanel();
		panel1.add(cxMti);
		panel1.add(cxFlightKey);

		panel.add(panel1);

		container.add(panel);
		return (container);

	}

	public int showDialog() {
		setSize(450, 220);
		setModal(true);
		setVisible(true);
		return (responseType);
	}

	public Field getField() {
		return (field);
	}

	private void populateStaticData() {
		for (FIELD_TYPE fieldType : FIELD_TYPE.values()) {
			cbFieldType.addItem(fieldType);
		}

		for (ENCODING_TYPE encodingType : ENCODING_TYPE.values()) {
			cbFixedFieldEncoding.addItem(encodingType);
			cbVarFieldEncoding.addItem(encodingType);
			cbTerminatedFieldEncoding.addItem(encodingType);
			cbIndicatorEncoding.addItem(encodingType);
			cbBmpFieldEncoding.addItem(encodingType);

		}
		for (BMP_FIELD_TYPE bmpFieldType : BMP_FIELD_TYPE.values()) {
			cbBmpFieldType.addItem(bmpFieldType);
		}

		cbParent.addItem("NONE");
		for (Field f : spec.getFields()) {
			cbParent.addItem(f);
		}

		tfFieldLength.setText("0");
		tfIndicatorLength.setText("0");
		tfFieldSequence.setText("-1");
		tfTerminatorCharacter.setText("1C");

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("__ok__")) {
			responseType = 0;
			if (!edit) {
				createField();
			} else {
				createField();
				updateField();
			}
			dispose();
		} else {
			// cancel
			responseType = 1;
			field = null;
			dispose();
		}

	}

	private void updateField() {
		Field.update(field);

	}

	private void createField() {

		FIELD_TYPE fieldType = (FIELD_TYPE) cbFieldType.getSelectedItem();

		switch (fieldType) {
		case FIXED: {
			createFixedField();
			break;
		}
		case VARIABLE: {
			createVariableField();
			break;
		}
		case TERMINATED: {
			createTerminatedField();
			break;
		}
		case BITMAPPED: {
			createBitmappedField();
			break;
		}

		default:
			throw new UnsupportedOperationException(fieldType
					+ " not supported");
		}

	}

	private void createBitmappedField() {
		BitmappedField field = null;
		if (!edit)
			field = new BitmappedField();
		else
			field = (BitmappedField) this.field;

		field.setFieldName(tfFieldName.getText());
		field.setFieldType(FIELD_TYPE.BITMAPPED);
		field.setFlightKey(cxFlightKey.isSelected());
		field.setMti(cxMti.isSelected());
		field.setBmpFieldType((BMP_FIELD_TYPE) cbBmpFieldType.getSelectedItem());
		field.setEncodingType((ENCODING_TYPE) cbBmpFieldEncoding
				.getSelectedItem());

		field.setSequence(Integer.parseInt(tfFieldSequence.getText()));

		if (cbParent.getSelectedItem().equals("NONE")) {
			field.setParent(null);
		} else {
			field.setParent(((Field) cbParent.getSelectedItem()));
		}
		this.field = field;

	}

	private void createTerminatedField() {
		TerminatedField field = null;
		if (!edit)
			field = new TerminatedField();
		else
			field = (TerminatedField) this.field;

		field.setFieldName(tfFieldName.getText());
		field.setFieldType(FIELD_TYPE.TERMINATED);
		field.setFlightKey(cxFlightKey.isSelected());
		field.setMti(cxMti.isSelected());
		field.setTerminator((byte) Integer.parseInt(
				tfTerminatorCharacter.getText(), 16));
		field.setEncodingType((ENCODING_TYPE) cbTerminatedFieldEncoding
				.getSelectedItem());

		field.setSequence(Integer.parseInt(tfFieldSequence.getText()));

		if (cbParent.getSelectedItem().equals("NONE")) {
			field.setParent(null);
		} else {
			field.setParent(((Field) cbParent.getSelectedItem()));
		}
		this.field = field;

	}

	private void createVariableField() {
		VariableField field = null;

		if (!edit)
			field = new VariableField();
		else
			field = (VariableField) this.field;

		field.setFieldName(tfFieldName.getText());
		field.setFieldType(FIELD_TYPE.VARIABLE);
		field.setFlightKey(cxFlightKey.isSelected());
		field.setMti(cxMti.isSelected());
		field.setSequence(Integer.parseInt(tfFieldSequence.getText()));
		field.setIndicatorLength(Integer.parseInt(tfIndicatorLength.getText()
				.trim()));
		field.setIndicatorEncodingType((ENCODING_TYPE) cbIndicatorEncoding
				.getSelectedItem());

		field.setEncodingType((ENCODING_TYPE) cbVarFieldEncoding
				.getSelectedItem());

		if (cbParent.getSelectedItem().equals("NONE")) {
			field.setParent(null);
		} else {
			field.setParent(((Field) cbParent.getSelectedItem()));
		}
		this.field = field;

	}

	private void createFixedField() {

		FixedField field = null;
		if (!edit)
			field = new FixedField();
		else
			field = (FixedField) this.field;

		field.setFieldName(tfFieldName.getText());
		field.setFieldType(FIELD_TYPE.FIXED);
		field.setFlightKey(cxFlightKey.isSelected());
		field.setSequence(Integer.parseInt(tfFieldSequence.getText()));
		field.setMti(cxMti.isSelected());
		field.setLength(Integer.parseInt(tfFieldLength.getText().trim()));
		field.setEncodingType((ENCODING_TYPE) cbFixedFieldEncoding
				.getSelectedItem());
		if (cbParent.getSelectedItem().equals("NONE")) {
			field.setParent(null);
		} else {
			field.setParent(((Field) cbParent.getSelectedItem()));
		}
		this.field = field;
	}
}
