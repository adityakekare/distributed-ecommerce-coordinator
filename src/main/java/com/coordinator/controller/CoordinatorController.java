package com.coordinator.controller;

import com.coordinator.pojo.Order;
import com.coordinator.pojo.Product;
import com.coordinator.pojo.ProductOrders;
import com.coordinator.pojo.ProductOrdersPK;
import com.coordinator.pojo.UserOrder;
import com.coordinator.service.PaxosService;
import com.coordinator.service.paxos.Promise;
import com.coordinator.service.paxos.Proposal;
import com.coordinator.service.paxos.RequestMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.coordinator.service.paxos.PaxosUtils.createProposal;

@RestController
@RequestMapping("")
public class CoordinatorController {

  private final String uri = "http://localhost:";

  @Autowired
  PaxosService paxosService;

  @GetMapping("/register/{portNo}")
  public ResponseEntity<Integer> registerServer(@PathVariable("portNo") Integer port) {
    int serverId = paxosService.register(port);
    return new ResponseEntity<>(serverId, HttpStatus.OK);
  }

  @GetMapping("/product")
  public ResponseEntity<Iterable<Product>> getProducts() {
    Proposal proposal = createProposal(RequestMethod.GET);
    if(!paxosService.executeOperation(proposal)){
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    RestTemplate restTemplate = new RestTemplate();
    Product[] products = new Product[200];
    for(Integer server: paxosService.getRegisteredServers()){
      String url = uri + server + "/product";
      products = restTemplate.getForObject(url, Product[].class);
    }
    List<Product> body = new ArrayList<>(Arrays.asList(Objects.requireNonNull(products)));
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  @GetMapping(value = { "/order", "/order/" })
  public ResponseEntity<Iterable<Object>> getOrders() {
    Proposal proposal = createProposal(RequestMethod.GET);
    if(!paxosService.executeOperation(proposal)){
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    RestTemplate restTemplate = new RestTemplate();
    Object[] orders = new Object[200];
    for(Integer server: paxosService.getRegisteredServers()){
      String url = uri + server + "/order";
      orders = restTemplate.getForObject(url, Object[].class);
    }
    List<Object> body = new ArrayList<>(Arrays.asList(Objects.requireNonNull(orders)));
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  @GetMapping("/order/{orderId}")
  public ResponseEntity<Object> getOrder(@PathVariable("orderId") Integer orderId) {
    Proposal proposal = createProposal(RequestMethod.GET);
    if(!paxosService.executeOperation(proposal)){
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    RestTemplate restTemplate = new RestTemplate();
    Object newOrder = null;
    for(Integer server: paxosService.getRegisteredServers()){
      String url = uri + server + "/order/" + orderId;
      newOrder = restTemplate.getForObject(url, Object.class);
    }

    return new ResponseEntity<>(newOrder, HttpStatus.OK);
  }

  @PostMapping("/order")
  public ResponseEntity<Object> create(@RequestBody UserOrder order) {
    Proposal proposal = createProposal(RequestMethod.PUT);
    if(!paxosService.executeOperation(proposal)){
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<UserOrder> entity = new HttpEntity<>(order, headers);
    Object newOrder = null;
    for(Integer serverPort: paxosService.getRegisteredServers()){
      String url = uri + serverPort + "/order";
      newOrder = restTemplate.postForObject(url, entity, Object.class);
    }
    return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
  }
}
