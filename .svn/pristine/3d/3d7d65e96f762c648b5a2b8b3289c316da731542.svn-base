package com.leetsoft.weremobile.util;

import com.leetsoft.weremobile.database.Products;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Georgi Shukov on 13.1.2021 г..
 */
public class DataUtil {
    public String formatDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public String getProductName(String id, List<Products> productsList){
        String productName = "";
        for(Products product : productsList) {
            if(product.getId().equals(id)) {
                productName = product.getName();
            }
        }
        return productName;
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String randomStringUUID(){
        UUID uuid = UUID.randomUUID();
        //Toast.makeText(this,"UUID=" + uuid.toString(),Toast.LENGTH_LONG).show();
        return uuid.toString();
    }
}
