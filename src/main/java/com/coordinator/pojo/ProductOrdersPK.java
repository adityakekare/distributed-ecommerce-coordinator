package com.coordinator.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class ProductOrdersPK implements Serializable {

  @JsonBackReference
  private Order order;

  private Product product;

  public ProductOrdersPK(){}
  public ProductOrdersPK(Order order, Product product) {
    this.order = order;
    this.product = product;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

}
