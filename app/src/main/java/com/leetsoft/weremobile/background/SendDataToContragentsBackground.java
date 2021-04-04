package com.leetsoft.weremobile.background;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.leetsoft.weremobile.database.Contragents;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.Settings;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class SendDataToContragentsBackground extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "SendDataToContragentsBackground";

    DatabaseHelper db;
    List<Contragents> contragentsList;
    List<Contragents> contragentsForAddAndUpdate;
    String ip = "";
    String port = "";
    List<Settings> settingsList;


    public SendDataToContragentsBackground(DatabaseHelper db) {
        this.db = db;
        contragentsList = new ArrayList<>();
        contragentsForAddAndUpdate = new ArrayList<>();
        contragentsList = db.getAllContragents();
        settingsList = new ArrayList<>();
        settingsList = db.getAllSettings();
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
        for (Contragents contragent :
                contragentsList) {
            if (!contragent.isIs_synced()) {
                contragentsForAddAndUpdate.add(contragent);
            }
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < contragentsForAddAndUpdate.size(); i++) {
            jsonArray.put(contragentsForAddAndUpdate.get(i).getJSONObject());
        }

        String encodedJArray = Base64.encodeToString((jsonArray.toString()).getBytes(), Base64.NO_WRAP);

        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL("http://" + ip + ":" + port + "/AddContragent");
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
//            httpURLConnection.setRequestProperty("multipart/form-data", "https://eddn.usgs.gov/fieldtest.html;charset=UTF-8");
//            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");

            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);

            httpURLConnection.connect();

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(encodedJArray);
            wr.flush();
            wr.close();

            httpURLConnection.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("test", "result from server: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        FetchDataFromContragentsBackground processContragents = new FetchDataFromContragentsBackground(db);
        processContragents.execute();
    }
}
