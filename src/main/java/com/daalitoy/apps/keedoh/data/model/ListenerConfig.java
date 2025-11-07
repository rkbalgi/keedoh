package com.daalitoy.apps.keedoh.data.model;

import java.util.List;

public class ListenerConfig extends Model {

  private static List<ListenerConfig> instances = null;
  private String name;
  private String ip;
  private int port;
  private String processorScript;
  private MLI mli;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getProcessorScript() {
    return processorScript;
  }

  public void setProcessorScript(String processorScript) {
    this.processorScript = processorScript;
  }

  public MLI getMli() {
    return mli;
  }

  public void setMli(MLI mli) {
    this.mli = mli;
  }

  public String toString() {
    return (this.name);
  }
}
