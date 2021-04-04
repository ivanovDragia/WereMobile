package com.leetsoft.weremobile.database;


public class Barcodes {
    String id;
    String barcode;
    String product_id;

    public Barcodes() {
    }

    public Barcodes(String barcode, String product_id) {
        this.barcode = barcode;
        this.product_id = product_id;
    }

    public Barcodes(String id, String barcode, String product_id) {
        this.id = id;
        this.barcode = barcode;
        this.product_id = product_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
