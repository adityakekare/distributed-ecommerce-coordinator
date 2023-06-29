package com.coordinator.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {

  private Integer id;

  @JsonFormat(pattern = "dd/MM/yyyy")
  private LocalDate dateCreated;

  private boolean isPaid;

  private List<ProductOrders> productOrders = new ArrayList<>();

  public Order(){}
  public Order(Integer id, LocalDate dateCreated, boolean isPaid, List<ProductOrders> productOrders) {
    this.id = id;
    this.dateCreated = dateCreated;
    this.isPaid = isPaid;
    this.productOrders = productOrders;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public LocalDate getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(LocalDate dateCreated) {
    this.dateCreated = dateCreated;
  }

  public boolean isPaid() {
    return isPaid;
  }

  public void setPaid(boolean paid) {
    isPaid = paid;
  }

  public List<ProductOrders> getProductOrders() {
    return productOrders;
  }

  public void setProductOrders(List<ProductOrders> productOrders) {
    this.productOrders = productOrders;
  }
}
