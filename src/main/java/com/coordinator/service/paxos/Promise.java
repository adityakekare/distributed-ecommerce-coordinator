package com.coordinator.service.paxos;

import java.io.Serializable;

/**
 * Class to encapsulate an Promise in the Paxos algorithm
 */
public class Promise implements Serializable {
  private Status status;
  private Proposal proposal;


  public Promise(){}

  public Promise(Status status, Proposal proposal) {
    this.status = status;
    this.proposal = proposal;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Proposal getProposal() {
    return proposal;
  }

  public void setProposal(Proposal proposal) {
    this.proposal = proposal;
  }
}
