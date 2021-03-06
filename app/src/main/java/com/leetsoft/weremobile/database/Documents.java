package com.leetsoft.weremobile.database;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class Documents {
    String id;
    String source_id;
    String destination_id;
    int document_number;
    Date date;
    boolean is_synced;

    public boolean isIs_synced() {
        return is_synced;
    }

    public void setIs_synced(boolean is_synced) {
        this.is_synced = is_synced;
    }

    public Documents() {
    }

    public Documents(String source_id, String destination_id, int document_number, Date date) {
        this.source_id = source_id;
        this.destination_id = destination_id;
        this.document_number = document_number;
        this.date = date;
    }

    public Documents(String id, String source_id, String destination_id, int document_number, Date date) {
        this.id = id;
        this.source_id = source_id;
        this.destination_id = destination_id;
        this.document_number = document_number;
        this.date = date;
    }

    public Documents(String source_id, String destination_id, int document_number, Date date,boolean is_synced) {
        this.source_id = source_id;
        this.destination_id = destination_id;
        this.document_number = document_number;
        this.date = date;
        this.is_synced = is_synced;
    }

    public Documents(String id, String source_id, String destination_id, int document_number, Date date,boolean is_synced) {
        this.id = id;
        this.source_id = source_id;
        this.destination_id = destination_id;
        this.document_number = document_number;
        this.date = date;
        this.is_synced = is_synced;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(String destination_id) {
        this.destination_id = destination_id;
    }

    public int getDocument_number() {
        return document_number;
    }

    public void setDocument_number(int document_number) {
        this.document_number = document_number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
