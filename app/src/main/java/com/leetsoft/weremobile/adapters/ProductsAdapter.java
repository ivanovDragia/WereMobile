package com.leetsoft.weremobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leetsoft.weremobile.ProductsAddBarcodeActivity;
import com.leetsoft.weremobile.R;
import com.leetsoft.weremobile.database.Products;

import java.util.ArrayList;
import java.util.List;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "ProductsAdapter";
    RecyclerView productRecyclerView;
    Context context;
    List<Products> productsList;
    List<Products> allProductsList;

    public ProductsAdapter(Context context, List<Products> productsList) {
        this.context = context;
        this.productsList = productsList;
        allProductsList = new ArrayList<>();
        allProductsList.addAll(productsList);
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: started");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_recycler_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
        Products product = productsList.get(position);
        holder.nameTV.setText(product.getName());
        holder.quantityTV.setText(String.valueOf(product.getQuantity()));
        holder.priceTV.setText(String.valueOf(product.getPrice()));
        holder.productNumberTV.setText(product.getProduct_number());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductsAddBarcodeActivity.class);
                intent.putExtra("id",product.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTV;
        TextView quantityTV;
        TextView priceTV;
        TextView productNumberTV;
        GridLayout parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTextView);
            quantityTV = itemView.findViewById(R.id.quantityTextView);
            priceTV = itemView.findViewById(R.id.priceTextView);
            productNumberTV = itemView.findViewById(R.id.productNumberTextView);
            parentLayout = itemView.findViewById(R.id.products_layout);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((Products) resultValue).getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Products> productsFiltred = new ArrayList<>();
                if (constraint != null) {
                    for (Products product : allProductsList) {
                        if (product.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            productsFiltred.add(product);
                        }
                    }

                    filterResults.values = productsFiltred;
                    filterResults.count = productsFiltred.size();

                    if(filterResults.count == 0){
                        filterResults.values = productsList;
                        filterResults.count = productsList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productsList.clear();
                if (results != null && results.count > 0) {
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof Products) {
                            productsList.add((Products) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {

                    productsList.addAll(allProductsList);
                    notifyDataSetChanged();
                }
            }
        };
    }
}
