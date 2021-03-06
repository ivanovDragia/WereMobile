package com.leetsoft.weremobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leetsoft.weremobile.background.FetchDataFromProductsBackground;
import com.leetsoft.weremobile.background.OnlySendDataToDocumentRowsBackground;
import com.leetsoft.weremobile.background.OnlySendDataToDocumentsBackground;
import com.leetsoft.weremobile.background.PostExecuteBackground;
import com.leetsoft.weremobile.background.PreExecuteBackground;
import com.leetsoft.weremobile.background.SendDataToContragentsBackground;
import com.leetsoft.weremobile.background.SendDataToDocumentRowsBackground;
import com.leetsoft.weremobile.background.SendDataToDocumentsBackground;
import com.leetsoft.weremobile.background.SendDataToLotsBackground;
import com.leetsoft.weremobile.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SyncActivity extends AppCompatActivity {

    public static TextView fetchedDataTV;
    public static Button btnSync, btnFullSync;
    public static ProgressBar progressBar;
    Context context = this;
    boolean connected = false;
    DatabaseHelper db = new DatabaseHelper(this);

    public static boolean productSyncSuccess = false;
    public static boolean contragentSyncSuccess = false;
    public static boolean lotSyncSuccess = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        //Shared Pref instance
        SharedPreferences sharedPreferences = getSharedPreferences("LastSync", MODE_PRIVATE);

        //widgets
        fetchedDataTV = findViewById(R.id.fetchedData);
        progressBar = findViewById(R.id.progressBar);
        btnSync = findViewById(R.id.btnSync);
        btnFullSync = findViewById(R.id.btnFullSync);

    }

    //using for logo at top right corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //onCLick button
    public void fullSyncOnClick(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Warning!");
        alert.setMessage("Are you sure you want to continue, all entries will be deleted during synchronization?");
        alert.setPositiveButton("Yes", (dialog1, which) -> {
            fetchData();
            fetchedDataTV.setText("");
        });
        alert.setNegativeButton("No", (dialog12, which) -> {
            Toast.makeText(this, "\n" +
                    "Sync denied!", Toast.LENGTH_SHORT).show();
        });
        alert.create().show();
    }

    public void syncOnClick(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Warning!");
        alert.setMessage("Are you sure you want to send data?");
        alert.setPositiveButton("Yes", (dialog1, which) -> {
            sendDataSync();
            fetchedDataTV.setText("");
        });
        alert.setNegativeButton("No", (dialog12, which) -> {
            Toast.makeText(this, "\n" +
                    "Sync denied!", Toast.LENGTH_SHORT).show();
        });
        alert.create().show();
    }

    //format date and return formated date as String
    private String formatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    private void fetchData() {
        internetConnection();

        FetchDataFromProductsBackground processProducts = new FetchDataFromProductsBackground(db);
        processProducts.execute();

        SendDataToLotsBackground processLots = new SendDataToLotsBackground(db);
        processLots.execute();

        SendDataToContragentsBackground processContragents = new SendDataToContragentsBackground(db);
        processContragents.execute();

        SendDataToDocumentsBackground processDocuments = new SendDataToDocumentsBackground(db);
        processDocuments.execute();

        SendDataToDocumentRowsBackground processDocumentRows = new SendDataToDocumentRowsBackground(db);
        processDocumentRows.execute();
    }

    private void sendDataSync() {
        internetConnection();

        PreExecuteBackground preExecute = new PreExecuteBackground();
        preExecute.execute();

        SendDataToLotsBackground processLots = new SendDataToLotsBackground(db);
        processLots.execute();

        SendDataToContragentsBackground processContragents = new SendDataToContragentsBackground(db);
        processContragents.execute();

        OnlySendDataToDocumentsBackground processDocuments = new OnlySendDataToDocumentsBackground(db);
        processDocuments.execute();

        OnlySendDataToDocumentRowsBackground processDocumentRows = new OnlySendDataToDocumentRowsBackground(db);
        processDocumentRows.execute();

        PostExecuteBackground postExecute = new PostExecuteBackground();
        postExecute.execute();
    }

    public void internetConnection()
    {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            Toast.makeText(this,"No Internet connection.",Toast.LENGTH_SHORT).show();
            connected = false;
    }
}