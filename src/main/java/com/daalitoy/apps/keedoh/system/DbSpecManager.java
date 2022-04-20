package com.daalitoy.apps.keedoh.system;

import com.daalitoy.apps.keedoh.db.mahout.Mahout;
import com.google.common.collect.Lists;
import io.github.rkbalgi.iso4k.Spec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class DbSpecManager implements SpecManager {

    private static final Logger log = LogManager.getLogger(DbSpecManager.class);

    private List<Spec> specs = Lists.newArrayList();

    @Override
    public Spec newSpec(String specName) {

        try {
//            String sql = "insert into msg_spec(msg_spec_id,msg_spec_name) values(NULL,?)";
//            Connection c = Mahout.getConnection();
//            PreparedStatement pSt = c.prepareCall(sql);
//
//            pSt.setString(1, specName);
//            pSt.execute();
//            pSt.close();
//            Statement cSt = c.createStatement();
//            ResultSet rs = cSt.executeQuery("call identity()");
//            rs.next();
//            int id = rs.getInt(1);
//            rs.close();
//            pSt.close();
//            cSt.close();
//            Spec spec = new Spec(id, specName);
//            specs.add(spec);
//            return (spec);
        } catch (Exception e) {
            log.info("unable to insert spec:", e);
            return (null);
        }
        return null;
    }

    @Override
    public List<Spec> allSpecs() {
//        if (specs != null) {
//            return (specs);
//        }
//        try {
//            String sql = "select * from msg_spec";
//            Connection c = Mahout.getConnection();
//            Statement st = c.createStatement();
//            ResultSet rs = st.executeQuery(sql);
//            specs = Lists.newArrayList();
//            while (rs.next()) {
//                specs.add(new Spec(rs.getInt(1), rs.getString(2)));
//            }
//            rs.close();
//            st.close();
//
//
//            return (specs);
//        } catch (Exception e) {
//            log.info("unable to insert spec", e);
//            return (null);
//        }
        return null;
    }
}
