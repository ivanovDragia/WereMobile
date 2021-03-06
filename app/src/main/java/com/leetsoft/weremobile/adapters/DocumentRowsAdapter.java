package com.leetsoft.weremobile.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leetsoft.weremobile.R;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.DocumentRows;
import com.leetsoft.weremobile.database.Lots;
import com.leetsoft.weremobile.database.Products;
import com.leetsoft.weremobile.interfaces.OnCreateDocumentRowButtonClickListener;
import com.leetsoft.weremobile.util.DataUtil;

import java.util.List;


public class DocumentRowsAdapter extends RecyclerView.Adapter<DocumentRowsAdapter.ViewHolder> {
    private static final String TAG = "DocumentRowsAdapter";

    Context context;
    List<DocumentRows> documentRowsList;
    List<Products> productsList;
    List<Lots> lotsList;
    OnCreateDocumentRowButtonClickListener listener;

    DatabaseHelper db;
    DataUtil dataUtil;

    public DocumentRowsAdapter(Context context, List<DocumentRows> documentRowsList, OnCreateDocumentRowButtonClickListener listener) {
        this.context = context;
        this.documentRowsList = documentRowsList;
        this.listener = listener;

        db = new DatabaseHelper(context);
        productsList = db.getAllProducts();
        lotsList = db.getAllLots();
        dataUtil = new DataUtil();
    }

    @NonNull
    @Override
    public DocumentRowsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: started");

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_row_recycler_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productIDTV;
        TextView lotIDTV;
        TextView quantityTV;
        TextView sumTV;
        TextView documentIDTV;

        ImageButton deleteButton;
        ImageButton editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productIDTV = itemView.findViewById(R.id.documentRowProductIDTextView);
            lotIDTV = itemView.findViewById(R.id.documentRowLotIDTextView);
            quantityTV = itemView.findViewById(R.id.documentRowQuantityTextView);
            sumTV = itemView.findViewById(R.id.documentRowSumTextView);

            deleteButton = itemView.findViewById(R.id.buttonDeleteRow);
            editButton = itemView.findViewById(R.id.buttonEditRow);


        }

    }

    @Override
    public void onBindViewHolder(@NonNull DocumentRowsAdapter.ViewHolder holder, int position) {
        DocumentRows row = documentRowsList.get(position);
        String lot_number="";
        for (Lots lot: lotsList) {
            if(lot.getId().equals(row.getLot_id())){
                lot_number = lot.getLot_number();
            }
        }
        String productName = dataUtil.getProductName(row.getProduct_id(),productsList);
        holder.productIDTV.setText(String.valueOf(productName));
        holder.lotIDTV.setText(lot_number);
        holder.quantityTV.setText(String.valueOf(row.getQuantity()));
        holder.sumTV.setText(String.valueOf(row.getSum()));


        holder.deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Delete");
            alert.setMessage("Confirm Delete?");
            alert.setPositiveButton("Yes", (dialog, which) -> {
                listener.onDeleteClicked(position,row);
                Toast.makeText(context, "Row is deleted!", Toast.LENGTH_LONG).show();
            });

            alert.setNegativeButton("No", (dialog, which) -> dialog.cancel());

            alert.create().show();
        });

        holder.editButton.setOnClickListener(v -> {
            listener.onEdidClicked(row,position);
        });
    }

    @Override
    public int getItemCount() {
        return documentRowsList.size();
    }

    private void deleteRow(DocumentRows row) {
        db.deleteDocumentRow(row);
        listener.updateQuantity(row);
    }
}
