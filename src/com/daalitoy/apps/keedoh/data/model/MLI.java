package com.daalitoy.apps.keedoh.data.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.daalitoy.apps.keedoh.data.model.impl.MsgLenIndicator;
import com.daalitoy.apps.keedoh.db.mahout.Mahout;
import com.google.common.collect.Lists;

public class MLI extends Model {

	private String name;
	private MsgLenIndicator mliImpl;
	private static List<MLI> instances = null;
	
	static{
		getInstances();
	}

	public MLI(String name, MsgLenIndicator obj) {
		this.name = name;
		this.mliImpl = obj;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MsgLenIndicator getMliImpl() {
		return mliImpl;
	}

	public String toString() {
		return (name);
	}

	public static List<MLI> getInstances() {
		if (instances != null) {
			return (instances);
		}
		try {
			PreparedStatement st = Mahout.getConnection().prepareStatement(
					"select * from mli");
			ResultSet rs = st.executeQuery();
			instances = Lists.newArrayList();
			while (rs.next()) {
				String className = rs.getString("MLI_CLASS");
				MLI mli = new MLI(rs.getString("MLI_NAME").trim(),
						(MsgLenIndicator) Class.forName(className)
								.newInstance());
				instances.add(mli);
			}

			return (instances);
		} catch (Exception e) {
			log.error("unable to load MLI", e);
			throw new KeedohModelException(e);
		}

	}

	public static MLI getByName(String mliName) {
		if (instances == null) {
			getInstances();
		}
		for (MLI mli : instances) {
			if (mli.getName().equals(mliName)) {
				return (mli);
			}
		}
		return null;
	}

}
