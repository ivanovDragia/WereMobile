package com.leetsoft.weremobile.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leetsoft.weremobile.EditContragentActivity;
import com.leetsoft.weremobile.R;
import com.leetsoft.weremobile.database.Contragents;
import com.leetsoft.weremobile.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class ContragentsAdapter extends RecyclerView.Adapter<ContragentsAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "ContragentsAdapter";
    RecyclerView contragentsRecyclerView;
    Context context;
    List<Contragents> contragentsList;
    List<Contragents> allContragentsList;
    DatabaseHelper db;

    public ContragentsAdapter(Context context, List<Contragents> contragentsList) {
        this.context = context;
        this.contragentsList = contragentsList;
        allContragentsList = new ArrayList<>();
        allContragentsList.addAll(contragentsList);
        db = new DatabaseHelper(context);
    }



    @NonNull
    @Override
    public ContragentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: started");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contragents_recycler_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContragentsAdapter.ViewHolder holder, int position) {
        Contragents contragent = contragentsList.get(position);
        holder.nameTV.setText(contragent.getName());
        holder.bullstatTV.setText(contragent.getBulstat());
        holder.vatTV.setText(contragent.getVAT_number());
        holder.cityTV.setText(contragent.getCity());
        holder.addressTV.setText(contragent.getAddress());
        holder.mrpTV.setText(contragent.getMRP());
        holder.phoneNumberTV.setText(contragent.getPhone_number());

        holder.delete.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setMessage("Confirm Delete?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.deleteContragent(contragent);
                    contragentsList.remove(contragent);
                    notifyDataSetChanged();
                    dialog.dismiss();
                    db.close();
                }
            });
            alertDialog.show();
        });

        holder.edit.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setMessage("Confirm Edit?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", (dialog, which) -> {
                Intent intent = new Intent(context , EditContragentActivity.class);
                String name=contragent.getName();
                String bullstat=contragent.getBulstat();
                String vat=contragent.getVAT_number();
                String city=contragent.getCity();
                String address=contragent.getAddress();
                String mrp=contragent.getMRP();
                String phone=contragent.getPhone_number();

                Bundle bundle=new Bundle();
                bundle.putString("id",contragent.getId());
                bundle.putString("name", name);
                bundle.putString("bullstat", bullstat);
                bundle.putString("vat", vat);
                bundle.putString("city", city);
                bundle.putString("address", address);
                bundle.putString("mrp", mrp);
                bundle.putString("phone", phone);
                intent.putExtras(bundle);

                context.startActivity(intent);
                dialog.dismiss();
            });
            alertDialog.show();
        });


    }

    @Override
    public int getItemCount() {
        return contragentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV;
        TextView bullstatTV;
        TextView vatTV;
        TextView cityTV;
        TextView addressTV;
        TextView mrpTV;
        TextView phoneNumberTV;
        ImageButton edit;
        ImageButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameContragentTextView);
            bullstatTV = itemView.findViewById(R.id.bullstatContragentTextView);
            vatTV = itemView.findViewById(R.id.VATContragentTextView);
            cityTV = itemView.findViewById(R.id.cityContragentTextView);
            addressTV = itemView.findViewById(R.id.addressContragentTextView);
            mrpTV = itemView.findViewById(R.id.MRPContragentTextView);
            phoneNumberTV = itemView.findViewById(R.id.phoneNumberContragentTextView);
            edit=itemView.findViewById(R.id.buttonEditContragent);
            delete=itemView.findViewById(R.id.buttonDeleteContragent);



        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((Contragents) resultValue).getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Contragents> contragentsFiltred = new ArrayList<>();
                if (constraint != null) {
                    for (Contragents contragent : allContragentsList) {
                        if (contragent.getName().toLowerCase().contains(constraint.toString().toLowerCase()) || contragent.getBulstat().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            contragentsFiltred.add(contragent);
                        }
                    }

                    filterResults.values = contragentsFiltred;
                    filterResults.count = contragentsFiltred.size();

                    if(filterResults.count == 0){
                        filterResults.values = contragentsList;
                        filterResults.count = contragentsList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contragentsList.clear();
                if (results != null && results.count > 0) {
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof Contragents) {
                            contragentsList.add((Contragents) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {

                    contragentsList.addAll(allContragentsList);
                    notifyDataSetChanged();
                }
            }
        };
    }
}
