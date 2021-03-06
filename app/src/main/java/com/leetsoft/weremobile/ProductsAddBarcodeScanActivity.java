package com.leetsoft.weremobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.leetsoft.weremobile.database.Barcodes;
import com.leetsoft.weremobile.database.DatabaseHelper;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ProductsAddBarcodeScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "ProductsScanAcctivity";
    ZXingScannerView ScannerView;
    DatabaseHelper db = new DatabaseHelper(this);
    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);
        Log.d(TAG, "onCreate: started.");
        Bundle bundle=getIntent().getExtras();
        productId=bundle.getString("id");

    }

    @Override
    public void handleResult(Result result) {
        if(result.getBarcodeFormat() == BarcodeFormat.EAN_13){
            Barcodes barcodeToUpload=new Barcodes(result.getText().toString(), productId);
            db.addBarcodes(barcodeToUpload);
            Toast.makeText(this,"Barcode Added!",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Barcode is not EAN_13",Toast.LENGTH_SHORT).show();
        }
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
}
