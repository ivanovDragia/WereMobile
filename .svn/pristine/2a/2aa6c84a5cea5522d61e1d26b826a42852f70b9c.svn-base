package com.leetsoft.weremobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leetsoft.weremobile.database.Contragents;
import com.leetsoft.weremobile.database.DatabaseHelper;

import java.util.ArrayList;


public class EditContragentActivity extends AppCompatActivity {
    private static final String TAG = "EditContragentActivity";
    TextView name;
    TextView bullstat;
    TextView VAT;
    TextView city;
    TextView address;
    TextView MRP;
    TextView phoneNumber;

    Button editBtn;

    DatabaseHelper db = new DatabaseHelper(this);

    String currentId;
    ArrayList<Contragents> lookupArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contragent);
        Log.d(TAG, "onCreate: started.");

        Bundle bundle=getIntent().getExtras();
        currentId=bundle.getString("id");
        name=findViewById(R.id.addContragentNameET);
        name.setText(bundle.getString("name"));
        bullstat=findViewById(R.id.addContragentBullstatET);
        bullstat.setText(bundle.getString("bullstat"));
        VAT=findViewById(R.id.addContragentVatET);
        VAT.setText(bundle.getString("vat"));
        city=findViewById(R.id.addContragentCityET);
        city.setText(bundle.getString("city"));
        address=findViewById(R.id.addContragentAddressET);
        address.setText(bundle.getString("address"));
        MRP=findViewById(R.id.addContragentMrpET);
        MRP.setText(bundle.getString("mrp"));
        phoneNumber=findViewById(R.id.addContragentPhoneNumberET);
        phoneNumber.setText(bundle.getString("phone"));

        editBtn=findViewById(R.id.addEditButton);
        editBtn.setText("Edit");
        lookupArray=db.getAllContragents();
        int z=0;
        for(int i=0;i<lookupArray.size();i++){
            if(bundle.getString("bullstat").equals(lookupArray.get(i).getBulstat())){
                z=i;
            }
        }
        lookupArray.remove(z);




        editBtn.setOnClickListener(v -> {

            String nameString=name.getText().toString().trim();
            String bullstatString=bullstat.getText().toString().trim();
            String VATString=VAT.getText().toString().trim();
            String cityString=city.getText().toString().trim();
            String addressString=address.getText().toString().trim();
            String MRPString=MRP.getText().toString().trim();
            String phoneNumberString=phoneNumber.getText().toString().trim();

            Contragents input=new Contragents(nameString,bullstatString,
                    VATString, cityString,
                    addressString, MRPString,
                    phoneNumberString);
            input.setId(currentId);



            int x=0;
            for(int i=0;i<lookupArray.size();i++){
                if(bullstatString.equals(lookupArray.get(i).getBulstat())){
                    x++;
                }
            }

            if(x>0){
                Toast.makeText(getApplicationContext(), "Bullstat already exists, use edit instead.", Toast.LENGTH_SHORT).show();
            }else if((nameString.isEmpty() || nameString.length()>30)||(bullstatString.isEmpty() || bullstatString.length()>30)||(VATString.isEmpty() || VATString.length()>30)||
                    (cityString.isEmpty() || cityString.length()>30)||(addressString.isEmpty()||addressString.length()>70)||(MRPString.isEmpty()||MRPString.length()>30)||
                    (phoneNumberString.isEmpty()||phoneNumberString.length()>30)){
                Toast.makeText(getApplicationContext(), "Fields cannot be longer then 30 characters or empty!", Toast.LENGTH_SHORT).show();
            }else{
                AlertDialog alertDialog = new AlertDialog.Builder(EditContragentActivity.this).create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setMessage("Confirm Edit?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", (dialog, which) -> {
                    db.updateContragent(input);
                    db.close();
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext() ,ContragentsActivity.class);
                    startActivity(intent);
                });

                alertDialog.show();

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
