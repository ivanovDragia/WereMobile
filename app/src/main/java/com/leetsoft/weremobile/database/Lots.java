package com.leetsoft.weremobile.database;

import java.util.Date;


public class Lots {
    String id;
    String lot_number;
    String product_id;
    double quantity;
    Date expiration_date;
    boolean is_synced;


    public Lots() {
    }

    public Lots(String lot_number, String product_id, double quantity, Date expiration_date) {
        this.lot_number = lot_number;
        this.product_id = product_id;
        this.quantity = quantity;
        this.expiration_date = expiration_date;
    }

    public Lots(String id, String lot_number, String product_id, double quantity, Date expiration_date) {
        this.id = id;
        this.lot_number = lot_number;
        this.product_id = product_id;
        this.quantity = quantity;
        this.expiration_date = expiration_date;
    }

    public Lots(String lot_number, String product_id, double quantity, Date expiration_date, boolean is_synced) {
        this.lot_number = lot_number;
        this.product_id = product_id;
        this.quantity = quantity;
        this.expiration_date = expiration_date;
        this.is_synced=is_synced;
    }

    public Lots(String id, String lot_number, String product_id, double quantity, Date expiration_date, boolean is_synced) {
        this.id = id;
        this.lot_number = lot_number;
        this.product_id = product_id;
        this.quantity = quantity;
        this.expiration_date = expiration_date;
        this.is_synced=is_synced;
    }

    public boolean isIs_synced() {
        return is_synced;
    }

    public void setIs_synced(boolean is_synced) {
        this.is_synced = is_synced;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLot_number() {
        return lot_number;
    }

    public void setLot_number(String lot_number) {
        this.lot_number = lot_number;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Date expiration_date) {
        this.expiration_date = expiration_date;
    }
}
