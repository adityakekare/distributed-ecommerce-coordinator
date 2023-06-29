package com.coordinator.service.paxos;

import java.io.Serializable;

/**
 * Class to encapsulate an Proposal in the Paxos algorithm
 */
public class Proposal implements Serializable {
  private int id;
  private RequestMethod requestMethod;

  public Proposal(){}
  public Proposal(int id, RequestMethod requestMethod) {
    this.id = id;
    this.requestMethod = requestMethod;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public RequestMethod getRequestMethod() {
    return requestMethod;
  }

  public void setRequestMethod(RequestMethod requestMethod) {
    this.requestMethod = requestMethod;
  }

}
