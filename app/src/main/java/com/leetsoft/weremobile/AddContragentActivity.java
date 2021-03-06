package com.leetsoft.weremobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leetsoft.weremobile.database.Contragents;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.util.DataUtil;

import java.util.ArrayList;

public class AddContragentActivity extends AppCompatActivity {
    private static final String TAG = "AddContragentActivity";
    TextView name;
    TextView bullstat;
    TextView VAT;
    TextView city;
    TextView address;
    TextView MRP;
    TextView phoneNumber;

    Button addBtn;
    ArrayList<Contragents> lookupArray;
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contragent);
        Log.d(TAG, "onCreate: started.");

        name=findViewById(R.id.addContragentNameET);
        bullstat=findViewById(R.id.addContragentBullstatET);
        VAT=findViewById(R.id.addContragentVatET);
        city=findViewById(R.id.addContragentCityET);
        address=findViewById(R.id.addContragentAddressET);
        MRP=findViewById(R.id.addContragentMrpET);
        phoneNumber=findViewById(R.id.addContragentPhoneNumberET);

        addBtn=findViewById(R.id.addEditButton);
        addBtn.setText("Add");
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookupArray=db.getAllContragents();

                String nameString=name.getText().toString().trim();
                String bullstatString=bullstat.getText().toString().trim();;
                String VATString=VAT.getText().toString().trim();;
                String cityString=city.getText().toString().trim();;
                String addressString=address.getText().toString().trim();;
                String MRPString=MRP.getText().toString().trim();;
                String phoneNumberString=phoneNumber.getText().toString().trim();;

                Contragents input=new Contragents(DataUtil.randomStringUUID(),nameString,bullstatString,
                        VATString, cityString,
                        addressString, MRPString,
                        phoneNumberString,false);
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
                        (phoneNumberString.isEmpty()||phoneNumberString.length()>10)){
                    Toast.makeText(getApplicationContext(), "Fields cannot be longer then 30 characters or empty!", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(AddContragentActivity.this).create();
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.setMessage("Confirm Add?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", (dialog, which) -> {
                        db.addContragent(input);
                        db.close();
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext() ,ContragentsActivity.class);
                        startActivity(intent);
                    });

                    alertDialog.show();

                }



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
