package com.leetsoft.weremobile.background;

import android.os.AsyncTask;

import com.leetsoft.weremobile.SyncActivity;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.Lots;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FetchDataFromLotsBackground extends AsyncTask<Void, Void, Void>{
    String data = "";
    DatabaseHelper db;
    String ip = "";
    String port = "";
    List<Settings> settingsList;
    List<Lots> lotsList;
    List<Lots> fetchedLotsList;

    public FetchDataFromLotsBackground(DatabaseHelper db) {
        this.db = db;
        settingsList = new ArrayList<>();
        settingsList = db.getAllSettings();
        lotsList = new ArrayList<>();
        lotsList = db.getAllLots();
        fetchedLotsList = new ArrayList<>();
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
            URL url = new URL("http://" + ip + ":" + port + "/GetAllLots");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONArray lotsJArray = new JSONArray(data);
            for (int i = 0; i < lotsJArray.length(); i++) {
                JSONObject JO = (JSONObject) lotsJArray.get(i);

                Lots lot = new Lots();
                lot.setId(JO.getString("ID"));
                lot.setProduct_id(JO.getString("ProductID"));
                lot.setQuantity(JO.getDouble("Quantity"));

                String dateInMillis=JO.getString("ExpirationDate");

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(dateInMillis.substring(6, dateInMillis.length() - 7)));

                SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                        Locale.getDefault());
                Date date = new Date();
                try {
                    date = dateFormat.parse(calendar.getTime().toString());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                lot.setExpiration_date(date);

                lot.setLot_number(JO.getString("LotNumber"));

                lot.setIs_synced(true);

                fetchedLotsList.add(lot);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(fetchedLotsList.size()>0){
            db.deleteAllLots();
            String text = SyncActivity.fetchedDataTV.getText().toString();
            SyncActivity.fetchedDataTV.setText(text+"Lot Sync is done!\n");
        }
        for (Lots lot:fetchedLotsList) {
            db.addLot(lot);
        }

        SyncActivity.lotSyncSuccess = true;
    }
}
