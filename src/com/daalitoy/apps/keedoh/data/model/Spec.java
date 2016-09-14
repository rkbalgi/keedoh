package com.daalitoy.apps.keedoh.data.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.daalitoy.apps.keedoh.db.mahout.Mahout;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Spec extends Model {

	private static ArrayList<Spec> specs;
	private int specId;
	private String specName;
	private List<Field> fields = Lists.newArrayList();
	private Map<Integer, Field> fieldsMap = Maps.newHashMap();
	private List<Message> messages;

	public Spec(int specId, String specName) {
		super();
		this.specId = specId;
		this.specName = specName;
	}

	public int getSpecId() {
		return specId;
	}

	public void setSpecId(int specId) {
		this.specId = specId;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String toString() {
		return (specName);
	}

	public static Spec newSpec(String specName) {

		try {
			String sql = "insert into msg_spec(msg_spec_id,msg_spec_name) values(NULL,?)";
			Connection c = Mahout.getConnection();
			PreparedStatement pSt = c.prepareCall(sql);

			pSt.setString(1, specName);
			pSt.execute();
			pSt.close();
			Statement cSt = c.createStatement();
			ResultSet rs = cSt.executeQuery("call identity()");
			rs.next();
			int id = rs.getInt(1);
			rs.close();
			pSt.close();
			cSt.close();
			Spec spec = new Spec(id, specName);
			specs.add(spec);
			return (spec);
		} catch (Exception e) {
			log.info("unable to insert spec:", e);
			return (null);
		}

	}

	public static List<Spec> allSpecs() {
		if (specs != null) {
			return (specs);
		}
		try {
			String sql = "select * from msg_spec";
			Connection c = Mahout.getConnection();
			Statement st = c.createStatement();
			ResultSet rs = st.executeQuery(sql);
			specs = Lists.newArrayList();
			while (rs.next()) {
				specs.add(new Spec(rs.getInt(1), rs.getString(2)));
			}
			rs.close();
			st.close();

			for (Spec spec : specs) {
				spec.load();
				spec.setMessages(Message.allMessages(spec));
			}

			return (specs);
		} catch (Exception e) {
			log.info("unable to insert spec", e);
			return (null);
		}
	}

	public Field getFieldById(int fieldId) {
		return (fieldsMap.get(fieldId));
	}

	private void load() {
		try {

			String sql = "select * from fld where msg_spec_id=?";
			Connection c = Mahout.getConnection();
			PreparedStatement st = c.prepareCall(sql);
			st.setInt(1, getSpecId());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Field field = Field.asObj(this, rs);
				fieldsMap.put(field.getFieldId(), field);
			}
		} catch (Exception e) {
			log.error("unable to load spec", e);
		}

		// populate children for each node
		for (Field f : fieldsMap.values()) {
			if (f.getParent() != null) {
				// this is a child, find parent and add
				f.getParent().addChild(f);
			}
		}

		// finally accumalate only those fields that dont
		// have parent
		for (Field f : fieldsMap.values()) {
			if (f.getParent() == null) {
				add(f);
			}
		}

	}

	public void add(Field field) {
		fields.add(field);

	}

	public List<Field> getFields() {
		return (fields);
	}

	public void remove(Field field) {
		fields.remove(field);
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<Message> getMessages() {
		return (messages);
	}

	public Field getFieldByName(String fieldName) {
		for (Field field : fields) {
			if (field.getFieldName().equals(fieldName)) {
				return (field);
			}
		}
		return (null);
	}

}
