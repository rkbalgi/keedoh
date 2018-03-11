package com.daalitoy.apps.keedoh.data.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class MessageSegment extends Model {

    private List<Field> fields = Lists.newArrayList();
    private Map<String, Field> fieldMap = Maps.newHashMap();

    private String segmentName;
    private List<String> mti;

    public MessageSegment() {
    }

    private void wireField(Field field) {

        add(field);
        if (field.children != null && field.children.size() > 0) {
            field.children.forEach(
                    child -> {
                        fieldMap.put(child.fieldName, child);
                        child.setParent(field);
                    });
        }
    }

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    public void add(Field field) {
        fields.add(field);
        fieldMap.put(field.getFieldName(), field);
    }

    public boolean contains(Field field) {
        return fields.contains(field);
    }

    public void remove(Field field) {
        fields.remove(field);
    }

    public List<Field> getFields() {
        return fields;
    }

    @JsonSetter
    public void setFields(List<Field> fields) {
        fields.forEach(
                f -> wireField(f));
    }

  /*	public static MessageSegment fromDb(Spec spec, int fragId) {
  	try {
  		String sql = "select * from msg_frag where msg_frag_id=? order by msg_frag_fld_order";
  		PreparedStatement st = Mahout.getConnection().prepareStatement(sql);
  		st.setInt(1, fragId);
  		ResultSet rs = st.executeQuery();
  		MessageSegment fragment = new MessageSegment();
  		fragment.setFragmentId(fragId);
  		while (rs.next()) {
  			fragment.setSegmentName(rs.getString("MSG_FRAG_NAME"));
  			fragment.add(spec.getFieldById(rs.getInt("MSG_FRAG_FLD_ID")));
  		}
  		return (fragment);
  	} catch (Exception e) {
  		throw new KeedohModelException(e);
  	}

  }*/

    public void swapFields(int a, int b) {

        Field fieldA = fields.get(a);
        fields.set(a, fields.get(b));
        fields.set(b, fieldA);
    }

    public Field getFieldByName(String fieldName) {
        return (fieldMap.get(fieldName));
    }

    public List<String> getMti() {
        return mti;
    }

    public void setMti(List<String> mti) {
        this.mti = mti;
    }
}
