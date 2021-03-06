package com.leetsoft.weremobile.adapters;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.leetsoft.weremobile.R;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.DocumentRows;
import com.leetsoft.weremobile.database.Lots;
import com.leetsoft.weremobile.database.Products;
import com.leetsoft.weremobile.interfaces.OnDocumentInformationEditClickListener;
import com.leetsoft.weremobile.util.DataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DocumentRowsAdapterNoBTN extends RecyclerView.Adapter<DocumentRowsAdapterNoBTN.ViewHolder>{
    private static final String TAG = "DocumentRowsAdapterNoBT";
    Context context;
    List<DocumentRows> documentRowsList;
    List<Products> productsList;
    ArrayList<Lots> lotsList;
    List<String> lotsNumberList;
    OnDocumentInformationEditClickListener mInterface;
    HashMap<String, String> productMap = new HashMap<String, String>();


    DatabaseHelper db;
    DataUtil dataUtil = new DataUtil();


    String docNumber = "";

    public DocumentRowsAdapterNoBTN(Context context, List<DocumentRows> documentRowsList, String docNumber, OnDocumentInformationEditClickListener mInterface) {
        this.context = context;
        this.documentRowsList = documentRowsList;
        this.docNumber=docNumber;
        db = new DatabaseHelper(context);
        productsList = db.getAllProducts();
        lotsList=db.getAllLots();
        lotsNumberList=new ArrayList<>();
        this.mInterface=mInterface;

        for(int i=0;i<productsList.size();i++){
            productMap.put(productsList.get(i).getId(), productsList.get(i).getName());
        }
    }

    @NonNull
    @Override
    public DocumentRowsAdapterNoBTN.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: started");

        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nobtn_document_row_recycler_layout, parent, false);
        DocumentRowsAdapterNoBTN.ViewHolder holder = new DocumentRowsAdapterNoBTN.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentRowsAdapterNoBTN.ViewHolder holder, int position) {
        DocumentRows row = documentRowsList.get(position);
        String lot_number="";
        for (Lots lot: lotsList) {
            if(lot.getId().equals(row.getLot_id())){
                lot_number = lot.getLot_number();
            }
        }

        //get product name by product id
        String productID = row.getProduct_id();

        DataUtil dataUtil = new DataUtil();
        String productName = dataUtil.getProductName(productID,productsList);


        String row_id = row.getId();
        holder.documentRowProductIDTextView.setText(productName);
        holder.documentRowLotIDTextView.setText(lot_number);
        holder.documentRowQuantityTextView.setText(String.valueOf(row.getQuantity()));
        holder.documentRowSumTextView.setText(String.valueOf(dataUtil.round(row.getSum(),2)));

        holder.documentRowEditBtn.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setMessage("Confirm Edit?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", (dialog, which) -> {
                final Dialog editDialog = new Dialog(context);
                editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                editDialog.setContentView(R.layout.custom_document_information_document_row_edit_dialogue);
                editDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                editDialog.setCancelable(true);

                final Spinner lotSpinner = editDialog.findViewById(R.id.documentsInformationRowSpinner);
                final EditText quantityEditText=editDialog.findViewById(R.id.editTextDocumentsInforatmionSpinner);
                final MaterialButton editButton=editDialog.findViewById(R.id.btnEditDocumentRowInformationDialog);

                lotsNumberList.clear();
                if(lotsNumberList.isEmpty()){
                    for(int i=0;i<lotsList.size();i++){
                        String spinnerString=new String(lotsList.get(i).getLot_number()+" "+productMap.get(lotsList.get(i).getProduct_id())+
                                " Quantity: "+lotsList.get(i).getQuantity());

                        lotsNumberList.add(spinnerString);
                    }
                }



                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(context,
                        android.R.layout.simple_spinner_item, lotsNumberList);
                dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                lotSpinner.setAdapter(dataAdapter1);


                editButton.setOnClickListener(v1 -> {


                    String newLotNumber = "";
                    double oldLotQuantity=0;
                    double newQuantity;
                    int difference=0;


                    if(quantityEditText.getText().toString().length()<=9 && quantityEditText.getText().toString().trim().length()>0 && !quantityEditText.getText().toString().trim().equals("0")){
                        String[] arrOfStr = lotSpinner.getSelectedItem().toString().split(" ");
                        String LotIdFromLotNumber="";
                        for (Lots lot: lotsList) {
                            if(lot.getLot_number().equals(arrOfStr[0])){
                                LotIdFromLotNumber = lot.getId();
                            }
                        }


                        newLotNumber=arrOfStr[0];
                        newQuantity=Double.parseDouble(quantityEditText.getText().toString().trim());

                        if(LotIdFromLotNumber.equals(row.getLot_id())){


                            String newProductID="-1";



                            for(int i=0;i<lotsList.size();i++){
                                if(newLotNumber.equals(lotsList.get(i).getLot_number())){
                                    newProductID=lotsList.get(i).getProduct_id();
                                    oldLotQuantity=lotsList.get(i).getQuantity();
                                }
                            }

                            if(newQuantity<=oldLotQuantity+row.getQuantity()){
                                db.deleteDocumentRowAndUpdateLots(row);
                                double newPrice=-1;

                                for(int i=0;i<productsList.size();i++){
                                    if(newProductID.equals(productsList.get(i).getId())){
                                        newPrice=productsList.get(i).getPrice();
                                    }
                                }


                                double newSum=newQuantity*newPrice;

                                DocumentRows newRow=new DocumentRows(row_id,newProductID, LotIdFromLotNumber, newQuantity,newSum ,row.getDocument_id());

                                db.addDocumentRowAndUpdateLot(newRow);



                                productsList.clear();
                                productsList = db.getAllProducts();
                                lotsList.clear();
                                lotsList=db.getAllLots();
                                documentRowsList.clear();
                                documentRowsList=db.getAllDocumentRows(docNumber);


                                notifyDataSetChanged();
                                editDialog.dismiss();

                                db.close();

                                mInterface.onEditClicked();
                            }else{
                                Toast.makeText(context, "Not enough quantity in lot", Toast.LENGTH_SHORT).show();
                            }
                        }else{

                            String newProductID="-1";



                            for(int i=0;i<lotsList.size();i++){
                                if(newLotNumber.equals(lotsList.get(i).getLot_number())){
                                    newProductID=lotsList.get(i).getProduct_id();
                                    oldLotQuantity=lotsList.get(i).getQuantity();
                                }
                            }

                            if(newQuantity<=oldLotQuantity){
                                db.deleteDocumentRowAndUpdateLots(row);
                                double newPrice=-1;

                                for(int i=0;i<productsList.size();i++){
                                    if(newProductID.equals(productsList.get(i).getId())){
                                        newPrice=productsList.get(i).getPrice();
                                    }
                                }

                                double newSum=newQuantity*newPrice;

                                DocumentRows newRow=new DocumentRows(row_id, newProductID, LotIdFromLotNumber, newQuantity,newSum ,row.getDocument_id());

                                db.addDocumentRowAndUpdateLot(newRow);



                                productsList.clear();
                                productsList = db.getAllProducts();
                                lotsList.clear();
                                lotsList=db.getAllLots();
                                documentRowsList.clear();
                                documentRowsList=db.getAllDocumentRows(docNumber);


                                notifyDataSetChanged();
                                editDialog.dismiss();

                                db.close();

                                mInterface.onEditClicked();
                            }else{
                                Toast.makeText(context, "Not enough quantity in lot", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }else{
                        Toast.makeText(context, "Quantity field cant be greater then 9 digits,empty or 0!", Toast.LENGTH_SHORT).show();
                    }


                });


                alertDialog.dismiss();
                editDialog.show();
            });
            alertDialog.show();
        });

        holder.documentRowDeleteBtn.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setMessage("Confirm Delete");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", (dialog, which) -> {
                db.deleteDocumentRowAndUpdateLots(row);


                productsList.clear();
                productsList = db.getAllProducts();
                lotsList.clear();
                lotsList=db.getAllLots();
                documentRowsList.clear();
                documentRowsList=db.getAllDocumentRows(docNumber);

                notifyDataSetChanged();
                dialog.dismiss();
                db.close();
            });
            alertDialog.show();
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView documentRowProductIDTextView;
        TextView documentRowLotIDTextView;
        TextView documentRowQuantityTextView;
        TextView documentRowSumTextView;
        ImageButton documentRowEditBtn;
        ImageButton documentRowDeleteBtn;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            documentRowProductIDTextView = itemView.findViewById(R.id.documentRowProductIDTextView);
            documentRowLotIDTextView = itemView.findViewById(R.id.documentRowLotIDTextView);
            documentRowQuantityTextView = itemView.findViewById(R.id.documentRowQuantityTextView);
            documentRowSumTextView = itemView.findViewById(R.id.documentRowSumTextView);
            documentRowDeleteBtn = itemView.findViewById(R.id.buttonDeleteRowDocumentInformation);
            documentRowEditBtn = itemView.findViewById(R.id.buttonEditRowDocumentInformation);


        }

    }


    @Override
    public int getItemCount() {
        return documentRowsList.size();
    }

    public String getDocNumber(){
        return docNumber;
    }

}
