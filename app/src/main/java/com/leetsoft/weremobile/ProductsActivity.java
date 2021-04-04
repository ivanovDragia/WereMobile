package com.leetsoft.weremobile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leetsoft.weremobile.adapters.ProductsAdapter;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.Lots;
import com.leetsoft.weremobile.database.Products;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProductsActivity extends AppCompatActivity {
    private static final String TAG = "ProductsActivity";
    Context context = this;
    DatabaseHelper db = new DatabaseHelper(this);

    List<Products> productsList = new ArrayList<>();
    List<Lots> lotsList = new ArrayList<>();

    SearchView searchView;
    public static TextView resultTV;
    Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Log.d(TAG, "onCreate: started.");

        productsList = db.getAllProducts();
        Collections.sort(productsList,
                (o1, o2) -> o1.getName().compareTo(o2.getName()));
        lotsList = db.getAllLots();

        for (Products product : productsList) {
            double quantity = 0;
            product.setQuantity(quantity);

            for (Lots lot : lotsList) {
                if (lot.getProduct_id().equals(product.getId())) {
                    double lotQuantity = lot.getQuantity();
                    quantity = quantity + lotQuantity;
                }
            }
            product.setQuantity(quantity);
            db.updateProduct(product);
        }

        searchView = findViewById(R.id.searchView);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ProductsAdapter adapter = new ProductsAdapter(this, productsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
}