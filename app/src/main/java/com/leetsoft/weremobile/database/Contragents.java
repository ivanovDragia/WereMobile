package com.leetsoft.weremobile.database;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class Contragents {
    String id;
    String name;
    String bulstat;
    String VAT_number;
    String city;
    String address;
    String MRP;
    String phone_number;
    boolean is_synced;

    public Contragents() {
    }

    public Contragents(String name, String bulstat, String VAT_number, String city, String address, String MRP, String phone_number) {
        this.name = name;
        this.bulstat = bulstat;
        this.VAT_number = VAT_number;
        this.city = city;
        this.address = address;
        this.MRP = MRP;
        this.phone_number = phone_number;
    }

    public Contragents(String id, String name, String bulstat, String VAT_number, String city, String address, String MRP, String phone_number) {
        this.id = id;
        this.name = name;
        this.bulstat = bulstat;
        this.VAT_number = VAT_number;
        this.city = city;
        this.address = address;
        this.MRP = MRP;
        this.phone_number = phone_number;
    }

    public Contragents(String id, String name, String bulstat, String VAT_number, String city, String address, String MRP, String phone_number, boolean is_synced) {
        this.id = id;
        this.name = name;
        this.bulstat = bulstat;
        this.VAT_number = VAT_number;
        this.city = city;
        this.address = address;
        this.MRP = MRP;
        this.phone_number = phone_number;
        this.is_synced = is_synced;
    }

    public Contragents(String name, String bulstat, String VAT_number, String city, String address, String MRP, String phone_number, boolean is_synced) {
        this.name = name;
        this.bulstat = bulstat;
        this.VAT_number = VAT_number;
        this.city = city;
        this.address = address;
        this.MRP = MRP;
        this.phone_number = phone_number;
        this.is_synced = is_synced;
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

    public String getBulstat() {
        return bulstat;
    }

    public void setBulstat(String bulstat) {
        this.bulstat = bulstat;
    }

    public String getVAT_number() {
        return VAT_number;
    }

    public void setVAT_number(String VAT_number) {
        this.VAT_number = VAT_number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public boolean isIs_synced() {
        return is_synced;
    }

    public void setIs_synced(boolean is_synced) {
        this.is_synced = is_synced;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", id);
            obj.put("name", name);
            obj.put("bulstat", bulstat);
            obj.put("vatnumber",VAT_number);
            obj.put("city",city);
            obj.put("address",address);
            obj.put("mrp",MRP);
            obj.put("phonenumber",phone_number);

        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
        return obj;
    }

}
