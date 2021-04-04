package com.leetsoft.weremobile.background;

import android.os.AsyncTask;
import android.view.View;

import com.leetsoft.weremobile.SyncActivity;


public class PreExecuteBackground  extends AsyncTask<String, String, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SyncActivity.progressBar.setVisibility(View.VISIBLE);
        SyncActivity.btnFullSync.setVisibility(View.INVISIBLE);
        SyncActivity.btnSync.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}
