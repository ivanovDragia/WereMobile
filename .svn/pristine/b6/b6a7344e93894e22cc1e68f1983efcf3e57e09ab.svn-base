package com.leetsoft.weremobile.background;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.leetsoft.weremobile.SyncActivity;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.Documents;
import com.leetsoft.weremobile.database.Settings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class SendDataToDocumentsBackground extends AsyncTask<String, String, String> {
    DatabaseHelper db;
    List<Documents> documentsList;
    List<Documents> documentsForAddAndUpdateList;

    String ip="";
    String port="";
    List<Settings> settingsList;


    public SendDataToDocumentsBackground(DatabaseHelper db){
        this.db=db;

        documentsList=new ArrayList<>();
        documentsList=db.getAllDocuments();
        documentsForAddAndUpdateList = new ArrayList<>();

        settingsList=new ArrayList<>();
        settingsList=db.getAllSettings();

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
    protected String doInBackground(String... params) {
        JSONArray jArray=new JSONArray();
        for(Documents document:documentsList){
            if(!document.isIs_synced()){
                documentsForAddAndUpdateList.add(document);
            }
        }

        for(Documents document: documentsForAddAndUpdateList){
            JSONObject jObject=new JSONObject();

            try{
                jObject.put("ID", document.getId());
                jObject.put("SourceID", document.getSource_id());
                jObject.put("DestinationID", document.getDestination_id());
                jObject.put("DocumentNumber", document.getDocument_number());
                jObject.put("Date", document.getDate().getTime());
                jArray.put(jObject);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        String encodedJArray = Base64.encodeToString(jArray.toString().getBytes(), Base64.NO_WRAP);
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL("http://" + ip + ":" + port + "/AddDocument");
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
        }catch (Exception e){
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
        db.deleteAllDocuments();
        if(documentsForAddAndUpdateList.size()>0){
            String text = SyncActivity.fetchedDataTV.getText().toString();
            SyncActivity.fetchedDataTV.setText(text+"Documents Sending is done!\n");
        }
    }
}
