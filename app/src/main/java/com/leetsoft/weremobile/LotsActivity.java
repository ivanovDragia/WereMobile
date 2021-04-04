package com.leetsoft.weremobile;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leetsoft.weremobile.adapters.LotsAdapter;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.Lots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LotsActivity extends AppCompatActivity {
    private static final String TAG = "LotsActivity";

    DatabaseHelper db = new DatabaseHelper(this);

    List<Lots> lotsList = new ArrayList<>();

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lots);
        Log.d(TAG, "onCreate: started.");

        lotsList = db.getAllLots();
        Collections.sort(lotsList,
                (o1, o2) -> o1.getExpiration_date().compareTo(o2.getExpiration_date()));

        for (Lots lot : lotsList) {
            System.out.println(lot.getProduct_id() + " " + lot.getQuantity() + " " + lot.getExpiration_date());
        }

        searchView = findViewById(R.id.lotsSearchView);

        RecyclerView lotsRecyclerView = findViewById(R.id.lots_recycler_view);
        LotsAdapter adapter = new LotsAdapter(this, lotsList);
        lotsRecyclerView.setAdapter(adapter);
        lotsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    adapter.getFilter().filter("");
                } else {
                    adapter.getFilter().filter(newText.toString());
                }
                return true;
            }
        });
    }


    //using for logo at top right corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}