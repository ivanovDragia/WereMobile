package com.leetsoft.weremobile.interfaces;

import com.leetsoft.weremobile.database.DocumentRows;


public interface OnCreateDocumentRowButtonClickListener {
    public void onDeleteClicked(int id, DocumentRows row);
    public void updateQuantity(DocumentRows row);
    public void onEdidClicked(DocumentRows row, int position);
}
