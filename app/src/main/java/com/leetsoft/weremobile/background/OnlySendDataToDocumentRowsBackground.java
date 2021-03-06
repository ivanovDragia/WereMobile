package com.leetsoft.weremobile.background;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.leetsoft.weremobile.SyncActivity;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.DocumentRows;
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


public class OnlySendDataToDocumentRowsBackground extends AsyncTask<String, String, String> {
    DatabaseHelper db;
    List<DocumentRows> documentRowsList;
    List<DocumentRows> documentRowsForAddAndUpdateList;

    String ip = "";
    String port = "";
    List<Settings> settingsList;


    public OnlySendDataToDocumentRowsBackground(DatabaseHelper db) {
        this.db = db;

        documentRowsList = new ArrayList<>();
        documentRowsList = db.getAllDocumentRows();
        documentRowsForAddAndUpdateList = new ArrayList<>();

        settingsList = new ArrayList<>();
        settingsList = db.getAllSettings();

        for (Settings setting : settingsList) {
            if (setting.getName().trim().toLowerCase().equals("ip")) {
                ip = setting.getValue();
            }
            if (setting.getName().trim().toLowerCase().equals("port")) {
                port = setting.getValue();
            }
        }
    }


    @Override
    protected String doInBackground(String... strings) {
        for (DocumentRows documentRow :
                documentRowsList) {
            if (!documentRow.isIs_synced()) {
                documentRowsForAddAndUpdateList.add(documentRow);
            }
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < documentRowsForAddAndUpdateList.size(); i++) {
            jsonArray.put(documentRowsForAddAndUpdateList.get(i).getJSONObject());
        }

        String encodedJArray = Base64.encodeToString((jsonArray.toString()).getBytes(), Base64.NO_WRAP);

        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL("http://" + ip + ":" + port + "/AddDocumentRow");
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (documentRowsForAddAndUpdateList.size() > 0) {
            String text = SyncActivity.fetchedDataTV.getText().toString();
            SyncActivity.fetchedDataTV.setText(text + "Document Rows Sending is done!\n");
        }

        for (DocumentRows row : documentRowsForAddAndUpdateList) {
            db.updateDocumentRowSynced(row);
        }
    }
}
