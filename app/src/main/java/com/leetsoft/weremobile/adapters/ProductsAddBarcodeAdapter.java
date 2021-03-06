package com.leetsoft.weremobile.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leetsoft.weremobile.R;
import com.leetsoft.weremobile.database.Barcodes;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.interfaces.OnPorductAddBarcodeClickListener;

import java.util.List;

public class ProductsAddBarcodeAdapter extends RecyclerView.Adapter<ProductsAddBarcodeAdapter.ViewHolder> {
    private static final String TAG = "ContragentsAdapter";
    Context context;
    List<Barcodes> barcodesList;
    DatabaseHelper db;
    OnPorductAddBarcodeClickListener mInterface;

    public ProductsAddBarcodeAdapter(Context context, List<Barcodes> barcodesList, OnPorductAddBarcodeClickListener mInterface) {
        this.context = context;
        this.barcodesList = barcodesList;
        db = new DatabaseHelper(context);
        this.mInterface=mInterface;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView barcodeTV;
        ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            barcodeTV = itemView.findViewById(R.id.ProductsAddBarcodeBarcodeTV);
            deleteBtn=itemView.findViewById(R.id.ProductsAddBarcodeDeleteBtn);
        }
    }


    @NonNull
    @Override
    public ProductsAddBarcodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: started");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_add_barcode_recycler_layout, parent, false);
        ProductsAddBarcodeAdapter.ViewHolder holder = new ProductsAddBarcodeAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAddBarcodeAdapter.ViewHolder holder, int position) {
        Barcodes barcode = barcodesList.get(position);
        holder.barcodeTV.setText(barcode.getBarcode());
        holder.deleteBtn.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setMessage("Confirm Delete?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Barcodes toDelete=new Barcodes(barcode.getId(), barcode.getBarcode(), barcode.getProduct_id());



                    db.deleteBarcode(toDelete);
                    barcodesList.remove(toDelete);
                    notifyDataSetChanged();
                    db.close();
                    alertDialog.dismiss();
                    mInterface.onDeleteClicked();
                }
            });
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return barcodesList.size();
    }
}
