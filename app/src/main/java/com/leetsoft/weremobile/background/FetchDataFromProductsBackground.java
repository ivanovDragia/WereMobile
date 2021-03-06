package com.leetsoft.weremobile.background;

import android.os.AsyncTask;
import android.view.View;

import com.leetsoft.weremobile.SyncActivity;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.Products;
import com.leetsoft.weremobile.database.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FetchDataFromProductsBackground extends AsyncTask<Void, Void, Void> {
    String data = "";
    String dataParsed = "";
    String singleParsed = "";
    DatabaseHelper db;
    String ip = "";
    String port = "";
    List<Settings> settingsList;
    List<Products> productsList;
    List<Products> fetchedProductsList;


    public FetchDataFromProductsBackground(DatabaseHelper db) {
        this.db = db;
        settingsList = new ArrayList<>();
        settingsList = db.getAllSettings();
        productsList = new ArrayList<>();
        productsList = db.getAllProducts();
        fetchedProductsList = new ArrayList<>();
        for (Settings setting : settingsList) {
            if (setting.getName().toLowerCase().trim().equals("ip")) {
                ip = setting.getValue().trim();
            }
            if (setting.getName().toLowerCase().trim().equals("port")) {
                port = setting.getValue().trim();
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SyncActivity.progressBar.setVisibility(View.VISIBLE);
        SyncActivity.btnFullSync.setVisibility(View.INVISIBLE);
        SyncActivity.btnSync.setVisibility(View.INVISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("http://" + ip + ":" + port + "/GetAllProducts");
            //URL url = new URL("http://192.168.0.101:9090/GetAllProducts");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONArray productsJArray = new JSONArray(data);
            for (int i = 0; i < productsJArray.length(); i++) {
                JSONObject JO = (JSONObject) productsJArray.get(i);

                Products product = new Products();
                product.setId(JO.getString("ID"));
                product.setName(JO.getString("Name"));
                product.setPrice(JO.getDouble("Price"));
                product.setQuantity(JO.getDouble("Quantity"));
                product.setProduct_number(JO.getString("ProductNumber"));

                fetchedProductsList.add(product);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            //SyncActivity.fetchedDataTV.setText("Problem with URL in Products\n");
        } catch (IOException e) {
            e.printStackTrace();
            //SyncActivity.fetchedDataTV.setText("Problem with IO in Products\n");
        } catch (JSONException e) {
            e.printStackTrace();
            //SyncActivity.fetchedDataTV.setText("Problem with JSON in Products\n");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //?? ?????????????? ???? ?????????????? ???????????????? ?? ???? ??????????, ???????????? ???????????? ???????????? ???????????????? ?? ?????????? ???? 1 ?? ???????? ?????? ?????????????????????? ?? ?????? ?? ?????????????? ???? ?????????????? ???????????????????????? ???? ???????????? ??????????????.
        if(fetchedProductsList.size()>0){
            db.deleteAllProducts();
            db.deleteAllBarcodes();
            String text = SyncActivity.fetchedDataTV.getText().toString();
            SyncActivity.fetchedDataTV.setText(text+"Product Sync is done!\n");
        }
        for (Products product:fetchedProductsList) {
            db.addProduct(product);
        }

        SyncActivity.productSyncSuccess = true;
    }
}
