package com.leetsoft.weremobile;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leetsoft.weremobile.adapters.DocumentAdapter;
import com.leetsoft.weremobile.database.Contragents;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.Documents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class DocumentsActivity extends AppCompatActivity {
    private static final String TAG = "DocumentsActivity";

    DatabaseHelper db = new DatabaseHelper(this);

    List<Documents> documentsList = new ArrayList<>();


    SearchView searchView;

    HashMap<String, String> mapContragents = new HashMap<>();
    List<Contragents> contragentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        Log.d(TAG, "onCreate: started.");

        documentsList = db.getAllDocuments();
        Collections.sort(documentsList,
                (o1, o2) -> o2.getDate().compareTo(o1.getDate()));

        getContragentName();

        searchView = findViewById(R.id.documentSearchView);

        RecyclerView documentsRecyclerView = findViewById(R.id.documents_recycler_view);
        DocumentAdapter adapter = new DocumentAdapter(this, documentsList, mapContragents);
        documentsRecyclerView.setAdapter(adapter);
        documentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    private void getContragentName() {
        contragentsList = db.getAllContragents();
        for (Contragents contragent : contragentsList) {
            mapContragents.put(contragent.getId(),contragent.getName());
        }
    }

    //using for logo at top right corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}