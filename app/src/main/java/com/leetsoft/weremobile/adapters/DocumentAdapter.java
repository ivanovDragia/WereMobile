package com.leetsoft.weremobile.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leetsoft.weremobile.DocumentInformationActivity;
import com.leetsoft.weremobile.R;
import com.leetsoft.weremobile.database.Contragents;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.DocumentRows;
import com.leetsoft.weremobile.database.Documents;
import com.leetsoft.weremobile.util.DataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "DocumentAdapter";

    Context context;
    List<Documents> documentsList;
    List<Documents> allDocumentsList;
    HashMap<String, String> mapContragents;
    List<String> contragentsNameList = new ArrayList<>();
    List<Contragents> contragentsList = new ArrayList<>();




    DatabaseHelper db = new DatabaseHelper(context);

    public DocumentAdapter(Context context, List<Documents> documentsList, HashMap<String, String> mapContragents) {
        this.context = context;
        this.documentsList = documentsList;
        this.mapContragents = mapContragents;

        allDocumentsList = new ArrayList<>();
        allDocumentsList.addAll(documentsList);
        db = new DatabaseHelper(context);

        contragentsList = db.getAllContragents();

        for (Contragents contragent : contragentsList) {
            contragentsNameList.add(contragent.getName());
        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: started");
        //Log.d(TAG, "onCreateViewHolder: Documents list count " + getItemCount());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_recycler_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView sourceIDTV;
        TextView destinationIDTV;
        TextView documentNumberTV;
        TextView dateTV;
        ImageButton deleteButton;
        ImageButton editButton;

        GridLayout parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sourceIDTV = itemView.findViewById(R.id.documentSourceTextView);
            destinationIDTV = itemView.findViewById(R.id.documentDestinationIDTextView);
            documentNumberTV = itemView.findViewById(R.id.documentDocNumberTextView);
            dateTV = itemView.findViewById(R.id.documentDateTextView);
            parentLayout = itemView.findViewById(R.id.documents_layout);
            editButton=itemView.findViewById(R.id.buttonEditDocument);
            deleteButton=itemView.findViewById(R.id.buttonDeleteDocument);
        }


        @Override
        public void onClick(View v)
        {
            Context context = v.getContext();
            Intent intent = new Intent(context, DocumentInformationActivity.class);
            context.startActivity(intent);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Documents document = documentsList.get(position);

        holder.sourceIDTV.setText(mapContragents.get(document.getSource_id()));
        holder.destinationIDTV.setText(mapContragents.get(document.getDestination_id()));
        holder.documentNumberTV.setText(String.valueOf(document.getDocument_number()));

        DataUtil dataUtil = new DataUtil();
        holder.dateTV.setText(dataUtil.formatDate(document.getDate()));


        holder.parentLayout.setOnClickListener(v -> {

            Intent intent = new Intent(context, DocumentInformationActivity.class);
            intent.putExtra("document_number",document.getId());
            context.startActivity(intent);
        });


        holder.deleteButton.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setMessage("Confirm Delete?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", (dialog, which) -> {
                List<DocumentRows> rowToDeleteToUpdateLot=db.getAllDocumentRows(document.getId());
                for(int i=0;i<rowToDeleteToUpdateLot.size();i++){
                    db.deleteDocumentRowAndUpdateLots(rowToDeleteToUpdateLot.get(i));
                }



                db.deleteDocument(document);
                documentsList.remove(document);
                notifyDataSetChanged();
                db.close();
                dialog.dismiss();
            });
            alertDialog.show();
        });

        holder.editButton.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setMessage("Confirm Edit?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", (dialog, which) -> {
                final Dialog editDialog = new Dialog(context);
                editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                editDialog.setContentView(R.layout.custom_document_source_destination_edit_dialog);
                editDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                editDialog.setCancelable(true);

                final Spinner supplierSpinner = editDialog.findViewById(R.id.documentsEditSupplierSpinner);
                final Spinner destinationSpinner = editDialog.findViewById(R.id.documentsEditDestinationSpinner);
                final Button finishEditDocument = editDialog.findViewById(R.id.btnEditDocumentDialog);

                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(context,
                        android.R.layout.simple_spinner_item, contragentsNameList);
                dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                destinationSpinner.setAdapter(dataAdapter1);

                ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(context,
                        android.R.layout.simple_spinner_item, contragentsNameList);
                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                supplierSpinner.setAdapter(dataAdapter2);

                finishEditDocument.setOnClickListener(v1 -> {
                    String supplierID="";
                    String destinationID="";

                    for(int i=0;i<contragentsList.size();i++){
                        if(contragentsList.get(i).getName().equals(supplierSpinner.getSelectedItem().toString())){
                            supplierID=contragentsList.get(i).getId();
                        }
                        if(contragentsList.get(i).getName().equals(destinationSpinner.getSelectedItem().toString())){
                            destinationID=contragentsList.get(i).getId();
                        }
                    }
                    Documents toSend=new Documents(document.getId(), supplierID, destinationID, document.getDocument_number(), document.getDate());
                    db.updateDocument(toSend);
                    documentsList.clear();
                    documentsList.addAll(db.getAllDocuments());
                    notifyDataSetChanged();
                    db.close();
                    editDialog.dismiss();
                });


                dialog.dismiss();
                editDialog.show();
            });
            alertDialog.show();
        });



        //Log.d(TAG, "onBindViewHolder: document Date: " + document.getDate());
    }

    @Override
    public int getItemCount() {
        return documentsList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return String.valueOf(((Documents) resultValue).getDocument_number());
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Documents> documentsFiltred = new ArrayList<>();
                if (constraint != null) {
                    for (Documents document : allDocumentsList) {
                        if (String.valueOf(document.getDocument_number()).contains(constraint.toString())) {
                            documentsFiltred.add(document);
                        }
                    }

                    filterResults.values = documentsFiltred;
                    filterResults.count = documentsFiltred.size();

                    if(filterResults.count == 0){
                        filterResults.values = documentsList;
                        filterResults.count = documentsList.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                documentsList.clear();
                if (results != null && results.count > 0) {
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof Documents) {
                            documentsList.add((Documents) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {

                    documentsList.addAll(allDocumentsList);
                    notifyDataSetChanged();
                }
            }
        };
    }
}

