package com.leetsoft.weremobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leetsoft.weremobile.adapters.ContragentsAdapter;
import com.leetsoft.weremobile.database.Contragents;
import com.leetsoft.weremobile.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class ContragentsActivity extends AppCompatActivity {

    private static final String TAG = "ContragentsActivity";

    DatabaseHelper db = new DatabaseHelper(this);

    List<Contragents> contragentsList = new ArrayList<>();

    SearchView searchView;

    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    ContragentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contragents);
        Log.d(TAG, "onCreate: started.");
        contragentsList = db.getAllContragents();

        searchView = findViewById(R.id.searchView_contragents);


        recyclerView = findViewById(R.id.recycler_view_contragents);
        adapter = new ContragentsAdapter(this, contragentsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if ( TextUtils.isEmpty ( newText ) ) {
                    adapter.getFilter().filter("");
                } else {
                    adapter.getFilter().filter(newText.toString());
                }
                return true;
            }
        });

        floatingActionButton = findViewById(R.id.floatingActionButton);

    }


    @Override
    protected void onStart() {
        //Recycler view doesnt update after adding contragent from AddContragentActivity
        //If u search for the new entry you just added the app crashes with a index out of bounds exception
        //to be fixed
        super.onStart();
        recyclerView.getRecycledViewPool().clear();
        recyclerView.getAdapter().notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        recyclerView.invalidate();
    }

    //using for logo at top right corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void floatingOnClick(View view) {
        Intent intent = new Intent(this,AddContragentActivity.class);
        startActivity(intent);
        //Toast.makeText(getApplicationContext(), "Setting value cannot be longer then 20 characters or empty!", Toast.LENGTH_SHORT).show();
    }
}