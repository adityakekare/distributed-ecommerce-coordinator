package com.coordinator.service;

import com.coordinator.exception.ResourceNotFoundException;
import com.coordinator.service.paxos.Promise;
import com.coordinator.service.paxos.Proposal;
import com.coordinator.service.paxos.Status;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minidev.json.JSONObject;

@Service
public class PaxosService {

  private final String uri = "http://localhost:";
  private final Map<Integer, Integer> registeredServers;
  private int size;

  public PaxosService(){
    registeredServers = new HashMap<>();
    size = 0;
  }

  public ArrayList<Integer> getRegisteredServers() {
    return new ArrayList<>(registeredServers.keySet());
  }

  public Integer register(int port) {
    registeredServers.put(port, size + 1);
    size += 1;
    System.out.println(port);
    for(Integer key: registeredServers.keySet()){
      System.out.println(key);
    }
    return size;
  }

  public void unRegister(int port) {
      for(Integer key: registeredServers.keySet()){
        if(port == key){
          registeredServers.remove(key);
          return;
        }
      }
      throw new ResourceNotFoundException("Server not found");
  }

  public Boolean executeOperation(Proposal proposal) {
    Registry registry = null;
    List<Integer> acceptors = new ArrayList<>(registeredServers.keySet());

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    int half = Math.floorDiv(acceptors.size(), 2) + 1;
    int numPromised = 0;

    for(Integer serverPort: acceptors){
      try{
        String url = uri + serverPort + "/paxos/prepare";
        HttpEntity<Proposal> entity = new HttpEntity<>(proposal, headers);
        Promise promise = restTemplate.postForObject(url, entity, Promise.class);

        if(promise == null){
          System.out.println("Server at port " + serverPort + " is down");
        } else if(promise.getStatus() == Status.PROMISED || promise.getStatus() == Status.ACCEPTED){
          numPromised += 1;
          System.out.println("Server at port " + serverPort + " has PROMISED proposal " +
                  proposal.getRequestMethod());
        } else{
          System.out.println("Server at port " + serverPort + " has REJECTED proposal " +
                  proposal.getRequestMethod());
        }
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Server at port " + serverPort + " is NOT RESPONDING to the proposal " +
                proposal.getRequestMethod());
      }
    }

    // Check the majority
    if(numPromised < half){
      return false;
    }

//    // Remove servers that are down from the acceptors list
//    for(PaxosServer acceptor: failedServers){
//      acceptors.remove(acceptor);
//    }
//    // Empty the servers that are down list
//    failedServers.clear();


    int accepted = 0;
    for(Integer serverPort: acceptors){
      try{
        String url = uri + serverPort + "/paxos/accept";
        HttpEntity<Proposal> entity = new HttpEntity<>(proposal, headers);
        Boolean isAccepted = restTemplate.postForObject(url, entity, Boolean.class);
//        Boolean isAccepted = acceptor.accept(proposal);

        if(isAccepted == null){
          System.out.println("Server at port " + serverPort + " is NOT RESPONDING to the proposal " +
                  proposal.getRequestMethod());
//          failedServers.add(acceptor);
        }
        else if(isAccepted){
          accepted += 1;
          System.out.println("Server at port " + serverPort + " has ACCEPTED proposal " +
                  proposal.getRequestMethod());

        }
      } catch (Exception ignored) {
      }
    }

    // Check the majority
    if(accepted < half){
      return false;
    }

//    // Remove servers that are down from the acceptors list
//    for(PaxosServer acceptor: failedServers){
//      acceptors.remove(acceptor);
//    }
    return true;
  }

}
