package com.leetsoft.weremobile.database;

public class Settings {
    String id;
    String name;
    String value;
    String description;

    public Settings() {
    }

    public Settings(String n, String v, String d){
        this.name=n;
        this.value=v;
        this.description=d;
    }

    public Settings(String id,String n, String v, String d){
        this.id = id;
        this.name=n;
        this.value=v;
        this.description=d;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
