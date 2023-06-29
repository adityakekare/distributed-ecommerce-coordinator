package com.coordinator.service.paxos;

import java.io.Serializable;

/**
 * Static class to generate proposals with unique IDs
 */
public final class PaxosUtils implements Serializable {

  private static int time = 0;

  public static synchronized Proposal createProposal(RequestMethod requestMethod){
    int proposalId = time++;

    Proposal proposal = new Proposal(proposalId, requestMethod);

    try{
      Thread.sleep(1);
    } catch (InterruptedException e) {
      System.out.println("Server error");
    }
    return proposal;
  }
}
