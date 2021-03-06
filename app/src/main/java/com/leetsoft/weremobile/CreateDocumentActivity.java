package com.leetsoft.weremobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leetsoft.weremobile.adapters.DocumentRowsAdapter;
import com.leetsoft.weremobile.database.Barcodes;
import com.leetsoft.weremobile.database.Contragents;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.DocumentRows;
import com.leetsoft.weremobile.database.Documents;
import com.leetsoft.weremobile.database.Lots;
import com.leetsoft.weremobile.database.Products;
import com.leetsoft.weremobile.interfaces.OnCreateDocumentRowButtonClickListener;
import com.leetsoft.weremobile.util.DataUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;



public class CreateDocumentActivity extends AppCompatActivity {
    private static final String TAG = "CreateDocumentActivity";

    DatabaseHelper db = new DatabaseHelper(this);
    DataUtil dataUtil = new DataUtil();

    private Spinner supplierSpinner, destinationSpinner;
    private Button btnSave, btnAddRow;
    private TextView sumTV;
    private Toolbar toolbar;
    private ImageButton btnBack;

    private List<String> contragentsNameList = new ArrayList<>();
    private List<Contragents> contragentsList = new ArrayList<>();

    private List<String> productsNameList = new ArrayList<>();
    private List<Products> productsList = new ArrayList<>();

    private List<String> lotsStringList = new ArrayList<>();
    private List<Lots> lotsList = new ArrayList<>();

    private List<String> lotIdValues = new ArrayList<>();
    private List<DocumentRows> documentRows = new ArrayList<>();
    private List<DocumentRows> unsavedDocumentRows;
    private List<Barcodes> barcodesList = new ArrayList<>();

    private int documentNumber = 0;
    private String productID = "0";
    private double sum = 0;
    private double quantity = 0;
    private double lotValue = 0;
    public static String scanedBarcode = "";
    public int scanedProductID = 0;

    DocumentRowsAdapter adapter;

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Back tapped");
        alert.setMessage("Sure?");
        alert.setPositiveButton("OK!", (dialogEdit, which) -> {
            for (DocumentRows row : unsavedDocumentRows) {
                listener.updateQuantity(row);
            }

            super.onBackPressed();
        });
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (DocumentRows row : unsavedDocumentRows) {
            listener.updateQuantity(row);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_document);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnBack = findViewById(R.id.imageButton);
        supplierSpinner = findViewById(R.id.supplierSpinner);
        destinationSpinner = findViewById(R.id.destinationSpinner);
        btnSave = findViewById(R.id.btnSave);
        btnAddRow = findViewById(R.id.btnAddRow);
        sumTV = findViewById(R.id.sumTextView);

        unsavedDocumentRows = new ArrayList<>();

        contragentsList = db.getAllContragents();
        barcodesList = db.getAllBarcodes();

        for (Contragents contragent : contragentsList) {
            contragentsNameList.add(contragent.getName());
        }

        productsList = db.getAllProducts();

        for (Products product : productsList) {
            productsNameList.add(product.getName());
        }

        lotsList = db.getAllLots();
        documentNumber = getDocumentNumber();

        addItemsOnSupplierSpinner();
        addItemsOnDestinationSpinner();

        btnBack.setOnClickListener(v -> {
            for (DocumentRows row : unsavedDocumentRows) {
                listener.updateQuantity(row);
            }

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });


        btnSave.setOnClickListener(v -> {

            Documents document = new Documents();
            String supplierID = getSupplierId();
            String destinationID = getDestinationId();
            Date date = new Date();


            document.setId(DataUtil.randomStringUUID());
            document.setSource_id(supplierID);
            document.setDestination_id(destinationID);
            document.setDate(date);
            document.setDocument_number(documentNumber);

            String id = db.addDocument(document);

            for (DocumentRows row : unsavedDocumentRows) {
                row.setDocument_id(id);
                db.addDocumentRow(row);
            }

            Toast.makeText(CreateDocumentActivity.this,
                    "Document saved in database with number " + document.getDocument_number(),
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        btnAddRow.setOnClickListener(v -> {
            showAddRowDialog();
            updateRows();
        });
    }


    //document number generator based on date
    private int getDocumentNumber() {
        Random random = new Random();
        int abc = 100 + random.nextInt(900);
        int value = db.getDocumentCount() + abc;
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("ddMM").format(new Date());
        String inv = date + value;
        return Integer.parseInt(inv);

    }

    private String getSupplierId() {
        String id = "0";
        for (Contragents contragent : contragentsList) {
            if (contragent.getName().trim().equals(supplierSpinner.getSelectedItem().toString())) {
                id = contragent.getId();
            }
        }
        return id;
    }

    private String getDestinationId() {
        String id = "0";
        for (Contragents contragent : contragentsList) {
            if (contragent.getName().trim().equals(destinationSpinner.getSelectedItem().toString())) {
                id = contragent.getId();
            }
        }
        return id;
    }

    //using for logo at top right corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }


    public void addItemsOnDestinationSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, contragentsNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(dataAdapter);
    }

    public void addItemsOnSupplierSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, contragentsNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supplierSpinner.setAdapter(dataAdapter);
    }

