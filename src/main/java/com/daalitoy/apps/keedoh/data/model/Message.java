package com.daalitoy.apps.keedoh.data.model;

public class Message extends Model {

    private int msgId = -1;
    private String msgName;
    private MessageSegment requestSegment;
    private MessageSegment responseSegment;
    private Spec spec;
    private String mtiValues;

    private ConnectorConfig connectorConfig;
    // private String[] requestMti;
    // private String[] responseMti;

    public Message() {
    }

    public Message(Spec spec, String msgName) {
        this.spec = spec;
        this.msgName = msgName;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public MessageSegment getRequestSegment() {
        return requestSegment;
    }

    public void setRequestSegment(MessageSegment requestSegment) {
        this.requestSegment = requestSegment;
    }

    public MessageSegment getResponseSegment() {
        return responseSegment;
    }

    public void setResponseSegment(MessageSegment responseSegment) {
        this.responseSegment = responseSegment;
    }

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }

  /*public void setMtiValues(String mtiValues) {
  	this.mtiValues = mtiValues;
  	String tokens[]=mtiValues.split("=");
  	requestMti=tokens[0].split(",");
  	responseMti=tokens[1].split(",");
  }*/

  /*	public String[] getRequestMti() {
  	return requestMti;
  }

  public String[] getResponseMti() {
  	return responseMti;
  }*/

    public ConnectorConfig getConnectorConfig() {
        return connectorConfig;
    }

    public void setConnectorConfig(ConnectorConfig connectorConfig) {
        this.connectorConfig = connectorConfig;
    }

    public String dumpToString() {

        StringBuilder builder = new StringBuilder();
        builder.append("msg: " + getMsgName() + " - request \n");
        getRequestSegment().getFields().forEach(f -> builder.append(f.dumpToString()));
        builder.append("msg: " + getMsgName() + " - response \n");
        getResponseSegment().getFields().forEach(f -> builder.append(f.dumpToString()));

        return builder.toString();
    }

    /**
     * save the msg to db
     *
     * @param msg
     */
  /*
  public static void save(Message msg) {

  	// lets delete everything and then recreate the message
  	// easy and dirty
  	Message.delete(msg);
  	try {

  		// lets do request fragment first
  		int requestFragId = addFragment(msg, true);
  		int responseFragId = addFragment(msg, false);

  		String sql = "insert into msg values(NULL,?,?,?,?,?)";
  		PreparedStatement st = Mahout.getConnection().prepareStatement(sql);
  		st.setString(1, msg.getMsgName());
  		st.setInt(2, requestFragId);
  		st.setInt(3, responseFragId);
  		st.setInt(4, msg.getSpec().getSpecId());
  		// TODO::
  		st.setString(5, "1,2");
  		st.executeUpdate();
  		st.close();

  	} catch (Exception e) {
  		log.error("error adding message", e);
  		throw new KeedohModelException(e);
  	}

  }

  private static int addFragment(Message msg, boolean request)
  		throws Exception {

  	String sql = "insert into msg_frag values(?,?,?,?)";
  	PreparedStatement st = null;
  	int order = 1;

  	List<Field> fields = null;
  	if (request)
  		fields = msg.getRequestSegment().getFields();
  	else
  		fields = msg.getResponseSegment().getFields();

  	boolean first = true;
  	int fragId = 0;
  	for (Field field : fields) {
  		st = Mahout.getConnection().prepareStatement(sql);
  		if (first) {
  			st.setNull(1, Types.INTEGER);
  		} else {
  			st.setInt(1, fragId);
  		}
  		if (request)
  			st.setString(2, msg.getMsgName() + " Request Fragment");
  		else
  			st.setString(2, msg.getMsgName() + " Response Fragment");
  		st.setInt(3, order++);
  		st.setInt(4, field.getFieldId());
  		st.executeUpdate();
  		st.close();
  		if (first) {
  			first = false;
  			fragId = Mahout.getIdentity();
  		}
  	}

  	return (fragId);
  }

  */
    /**
     * delete the message from db
     *
     * @param msg
     */
  /*
  public static void delete(Message msg) {
  	if (msg.getMsgId() == -1) {
  		// not persisted yet, so do nothing
  	} else {
  		try {
  			String sql = "delete from msg_frag where msg_frag_id in(?,?)";
  			PreparedStatement st = Mahout.getConnection().prepareStatement(
  					sql);
  			st.setInt(1, msg.getRequestSegment().getFragmentId());
  			st.setInt(2, msg.getResponseSegment().getFragmentId());
  			st.executeUpdate();
  			st.close();
  			st = Mahout.getConnection().prepareStatement(
  					"delete from msg where msg_id=?");
  			st.setInt(1, msg.getMsgId());
  			st.executeUpdate();
  		} catch (Exception e) {
  			throw new KeedohModelException(e);
  		}
  	}

  }*/

}
