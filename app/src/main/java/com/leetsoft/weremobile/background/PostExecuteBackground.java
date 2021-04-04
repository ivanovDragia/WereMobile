package com.leetsoft.weremobile.background;

import android.os.AsyncTask;
import android.view.View;

import com.leetsoft.weremobile.SyncActivity;


public class PostExecuteBackground extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        SyncActivity.progressBar.setVisibility(View.INVISIBLE);
        SyncActivity.btnFullSync.setVisibility(View.VISIBLE);
        SyncActivity.btnSync.setVisibility(View.VISIBLE);
    }
}
