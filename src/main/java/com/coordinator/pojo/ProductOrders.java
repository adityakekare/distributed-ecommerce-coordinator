package com.coordinator.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProductOrders {

  private ProductOrdersPK key;

  private Integer quantity;

  public ProductOrders(){}

  public ProductOrders(Product product, Order order, Integer quantity){
    this.key = new ProductOrdersPK();
    key.setOrder(order);
    key.setProduct(product);
    this.quantity = quantity;
  }

  public Product getProduct() {
    return this.key.getProduct();
  }

  public float getTotalPrice() {
    return getProduct().getPrice() * getQuantity();
  }

  public ProductOrdersPK getKey() {
    return key;
  }

  public void setKey(ProductOrdersPK key) {
    this.key = key;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

}
