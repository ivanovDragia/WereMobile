package com.leetsoft.weremobile.database;


public class Products {
    String id;
    String name;
    double quantity;
    double price;
    String product_number;

    public Products() {
    }

    public Products(String name, String product_number, double quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.product_number = product_number;
    }

    public Products(String id, String product_number, String name, double quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.product_number = product_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProduct_number() {
        return product_number;
    }

    public void setProduct_number(String product_number) {
        this.product_number = product_number;
    }

}