    private void showAddRowDialog() {
        final Dialog dialog = new Dialog(CreateDocumentActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_document_row_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        final Spinner lotSpinner = dialog.findViewById(R.id.lotSpinner);
        final Spinner productSpinner = dialog.findViewById(R.id.productSpinner);
        final Button btnAddRowDialog = dialog.findViewById(R.id.btnAddRowDialog);
        final EditText quantityEditText = dialog.findViewById(R.id.quantityEditText);
        final Button btnScanBarcode = dialog.findViewById(R.id.btnScanProductRowDialog);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, productsNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(dataAdapter);


        ArrayAdapter<String> dataLotAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lotsStringList);
        dataLotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lotSpinner.setAdapter(dataLotAdapter);

        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lotsStringList.clear();
                lotIdValues.clear();


                for (Products product : productsList) {
                    if (product.getName().trim().equals(productSpinner.getSelectedItem().toString())) {
                        productID = product.getId();
                    }
                }

                for (Lots lot : lotsList) {
                    if (productID.equals(lot.getProduct_id())) {
                        lotsStringList.add("LN: " + lot.getLot_number() + " Quantity: " + lot.getQuantity());
                        lotValue = lot.getQuantity();
                        lotIdValues.add(String.valueOf(lot.getLot_number()));
                    }

                }
                if (lotsStringList.isEmpty()) {
                    lotsStringList.add("LN: 00000" + " Quantity: 0");
                }
                // SubCategories
                dataLotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        if (!scanedBarcode.isEmpty()) {

            productSpinner.setSelection(getIndex(productSpinner, getProductNameByBarcode()));
            productSpinner.setEnabled(false);
            btnScanBarcode.setVisibility(View.INVISIBLE);

        }


        btnScanBarcode.setOnClickListener(v -> {
            if (hasPermission(getApplicationContext(), Manifest.permission.CAMERA)) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 102);
            }
            dialog.dismiss();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Scaner");
            alert.setMessage("Confirm Barcode Scaned");
            alert.setPositiveButton("Yes", (dialog1, which) -> {
                showAddRowDialog();
            });
            alert.setNegativeButton("No", (dialog12, which) -> {
                dialog.show();
            });
            alert.create().show();
        });

        btnAddRowDialog.setOnClickListener(v -> {

            if (!quantityEditText.getText().toString().equals("")) {
                double quantity = Double.parseDouble(quantityEditText.getText().toString());

                if (quantity > 0) {
                    String spinerValue = lotSpinner.getSelectedItem().toString();
                    String[] splited = spinerValue.split(" ");

                    lotValue = Double.parseDouble(splited[splited.length - 1]);


                    if (quantity > lotValue) {
                        Toast.makeText(this, "Enter value lower or equal to lot quantity.", Toast.LENGTH_SHORT).show();
                    } else {
                        DocumentRows row = new DocumentRows();
                        row.setId(DataUtil.randomStringUUID());

                        row.setQuantity(quantity);

                        row.setProduct_id(productID);
                        String lot_number = lotIdValues.get(lotSpinner.getSelectedItemPosition());
                        String lot_id = "";
                        for (Lots lot : lotsList) {
                            if (lot.getLot_number().equals(lot_number)) {
                                lot_id = lot.getId();
                            }
                        }
                        row.setLot_id(lot_id);

                        double price = 0;
                        for (Products product : productsList) {
                            if (product.getId().equals(productID)) {
                                price = product.getPrice();
                            }
                        }
                        row.setSum(dataUtil.round(quantity * price, 2));

                        unsavedDocumentRows.add(row);

                        for (Lots lot : lotsList) {
                            if (lot.getId().equals(lot_id)) {
                                double lotQuantity = lot.getQuantity();
                                lot.setQuantity(lotQuantity - quantity);
                                db.updateLot(lot);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        updateRows();
                        dialog.dismiss();
                    }

                } else {
                    Toast.makeText(this, "Value can't be 0", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter value!", Toast.LENGTH_SHORT).show();
            }

        });
        scanedBarcode = "";
        //Toast.makeText(this, "Barcode: " + scanedBarcode, Toast.LENGTH_LONG).show();
        dialog.show();
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    private String getProductNameByBarcode() {
        for (Barcodes barcode : barcodesList) {
            if (barcode.getBarcode().equals(scanedBarcode)) {
                return dataUtil.getProductName(barcode.getProduct_id(), productsList);
            }
        }
        return "";
    }

    private void updateRows() {
        sum = 0;
        for (DocumentRows documentRows : unsavedDocumentRows) {
            sum = sum + documentRows.getSum();
        }
        sumTV.setText("Sum: " + dataUtil.round(sum, 2));
        RecyclerView recyclerView = findViewById(R.id.document_row_recycler_view);
        adapter = new DocumentRowsAdapter(this, unsavedDocumentRows, listener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getParent()));
    }

    private void showEditRowDialog(DocumentRows row) {
        final Dialog dialog = new Dialog(CreateDocumentActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_document_row_edit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        final TextView productName = dialog.findViewById(R.id.productTextView);
        final TextView lotID = dialog.findViewById(R.id.lotTextView);
        final Button btnEditRowDialog = dialog.findViewById(R.id.btnEditRowDialog);
        final EditText newQuantityEditText = dialog.findViewById(R.id.newQuantityEditText);

        productName.setText(dataUtil.getProductName(row.getProduct_id(), productsList));

        for (Lots lot : lotsList) {
            if (lot.getId().equals(row.getLot_id())) {

                lotID.setText(String.valueOf(lot.getLot_number()) + " Quantity: " + (lot.getQuantity() + row.getQuantity()));
            }
        }


        btnEditRowDialog.setOnClickListener(v -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Edit");
            alert.setMessage("Confirm Edit?");
            alert.setPositiveButton("Yes", (dialogEdit, which) -> {
                //db.deleteDocumentRow(row);
                unsavedDocumentRows.remove(row);
                for (Lots lot : lotsList) {
                    if (lot.getId().equals(row.getLot_id())) {
                        double lotQuantity = lot.getQuantity();
                        lot.setQuantity(lot.getQuantity() + row.getQuantity());
                        db.updateLot(lot);
                    }
                }
                updateRows();

                if (!newQuantityEditText.getText().toString().equals("")) {

                    String quantityStr = newQuantityEditText.getText().toString();
                    quantity = Double.parseDouble(quantityStr);
                    if (quantity > 0) {
                        for (Lots lot : lotsList) {
                            if (lot.getId().equals(row.getLot_id())) {
                                lotValue = lot.getQuantity();
                            }
                        }
                        if (quantity > lotValue) {
                            Toast.makeText(this, "Enter value lower or equal to lot quantity.", Toast.LENGTH_SHORT).show();
                        } else {
                            row.setQuantity(quantity);

                            double price = 0;
                            for (Products product : productsList) {
                                if (product.getId().equals(productID)) {
                                    price = product.getPrice();
                                }
                            }
                            row.setSum(dataUtil.round(quantity * price, 2));

                            //db.addDocumentRow(row);
                            unsavedDocumentRows.add(row);
                            for (Lots lot : lotsList) {
                                if (lot.getId().equals(row.getLot_id())) {
                                    double lotQuantity = lot.getQuantity();
                                    lot.setQuantity(lot.getQuantity() - row.getQuantity());
                                    db.updateLot(lot);
                                }
                            }

                            updateRows();

                            dialog.dismiss();
                        }
                    } else {
                        Toast.makeText(this, "Value can't be 0", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Enter value!", Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton("No", (dialogEdit, which) -> dialog.cancel());

            alert.create().show();
        });
        dialog.show();
    }


    private boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
                startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));
            } else {
                //permission denied
                Toast.makeText(this, "Permission is Denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    OnCreateDocumentRowButtonClickListener listener = new OnCreateDocumentRowButtonClickListener() {
        @Override
        public void onDeleteClicked(int id, DocumentRows row) {
            for (Lots lot : lotsList) {
                if (lot.getId().equals(row.getLot_id())) {
                    double lotQuantity = lot.getQuantity();
                    lot.setQuantity(lot.getQuantity() + row.getQuantity());
                    db.updateLot(lot);
                }
            }
            unsavedDocumentRows.remove(id);
            updateRows();
        }

        @Override
        public void updateQuantity(DocumentRows row) {
            for (Lots lot : lotsList) {
                if (lot.getId().equals(row.getLot_id())) {
                    lot.setQuantity(lot.getQuantity() + row.getQuantity());
                    db.updateLot(lot);
                }
            }
        }

        @Override
        public void onEdidClicked(DocumentRows row, int position) {
            showEditRowDialog(row);
            updateRows();

        }

    };
}