package com.daalitoy.apps.keedoh.data.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.daalitoy.apps.keedoh.db.mahout.Mahout;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MessageFragment extends Model {

	private List<Field> fields = Lists.newArrayList();
	private Map<String, Field> fieldMap = Maps.newHashMap();

	private int fragmentId;
	private String fragmentName;

	public MessageFragment() {
	}

	public int getFragmentId() {
		return fragmentId;
	}

	public void setFragmentId(int fragmentId) {
		this.fragmentId = fragmentId;
	}

	public String getFragmentName() {
		return fragmentName;
	}

	public void setFragmentName(String fragmentName) {
		this.fragmentName = fragmentName;
	}

	public void add(Field field) {
		fields.add(field);
		fieldMap.put(field.getFieldName(), field);
	}

	public boolean contains(Field field) {
		if (fields.contains(field)) {
			return (true);
		} else {
			return (false);
		}
	}

	public void remove(Field field) {
		fields.remove(field);

	}

	public List<Field> getFields() {
		return fields;
	}

	public static MessageFragment fromDb(Spec spec, int fragId) {
		try {
			String sql = "select * from msg_frag where msg_frag_id=? order by msg_frag_fld_order";
			PreparedStatement st = Mahout.getConnection().prepareStatement(sql);
			st.setInt(1, fragId);
			ResultSet rs = st.executeQuery();
			MessageFragment fragment = new MessageFragment();
			fragment.setFragmentId(fragId);
			while (rs.next()) {
				fragment.setFragmentName(rs.getString("MSG_FRAG_NAME"));
				fragment.add(spec.getFieldById(rs.getInt("MSG_FRAG_FLD_ID")));
			}
			return (fragment);
		} catch (Exception e) {
			throw new KeedohModelException(e);
		}

	}

	public void swapFields(int a, int b) {

		Field fieldA = fields.get(a);
		fields.set(a, fields.get(b));
		fields.set(b, fieldA);

	}

	public Field getFieldByName(String fieldName) {
		return (fieldMap.get(fieldName));

	}

}
