package com.daalitoy.apps.keedoh.data.model;

import com.daalitoy.apps.keedoh.data.common.ENCODING_TYPE;
import com.daalitoy.apps.keedoh.data.common.FIELD_TYPE;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "fieldType")
@JsonTypeIdResolver(FieldTypeResolver.class)
/*@JsonSubTypes({
        @JsonSubTypes.Type(value=FixedField.class,name="FIXED"),
        @JsonSubTypes.Type(value=VariableField.class,name="VARIABLE"),
        @JsonSubTypes.Type(value=BitmappedField.class,name="BITMAPPED"),
        @JsonSubTypes.Type(value=TerminatedField.class,name="TERMINATED"),

})*/
public abstract class Field extends Model {

    /*private static String FIXED_FLD_UPDATE_QUERY = "update fld set fld_name=?,fld_size=?,fld_encoding=?,fld_seq=?,fld_parent_id=?,is_mti=?,is_flight_key=? where fld_id=? and msg_spec_id=?";
        private static String VARIABLE_FLD_UPDATE_QUERY = "update fld set fld_name=?,fld_len_ind_size=?,fld_encoding=?,fld_len_ind_encoding=?,fld_seq=?,fld_parent_id=?,is_mti=?,is_flight_key=? where fld_id=? and msg_spec_id=?";
        private static String TERMINATED_FLD_UPDATE_QUERY = "update fld set fld_name=?,fld_encoding=?,terminator_char=?,fld_seq=?,fld_parent_id=?,is_mti=?,is_flight_key=? where fld_id=? and msg_spec_id=?";
    */
    protected int fieldId;
    protected String fieldName;
    protected FIELD_TYPE fieldType;
    protected int length;
    protected ENCODING_TYPE encodingType;
    protected boolean isMti;
    protected boolean isFlightKey;
    protected Field parent;
    protected int sequence;

    protected List<Field> children = Lists.newArrayList();
    protected Map<Integer, Field> childrenBySequenceMap = Maps.newHashMap();

    protected Spec spec;

