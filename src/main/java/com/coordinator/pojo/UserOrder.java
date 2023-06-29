package com.coordinator.pojo;

import java.util.List;

public class UserOrder {

    private List<OrderDto> productOrders;

    public UserOrder(){}

    public List<OrderDto> getProductOrders() {
      System.out.println(productOrders);
      return productOrders;
    }

    public void setProductOrders(List<OrderDto> productOrders) {
      this.productOrders = productOrders;
    }
  }