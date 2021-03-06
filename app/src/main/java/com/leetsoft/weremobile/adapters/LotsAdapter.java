package com.leetsoft.weremobile.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leetsoft.weremobile.R;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.Lots;
import com.leetsoft.weremobile.database.Products;
import com.leetsoft.weremobile.util.DataUtil;

import java.util.ArrayList;
import java.util.List;


public class LotsAdapter extends RecyclerView.Adapter<LotsAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "LotsAdapter";
    Context context;
    List<Lots> lotsList;
    List<Lots> allLotsList;

    List<Products> productsList;
    DataUtil dataUtil = new DataUtil();


    public LotsAdapter(Context context, List<Lots> lotsList) {
        this.context = context;
        this.lotsList = lotsList;
        allLotsList = new ArrayList<>();
        allLotsList.addAll(lotsList);

        DatabaseHelper db = new DatabaseHelper(context);
        productsList = db.getAllProducts();
    }

    @NonNull
    @Override
    public LotsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: started");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lots_recycler_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LotsAdapter.ViewHolder holder, int position) {
        Lots lot = lotsList.get(position);

        String productID = lot.getProduct_id();
        String productName = dataUtil.getProductName(productID, productsList);
        holder.productIDTV.setText(productName);
        //holder.productIDTV.setText(String.valueOf(lot.getProduct_id()));
        holder.quantityTV.setText(String.valueOf(lot.getQuantity()));

        //format date from database
        holder.expirationDateTV.setText(dataUtil.formatDate(lot.getExpiration_date()));

        holder.lotNumberTV.setText(lot.getLot_number());
    }

    @Override
    public int getItemCount() {
        return lotsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productIDTV;
        TextView quantityTV;
        TextView expirationDateTV;
        TextView lotNumberTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productIDTV = itemView.findViewById(R.id.productIdTextView);
            quantityTV = itemView.findViewById(R.id.quantityLotsTextView);
            expirationDateTV = itemView.findViewById(R.id.expirationDateTextView);
            lotNumberTV = itemView.findViewById(R.id.lotNumberTextView);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((Lots) resultValue).getLot_number();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Lots> lotsFiltred = new ArrayList<>();
                if (constraint != null) {
                    for (Lots lot : allLotsList) {
                        String productName = dataUtil.getProductName(lot.getProduct_id(),productsList);
                        if (lot.getLot_number().contains(constraint.toString()) || productName.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            lotsFiltred.add(lot);
                        }
                    }

                    filterResults.values = lotsFiltred;
                    filterResults.count = lotsFiltred.size();

                    if(filterResults.count == 0){
                        filterResults.values = lotsList;
                        filterResults.count = lotsList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                lotsList.clear();
                if (results != null && results.count > 0) {
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof Lots) {
                            lotsList.add((Lots) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {

                    lotsList.addAll(allLotsList);
                    notifyDataSetChanged();
                }
            }
        };
    }
}
