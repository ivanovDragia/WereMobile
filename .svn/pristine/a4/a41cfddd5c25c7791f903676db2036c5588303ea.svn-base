package com.leetsoft.weremobile.background;

import android.os.AsyncTask;

import com.leetsoft.weremobile.SyncActivity;
import com.leetsoft.weremobile.database.Contragents;
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

public class FetchDataFromContragentsBackground extends AsyncTask<Void, Void, Void> {
    String data = "";
    DatabaseHelper db;
    String ip = "";
    String port = "";
    List<Settings> settingsList;
    List<Contragents> contragentsList;
    List<Contragents> fetchedContragentsList;

    public FetchDataFromContragentsBackground(DatabaseHelper db) {
        this.db = db;
        settingsList = new ArrayList<>();
        settingsList = db.getAllSettings();
        contragentsList = new ArrayList<>();
        contragentsList = db.getAllContragents();
        fetchedContragentsList = new ArrayList<>();
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
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("http://" + ip + ":" + port + "/GetAllContragents");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONArray contragentsJArray = new JSONArray(data);
            for (int i = 0; i < contragentsJArray.length(); i++) {
                JSONObject JO = (JSONObject) contragentsJArray.get(i);

                Contragents contragent = new Contragents();
                contragent.setId(JO.getString("ID"));
                contragent.setName(JO.getString("Name"));
                contragent.setBulstat(JO.getString("Bulstat"));
                contragent.setVAT_number(JO.getString("VatNumber"));
                contragent.setCity(JO.getString("City"));
                contragent.setAddress(JO.getString("Address"));
                contragent.setMRP(JO.getString("Mrp"));
                contragent.setPhone_number(JO.getString("PhoneNumber"));
                contragent.setIs_synced(true);

                fetchedContragentsList.add(contragent);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //SyncActivity.fetchedDataTV.setText("Problem with URL in Contragents\n");
        } catch (IOException e) {
            e.printStackTrace();
            //yncActivity.fetchedDataTV.setText("Problem with IO in Contragents\n");
        } catch (JSONException e) {
            e.printStackTrace();
            //SyncActivity.fetchedDataTV.setText("Problem with JSON in Contragents\n");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(fetchedContragentsList.size()>0){
            db.deleteAllContragents();
            String text = SyncActivity.fetchedDataTV.getText().toString();
            SyncActivity.fetchedDataTV.setText(text+"Contragent Sync is done!\n");
        }
        for (Contragents contragent:fetchedContragentsList) {
            db.addContragentWithID(contragent);
        }

        SyncActivity.contragentSyncSuccess = true;
    }
}
