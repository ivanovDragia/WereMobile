package com.leetsoft.weremobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    //buttons
    private Button btnProducts;
    private Button btnLots;
    private Button btnContragents;
    private Button btnDocuments;
    private Button btnCreateDocuments;
    private Button btnSettings;
    private Button btnSync;
    private ImageView logo;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo = findViewById(R.id.logo);
        logo.startAnimation(AnimationUtils.loadAnimation(
                getApplicationContext(),
                R.anim.zoom_in_fade_in
        ));

        overridePendingTransition(R.anim.zoom_in_fade_in_fast, R.anim.zoom_none_fade_none);

        //widgets
        btnProducts = findViewById(R.id.btnProducts);
        btnLots = findViewById(R.id.btnLots);
        btnContragents = findViewById(R.id.btnContragents);
        btnDocuments = findViewById(R.id.btnDocuments);
        btnCreateDocuments = findViewById(R.id.btnCreateDocuments);
        btnSettings = findViewById(R.id.btnSettings);
        btnSync = findViewById(R.id.btnSync);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logo.startAnimation(AnimationUtils.loadAnimation(
                getApplicationContext(),
                R.anim.zoom_in_fade_in
        ));
        overridePendingTransition(R.anim.zoom_in_fade_in_fast, R.anim.zoom_none_fade_none);
    }

    //products button onClick
    public void productsOnClick(View view) {
        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }

    //products button onClick
    public void contragentsOnClick(View view) {
        Intent intent = new Intent(this, ContragentsActivity.class);
        startActivity(intent);
    }

    //lots button onClick
    public void lotsOnClick(View view) {
        Intent intent = new Intent(this, LotsActivity.class);
        startActivity(intent);
    }

    //documents button onClick
    public void documentsOnClick(View view) {
        Intent intent = new Intent(this, DocumentsActivity.class);
        startActivity(intent);
    }

    //create document button onClick
    public void createDocumentOnClick(View view) {
        Intent intent = new Intent(this, CreateDocumentActivity.class);
        startActivity(intent);
    }

    //settings button onClick
    public void settingsOnClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    //sync button onClick
    public void syncOnClick(View view) {
        Intent intent = new Intent(this, SyncActivity.class);
        startActivity(intent);
    }
}