package com.leetsoft.weremobile.background;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.Lots;
import com.leetsoft.weremobile.database.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SendDataToLotsBackground extends AsyncTask<String, String, String> {
    DatabaseHelper db;
    List<Lots> lotsList;
    List<Lots> unsyncedLotsList;
    String ip = "";
    String port = "";
    List<Settings> settingsList;

    public SendDataToLotsBackground(DatabaseHelper db) {
        this.db = db;

        lotsList = new ArrayList<>();
        lotsList = db.getAllLots();

        settingsList = new ArrayList<>();
        settingsList = db.getAllSettings();

        unsyncedLotsList = new ArrayList<>();

        for (Settings setting : settingsList) {
            if (setting.getName().trim().toLowerCase().equals("ip")) {
                ip = setting.getValue();
            }
            if (setting.getName().trim().toLowerCase().equals("port")) {
                port = setting.getValue();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(String... params) {

        JSONArray jArray = new JSONArray();

        for (Lots lot : lotsList) {
            if (!lot.isIs_synced()) {
                JSONObject jsonObj = new JSONObject();

                try {
                    jsonObj.put("ID", lot.getId());
                    jsonObj.put("ProductID", lot.getProduct_id());
                    jsonObj.put("Quantity", lot.getQuantity());
                    jsonObj.put("ExpirationDate", lot.getExpiration_date().getTime());
                    jsonObj.put("LotNumber", lot.getLot_number());
                    jArray.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String encodedJArray = Base64.encodeToString(jArray.toString().getBytes(), Base64.NO_WRAP);
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL("http://" + ip + ":" + port + "/AddLot");
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        FetchDataFromLotsBackground process = new FetchDataFromLotsBackground(db);
        process.execute();
    }
}