    public Field() {
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public FIELD_TYPE getFieldType() {

        if (fieldType == null) {
            if (Predicates.instanceOf(FixedField.class).apply(this)) {
                fieldType = FIELD_TYPE.FIXED;
            }
            if (Predicates.instanceOf(VariableField.class).apply(this)) {
                fieldType = FIELD_TYPE.VARIABLE;
            }
            if (Predicates.instanceOf(BitmappedField.class).apply(this)) {
                fieldType = FIELD_TYPE.BITMAPPED;
            }
            if (Predicates.instanceOf(TerminatedField.class).apply(this)) {
                fieldType = FIELD_TYPE.TERMINATED;
            }
        }

        return fieldType;
    }

    public void setFieldType(FIELD_TYPE fieldType) {
        this.fieldType = fieldType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ENCODING_TYPE getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(ENCODING_TYPE encodingType) {
        this.encodingType = encodingType;
    }

    public boolean isMti() {
        return isMti;
    }

    public void setMti(boolean isMti) {
        this.isMti = isMti;
    }

    public boolean isFlightKey() {
        return isFlightKey;
    }

    public void setFlightKey(boolean isFlightKey) {
        this.isFlightKey = isFlightKey;
    }

    public Field getParent() {
        return parent;
    }

    public void setParent(Field parent) {
        this.parent = parent;
    }

    public String toString() {
        String label =
                (getSequence() != -1) ? "(" + getSequence() + ") " + getFieldName() : getFieldName();
        return (label);
    }

    public void addChild(Field f) {
        children.add(f);
        childrenBySequenceMap.put(f.getSequence(), f);
        Collections.sort(
                children,
                new Comparator<Field>() {

                    @Override
                    public int compare(Field field1, Field field2) {
                        return (field1.getSequence() - field2.getSequence());
                    }
                });
    }

    public Field getChild(int sequence) {
        return (childrenBySequenceMap.get(sequence));
    }

    public List<Field> getChildren() {
        return (children);
    }

  /*	public static void update(Field field) {

  	String sql = null;
  	PreparedStatement st = null;

  	try {
  		int index = 0;
  		if (field.getFieldType() == FIELD_TYPE.FIXED) {
  			sql = FIXED_FLD_UPDATE_QUERY;
  			st = Mahout.getConnection().prepareStatement(sql);
  			st.setInt(2, field.getLength());
  			st.setString(3, field.getEncodingType().toString());
  			index = 4;

  		} else if (field.getFieldType() == FIELD_TYPE.VARIABLE) {
  			sql = VARIABLE_FLD_UPDATE_QUERY;
  			// fld_len_ind_size=?,fld_encoding=?,fld_len_ind_encoding=?
  			st = Mahout.getConnection().prepareStatement(sql);
  			st.setInt(2, ((VariableField) field).getIndicatorLength());
  			st.setString(3, field.getEncodingType().toString());
  			st.setString(4, ((VariableField) field)
  					.getIndicatorEncodingType().toString());
  			index = 5;

  		} else if (field.getFieldType() == FIELD_TYPE.TERMINATED) {
  			sql = TERMINATED_FLD_UPDATE_QUERY;
  			// fld_name=?,fld_encoding=?,terminator_char=?
  			st = Mahout.getConnection().prepareStatement(sql);
  			st.setString(2, field.getEncodingType().toString());
  			st.setString(3, Integer.toHexString(((TerminatedField) field)
  					.getTerminator()));
  			index = 4;

  		} else {
  			throw new UnsupportedOperationException("unsupported field:"
  					+ field.getFieldType());
  		}

  		// common attributes
  		st.setString(1, field.getFieldName());
  		st.setInt(index, field.getSequence());
  		if (field.getParent() != null) {
  			st.setInt(index + 1, field.getParent().getFieldId());
  		} else {
  			st.setNull(index + 1, Types.INTEGER);
  		}

  		st.setString(index + 2, field.isMti() ? "Y" : "N");
  		st.setString(index + 3, field.isFlightKey() ? "Y" : "N");
  		st.setInt(index + 4, field.getFieldId());
  		st.setInt(index + 5, field.getSpec().getSpecId());

  		st.executeUpdate();

  	} catch (Exception e) {
  		throw new KeedohModelException(e);
  	} finally {
  		try {
  			st.close();
  		} catch (Exception e) {
  		}
  	}

  }

  public static void newField(Field field) throws MahoutException {

  	try {
  		Connection c = Mahout.getConnection();
  		String sql = "insert into fld values(NULL,?,?,?,?,?,?,?,?,?,?,?,?)";
  		PreparedStatement st = c.prepareCall(sql);
  		if (field instanceof FixedField) {
  			st.setInt(1, field.getSpec().getSpecId());
  			st.setString(2, field.getFieldName());
  			st.setString(3, field.getFieldType().toString());
  			st.setInt(4, field.getLength());
  			st.setString(5, field.getEncodingType().toString());
  			st.setNull(6, Types.NUMERIC);
  			st.setNull(7, Types.VARCHAR);
  			st.setNull(8, Types.VARCHAR);
  			st.setInt(9, field.getSequence());
  			if (field.getParent() != null)
  				st.setInt(10, field.getParent().getFieldId());
  			else
  				st.setNull(10, Types.NUMERIC);
  			st.setString(11, field.isMti() ? "Y" : "N");
  			st.setString(12, field.isFlightKey() ? "Y" : "N");

  		} else if (field instanceof VariableField) {
  			st.setInt(1, field.getSpec().getSpecId());
  			st.setString(2, field.getFieldName());
  			st.setString(3, field.getFieldType().toString());
  			st.setNull(4, Types.NUMERIC);
  			st.setString(5, field.getEncodingType().toString());
  			st.setInt(6, ((VariableField) field).getIndicatorLength());
  			st.setString(7, ((VariableField) field)
  					.getIndicatorEncodingType().toString());
  			st.setNull(8, Types.VARCHAR);
  			st.setInt(9, field.getSequence());
  			if (field.getParent() != null)
  				st.setInt(10, field.getParent().getFieldId());
  			else
  				st.setNull(10, Types.NUMERIC);
  			st.setString(11, field.isMti() ? "Y" : "N");
  			st.setString(12, field.isFlightKey() ? "Y" : "N");

  		} else if (field instanceof TerminatedField) {
  			st.setInt(1, field.getSpec().getSpecId());
  			st.setString(2, field.getFieldName());
  			st.setString(3, field.getFieldType().toString());
  			st.setInt(4, field.getLength());
  			st.setString(5, field.getEncodingType().toString());
  			st.setNull(6, Types.NUMERIC);
  			st.setNull(7, Types.VARCHAR);
  			st.setString(8, Integer.toHexString(((TerminatedField) field)
  					.getTerminator()));
  			st.setInt(9, field.getSequence());
  			if (field.getParent() != null)
  				st.setInt(10, field.getParent().getFieldId());
  			else
  				st.setNull(10, Types.NUMERIC);
  			st.setString(11, field.isMti() ? "Y" : "N");
  			st.setString(12, field.isFlightKey() ? "Y" : "N");

  		} else if (field instanceof BitmappedField) {
  			st.setInt(1, field.getSpec().getSpecId());
  			st.setString(2, field.getFieldName());
  			st.setString(3, field.getFieldType().toString());
  			if (((BitmappedField) field).getBmpFieldType() == BMP_FIELD_TYPE.PRIMARY) {
  				st.setInt(4, 8);
  			} else {
  				st.setInt(4, 16);
  			}
  			st.setString(5, field.getEncodingType().toString());
  			st.setNull(6, Types.NUMERIC);
  			st.setNull(7, Types.VARCHAR);
  			st.setNull(8, Types.VARCHAR);
  			st.setInt(9, field.getSequence());
  			if (field.getParent() != null)
  				st.setInt(10, field.getParent().getFieldId());
  			else
  				st.setNull(10, Types.NUMERIC);
  			st.setString(11, field.isMti() ? "Y" : "N");
  			st.setString(12, field.isFlightKey() ? "Y" : "N");

  		}

  		st.execute();
  		Statement cSt = c.createStatement();
  		ResultSet rs = cSt.executeQuery("call identity()");
  		rs.next();
  		int id = rs.getInt(1);
  		field.setFieldId(id);
  		rs.close();
  		st.close();
  		cSt.close();

  	} catch (Exception e) {
  		throw new MahoutException(e);
  	}

  }

  public static Field asObj(Spec spec, ResultSet rs) {

  	Field field = null;
  	try {
  		FIELD_TYPE fieldType = FIELD_TYPE.valueOf(rs.getString("FLD_TYPE")
  				.trim());

  		switch (fieldType) {
  		case FIXED: {
  			FixedField fixedField = new FixedField();
  			fixedField.setLength(rs.getInt("FLD_SIZE"));
  			fixedField.setEncodingType(ENCODING_TYPE.valueOf(rs.getString(
  					"FLD_ENCODING").trim()));

  			field = fixedField;
  			break;
  		}
  		case TERMINATED: {
  			TerminatedField terminatedField = new TerminatedField();
  			terminatedField.setEncodingType(ENCODING_TYPE.valueOf(rs
  					.getString("FLD_ENCODING").trim()));
  			terminatedField.setTerminator((byte) Integer.parseInt(
  					rs.getString("TERMINATOR_CHAR"), 16));

  			field = terminatedField;
  			break;
  		}
  		case VARIABLE: {
  			VariableField variableField = new VariableField();
  			variableField.setEncodingType(ENCODING_TYPE.valueOf(rs
  					.getString("FLD_ENCODING").trim()));
  			variableField.setIndicatorLength(rs.getInt("FLD_LEN_IND_SIZE"));
  			variableField.setIndicatorEncodingType(ENCODING_TYPE.valueOf(rs
  					.getString("FLD_LEN_IND_ENCODING").trim()));
  			field = variableField;
  			break;

  		}
  		case BITMAPPED: {
  			BitmappedField bitmappedField = new BitmappedField();
  			bitmappedField.setEncodingType(ENCODING_TYPE.valueOf(rs
  					.getString("FLD_ENCODING").trim()));
  			int fldSize = rs.getInt("FLD_SIZE");
  			if (fldSize == 8) {
  				bitmappedField.setBmpFieldType(BMP_FIELD_TYPE.PRIMARY);
  			} else {
  				bitmappedField.setBmpFieldType(BMP_FIELD_TYPE.EXTENDED);
  			}
  			field = bitmappedField;
  			break;

  		}
  		default: {
  			throw new UnsupportedOperationException("unsupported field:"
  					+ fieldType);
  		}

  		}

  		field.setFieldType(FIELD_TYPE.valueOf(rs.getString("FLD_TYPE")
  				.trim()));
  		field.setFieldId(rs.getInt("FLD_ID"));
  		field.setFieldName(rs.getString("FLD_NAME"));
  		field.setSequence(rs.getInt("FLD_SEQ"));

  		char mti = rs.getString("IS_MTI").charAt(0);
  		field.setMti(mti == 'Y' ? true : false);

  		char flightKey = rs.getString("IS_FLIGHT_KEY").charAt(0);
  		field.setFlightKey(flightKey == 'Y' ? true : false);

  		int parentId = rs.getInt("FLD_PARENT_ID");
  		if (parentId != 0) {
  			field.setParent(spec.getFieldById(parentId));
  		}

  		field.setSpec(spec);
  		return (field);
  	} catch (Exception e) {
  		log.error("unable to load field", e);
  		throw new KeedohModelException(e);
  	}

  }

  public static void remove(Field field) {
  	try {
  		String sql = "delete from fld where fld_id=? and msg_spec_id=?";
  		PreparedStatement st = Mahout.getConnection().prepareStatement(sql);
  		st.setInt(1, field.getFieldId());
  		st.setInt(2, field.getSpec().getSpecId());
  		st.executeUpdate();
  		st.close();
  		// remove from spec
  		field.getSpec().remove(field);
  	} catch (Exception e) {
  		throw new KeedohModelException(e);
  	}

  }*/

    public boolean hasChildren() {
        return (children.size() > 0);
    }

    public String dumpToString() {

        StringBuilder builder = new StringBuilder();
        dumpInternal(builder, 0);
        return builder.toString();
    }

    private void dumpInternal(StringBuilder builder, int indent) {
        builder.append(
                "field: "
                        + Strings.padStart(">", indent + 1, '-')
                        + getFieldName()
                        + " type:"
                        + getFieldType()
                        + " length:"
                        + length
                        + " encoding:"
                        + getEncodingType()
                        + "\n");
        if (getChildren() != null && getChildren().size() > 0) {
            getChildren().forEach(f -> f.dumpInternal(builder, indent + 1));
        }
    }
}
