package com.leetsoft.weremobile;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leetsoft.weremobile.adapters.ProductsAddBarcodeAdapter;
import com.leetsoft.weremobile.database.Barcodes;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.Products;
import com.leetsoft.weremobile.interfaces.OnPorductAddBarcodeClickListener;

import java.util.ArrayList;

public class ProductsAddBarcodeActivity extends AppCompatActivity {
    private static final String TAG = "PrBarcodeActivity";

    String productID;

    TextView nameTV;
    TextView quantityTV;
    TextView priceTV;
    TextView productNumberTV;

    DatabaseHelper db = new DatabaseHelper(this);

    ArrayList<Products> lookupArray=new ArrayList<>();
    Products product;
    ArrayList<Barcodes> barcodesUnfiltered=new ArrayList<>();
    ArrayList<Barcodes> barcodesFiltered=new ArrayList<>();

    Button scanBtn;
    Button inputBtn;
    ProductsAddBarcodeAdapter adapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_add_barcode_layout);
        Log.d(TAG, "onCreate: started.");


        Bundle bundle=getIntent().getExtras();
        productID=bundle.getString("id");

        lookupArray=db.getAllProducts();

        for(int i=0;i<lookupArray.size();i++){
            if(lookupArray.get(i).getId().equals(productID)){
                product=lookupArray.get(i);
            }
        }

        nameTV=findViewById(R.id.ProductsAddBarcodeNameTF);
        quantityTV=findViewById(R.id.ProductsAddBarcodeQuantityTF);
        priceTV=findViewById(R.id.ProductsAddBarcodePriceTF);
        productNumberTV=findViewById(R.id.ProductsAddBarcodeNumberTF);
        scanBtn=findViewById(R.id.ProductsAddBarcodeScanBtn);
        inputBtn=findViewById(R.id.ProductsAddBarcodeInputBtn);

        nameTV.setText(product.getName());
        quantityTV.setText(String.valueOf(product.getQuantity()));
        priceTV.setText(String.valueOf(product.getPrice()));
        productNumberTV.setText(product.getProduct_number());

        barcodesUnfiltered = db.getAllBarcodes();

        if(!barcodesUnfiltered.isEmpty()){
            for(int i=0;i<barcodesUnfiltered.size();i++){
                if(barcodesUnfiltered.get(i).getProduct_id().equals(productID)){
                    barcodesFiltered.add(barcodesUnfiltered.get(i));
                }
            }
        }

        RecyclerView barcodesRecyclerView = findViewById(R.id.ProductsAddBarcodeRecyclerView);
        adapter = new ProductsAddBarcodeAdapter(this, barcodesFiltered, click);
        barcodesRecyclerView.setAdapter(adapter);
        barcodesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        scanBtn.setOnClickListener(v -> {
            if (hasPermission(getApplicationContext(), Manifest.permission.CAMERA)) {
                Intent intent = new Intent(getApplicationContext() , ProductsAddBarcodeScanActivity.class);
                Bundle bundle1 =new Bundle();
                bundle1.putString("id", productID);
                intent.putExtras(bundle1);

                startActivity(intent);
            } else {
                ActivityCompat.requestPermissions(ProductsAddBarcodeActivity.this, new String[]{Manifest.permission.CAMERA}, 102);
            }
        });

        inputBtn.setOnClickListener(v -> {
            final Dialog barcodeDialog = new Dialog(ProductsAddBarcodeActivity.this);
            barcodeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            barcodeDialog.setContentView(R.layout.custom_addbarcode_dialog);
            barcodeDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            barcodeDialog.setCancelable(true);

            final EditText barcodeEditeText=barcodeDialog.findViewById(R.id.inputBarcodeET);
            final Button addBarcodeButton=barcodeDialog.findViewById(R.id.btnAddBarcode);

            addBarcodeButton.setOnClickListener(v1 -> {
                String toCheck=barcodeEditeText.getText().toString().trim();

                if(toCheck.length()==13){
                    if(toCheck.matches("^\\d+$")){
                        String barcadeArray=new String(toCheck.substring(0, toCheck.length()-1));
                        int originalControlNumber=Integer.parseInt(toCheck.substring(toCheck.length()-1));

                        int newControlNumber=0;
                        int sum=0;
                        for(int i=0;i<=11;i++){
                            if(i%2==0){
                                sum+=Character.getNumericValue(barcadeArray.charAt(i));
                            }else if(i%2!=0){
                                sum+=Character.getNumericValue(barcadeArray.charAt(i))*3;
                            }
                        }
                        newControlNumber=10-(sum%10);
                        if(newControlNumber==originalControlNumber){
                            Barcodes barcodeToAdd=new Barcodes(toCheck, productID);
                            db.addBarcodes(barcodeToAdd);

                            barcodesUnfiltered = db.getAllBarcodes();
                            barcodesFiltered.clear();
                            if(!barcodesUnfiltered.isEmpty()){
                                for(int i=0;i<barcodesUnfiltered.size();i++){
                                    if(barcodesUnfiltered.get(i).getProduct_id().equals(productID)){
                                        barcodesFiltered.add(barcodesUnfiltered.get(i));
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                            db.close();
                            barcodeDialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(), "Barcode control number invalid", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "Barcode can only consist of numbers!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid Barcode Length", Toast.LENGTH_SHORT).show();
                }
            });
            barcodeDialog.show();
        });



    }

    private boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
                startActivity(new Intent(getApplicationContext(), ProductsAddBarcodeScanActivity.class));
            } else {
                //permission denied
                Toast.makeText(this, "Permission is Denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    OnPorductAddBarcodeClickListener click = new OnPorductAddBarcodeClickListener(){

        @Override
        public void onDeleteClicked() {
            barcodesUnfiltered = db.getAllBarcodes();
            barcodesFiltered.clear();
            if(!barcodesUnfiltered.isEmpty()){
                for(int i=0;i<barcodesUnfiltered.size();i++){
                    if(barcodesUnfiltered.get(i).getProduct_id().equals(productID)){
                        barcodesFiltered.add(barcodesUnfiltered.get(i));
                    }
                }
            }
            adapter.notifyDataSetChanged();
            db.close();
        }

    };

    @Override
    public void onRestart()
    {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }
}
