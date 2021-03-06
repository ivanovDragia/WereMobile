package com.leetsoft.weremobile;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leetsoft.weremobile.adapters.CustomSettingsAdapter;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.Settings;
import com.leetsoft.weremobile.interfaces.OnSettingsAdapterButtonClickListener;
import com.leetsoft.weremobile.util.DataUtil;

import java.util.ArrayList;


public class SettingsActivity extends AppCompatActivity {
    Button btnAddSetting;
    private CustomSettingsAdapter sAdapter;
    private ListView settingsListView;
    ArrayList<Settings> settings = new ArrayList<>();


    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsListView = findViewById(R.id.settingsListView);
        ShowSettings();
        btnAddSetting = findViewById(R.id.btnAddSetting);
        btnAddSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomSettingDialog();
            }
        });
    }

    private void ShowSettings() {
        settings = db.getAllSettings();
        sAdapter = new CustomSettingsAdapter(this, settings, click);
        settingsListView.setAdapter(sAdapter);
    }

    private void showCustomSettingDialog() {
        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_settings_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);


        final EditText nameEt = dialog.findViewById(R.id.editTextName);
        final EditText valueEt = dialog.findViewById(R.id.editTextValue);
        final EditText descriptionEt = dialog.findViewById(R.id.editTextDescription);
        Button addSettingButton = dialog.findViewById(R.id.btnAddSetting);


        addSettingButton.setOnClickListener(v -> {
            String name = nameEt.getText().toString().trim();
            String value = valueEt.getText().toString().trim();
            String description = descriptionEt.getText().toString().trim();
            if(name.isEmpty() || name.length()>10){
                Toast.makeText(getApplicationContext(), "Setting name cannot be longer then 10 characters or empty!", Toast.LENGTH_SHORT).show();
            }else if(value.isEmpty() || value.length()>20){
                Toast.makeText(getApplicationContext(), "Setting value cannot be longer then 20 characters or empty!", Toast.LENGTH_SHORT).show();
            }else{
                Settings setting = new Settings(name, value, description);
                setting.setId(DataUtil.randomStringUUID());
                db.addSetting(setting);
                settings.add(setting);
                dialog.dismiss();
                ShowSettings();
            }

        });
        dialog.show();
    }


    private void showCustomEditDialog(String n, String v, String d, String i) {
        String id=i;
        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_edit_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);


        final EditText nameEt = dialog.findViewById(R.id.editTextName);
        nameEt.setText(n);
        final EditText valueEt = dialog.findViewById(R.id.editTextValue);
        valueEt.setText(v);
        final EditText descriptionEt = dialog.findViewById(R.id.editTextDescription);
        descriptionEt.setText(d);
        Button addSettingButton = dialog.findViewById(R.id.btnAddSetting);



        addSettingButton.setOnClickListener(v1 -> {
            String name = nameEt.getText().toString().trim();
            String value = valueEt.getText().toString().trim();
            String description = descriptionEt.getText().toString().trim();

            if(name.isEmpty() || name.length()>10){
                Toast.makeText(getApplicationContext(), "Setting name cannot be longer then 10 characters or empty!", Toast.LENGTH_SHORT).show();
            }else if(value.isEmpty() || value.length()>20){
                Toast.makeText(getApplicationContext(), "Setting value cannot be longer then 20 characters or empty!", Toast.LENGTH_SHORT).show();
            }else{
                Settings setting = new Settings(id ,name, value, description);
                db.updateSetting(setting);
                dialog.dismiss();
                ShowSettings();
            }


        });
        dialog.show();
    }







    //using for logo at top right corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    OnSettingsAdapterButtonClickListener click = new OnSettingsAdapterButtonClickListener() {

        @Override
        public void onEditClicked(View view, int position) {
                showCustomEditDialog(sAdapter.getLastSelectedName(),sAdapter.getLastSelectedValue(),sAdapter.getLastSelectedDescription(), sAdapter.getI());
        }

        @Override
        public void onDeleteClicked() {



            ShowSettings();
        }
    };
}