package com.leetsoft.weremobile.database;

import org.json.JSONException;
import org.json.JSONObject;


public class DocumentRows {
    String id;
    String product_id;
    String lot_id;
    double quantity;
    double sum;
    String document_id;
    boolean is_synced;

    public DocumentRows() {
    }

    public DocumentRows(String product_id, String lot_id, double quantity, double sum, String document_id) {
        this.product_id = product_id;
        this.lot_id = lot_id;
        this.quantity = quantity;
        this.sum = sum;
        this.document_id = document_id;
    }

    public DocumentRows(String id, String product_id, String lot_id, double quantity, double sum, String document_id) {
        this.id = id;
        this.product_id = product_id;
        this.lot_id = lot_id;
        this.quantity = quantity;
        this.sum = sum;
        this.document_id = document_id;
    }

    public DocumentRows(String product_id, String lot_id, double quantity, double sum, String document_id,boolean is_synced) {
        this.product_id = product_id;
        this.lot_id = lot_id;
        this.quantity = quantity;
        this.sum = sum;
        this.document_id = document_id;
        this.is_synced = is_synced;
    }

    public DocumentRows(String id, String product_id, String lot_id, double quantity, double sum, String document_id,boolean is_synced) {
        this.id = id;
        this.product_id = product_id;
        this.lot_id = lot_id;
        this.quantity = quantity;
        this.sum = sum;
        this.document_id = document_id;
        this.is_synced = is_synced;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getLot_id() {
        return lot_id;
    }

    public void setLot_id(String lot_id) {
        this.lot_id = lot_id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }

    public boolean isIs_synced() {
        return is_synced;
    }

    public void setIs_synced(boolean is_synced) {
        this.is_synced = is_synced;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", id);
            obj.put("productid", product_id);
            obj.put("lotid", lot_id);
            obj.put("quantity",quantity);
            obj.put("sum",sum);
            obj.put("documentid",document_id);

        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
        return obj;
    }
}
