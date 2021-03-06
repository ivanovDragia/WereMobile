package com.leetsoft.weremobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "database_were_mobile";

    //table PRODUCTS
    private static final String TABLE_PRODUCTS = "products";
    private static final String PRODUCTS_ID = "id";
    private static final String PRODUCTS_NAME = "name";
    private static final String PRODUCTS_QUANTITY = "quantity";
    private static final String PRODUCTS_PRICE = "price";
    private static final String PRODUCTS_PRODUCT_NUMBER = "product_number";

    //table LOTS
    private static final String TABLE_LOTS = "lots";
    private static final String LOTS_ID = "id";
    private static final String LOTS_LOT_NUMBER = "lot_number";
    private static final String LOTS_PRODUCT_ID = "product_id";
    private static final String LOTS_QUANTITY = "quantity";
    private static final String LOTS_EXPIRATION_DATE = "expiration_date";
    private static final String LOTS_ISSYNCED = "is_synced";

    //table CONTRAGENTS
    private static final String TABLE_CONTRAGENTS = "contragents";
    private static final String CONTRAGENTS_ID = "id";
    private static final String CONTRAGENTS_NAME = "name";
    private static final String CONTRAGENTS_BULSTAT = "bulstat";
    private static final String CONTRAGENTS_VAT_NUMBER = "VAT_number";
    private static final String CONTRAGENTS_CITY = "city";
    private static final String CONTRAGENTS_ADDRESS = "address";
    private static final String CONTRAGENTS_MRP = "MRP";
    private static final String CONTRAGENTS_PHONE_NUMBER = "phone_number";
    private static final String CONTRAGENTS_ISSYNCED = "is_synced";

    //table DOCUMENTS
    private static final String TABLE_DOCUMENTS = "documents";
    private static final String DOCUMENTS_ID = "id";
    private static final String DOCUMENTS_SOURCE_ID = "source_id";
    private static final String DOCUMENTS_DESTINATION_ID = "destination_id";
    private static final String DOCUMENTS_DOCUMENT_NUMBER = "document_number";
    private static final String DOCUMENTS_DATE = "date";
    private static final String DOCUMENTS_ISSYNCED = "is_synced";

    //table DOCUMENT_ROWS
    private static final String TABLE_DOCUMENT_ROWS = "document_rows";
    private static final String DOCUMENT_ROWS_ID = "id";
    private static final String DOCUMENT_ROWS_PRODUCT_ID = "product_id";
    private static final String DOCUMENT_ROWS_LOT_ID = "lot_id";
    private static final String DOCUMENT_ROWS_QUANTITY = "quantity";
    private static final String DOCUMENT_ROWS_SUM = "sum";
    private static final String DOCUMENT_ROWS_DOCUMENT_ID = "document_id";
    private static final String DOCUMENT_ROWS_ISSYNCED = "is_synced";

    //table SETTINGS
    private static final String TABLE_SETTINGS = "settings";
    private static final String SETTINGS_ID = "id";
    private static final String SETTINGS_NAME = "name";
    private static final String SETTINGS_VALUE = "value";
    private static final String SETTINGS_DESCRIPTION = "description";

    //table BARCODES
    private static final String TABLE_BARCODES = "barcodes";
    private static final String BARCODES_ID = "id";
    private static final String BARCODES_BARCODE = "barcode";
    private static final String BARCODES_PRODUCT_ID = "product_id";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table PRODUCTS
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
                PRODUCTS_ID + " TEXT PRIMARY KEY," + PRODUCTS_NAME + " NVARCHAR," +
                PRODUCTS_QUANTITY + " DOUBLE," + PRODUCTS_PRICE + " DOUBLE," + PRODUCTS_PRODUCT_NUMBER + " NVARCHAR" + ")";

        //create table LOTS
        String CREATE_LOTS_TABLE = "CREATE TABLE " + TABLE_LOTS + "(" +
                LOTS_ID + " TEXT PRIMARY KEY," + LOTS_PRODUCT_ID + " TEXT," +
                LOTS_QUANTITY + " DOUBLE," + LOTS_EXPIRATION_DATE + " DATETIME," + LOTS_LOT_NUMBER + " NVARCHAR," + LOTS_ISSYNCED + " BIT," + "FOREIGN KEY(" + LOTS_PRODUCT_ID + ")" +
                " REFERENCES " + TABLE_PRODUCTS + "(" + PRODUCTS_ID + ")" + ")";

        //create table CONTRAGENTS
        String CREATE_CONTRAGENTS_TABLE = "CREATE TABLE " + TABLE_CONTRAGENTS + "(" +
                CONTRAGENTS_ID + " TEXT PRIMARY KEY," + CONTRAGENTS_NAME + " NVARCHAR," +
                CONTRAGENTS_BULSTAT + " NVARCHAR," + CONTRAGENTS_VAT_NUMBER + " NVARCHAR," + CONTRAGENTS_CITY + " NVARCHAR," +
                CONTRAGENTS_ADDRESS + " NVARCHAR," + CONTRAGENTS_MRP + " NVARCHAR," + CONTRAGENTS_PHONE_NUMBER + " NVARCHAR," + CONTRAGENTS_ISSYNCED + " BIT" + ")";

        //create table DOCUMENTS
        String CREATE_DOCUMENTS_TABLE = "CREATE TABLE " + TABLE_DOCUMENTS + "(" +
                DOCUMENTS_ID + " TEXT PRIMARY KEY," + DOCUMENTS_SOURCE_ID + " TEXT," +
                DOCUMENTS_DESTINATION_ID + " TEXT," + DOCUMENTS_DOCUMENT_NUMBER + " INTEGER," + DOCUMENTS_DATE + " DATETIME," + DOCUMENTS_ISSYNCED + " BIT," + "FOREIGN KEY(" + DOCUMENTS_SOURCE_ID + ")" +
                " REFERENCES " + TABLE_CONTRAGENTS + "(" + CONTRAGENTS_ID + ")," + "FOREIGN KEY(" + DOCUMENTS_DESTINATION_ID + ")" +
                " REFERENCES " + TABLE_CONTRAGENTS + "(" + CONTRAGENTS_ID + ")" + ")";

        //create table DOCUMENT_ROWS
        String CREATE_DOCUMENT_ROWS_TABLE = "CREATE TABLE " + TABLE_DOCUMENT_ROWS + "(" +
                DOCUMENT_ROWS_ID + " TEXT PRIMARY KEY," + DOCUMENT_ROWS_PRODUCT_ID + " TEXT," +
                DOCUMENT_ROWS_LOT_ID + " TEXT," + DOCUMENT_ROWS_QUANTITY + " DOUBLE," + DOCUMENT_ROWS_SUM + " DOUBLE," +
                DOCUMENT_ROWS_DOCUMENT_ID + " TEXT," + DOCUMENT_ROWS_ISSYNCED + " BIT," + "FOREIGN KEY(" + DOCUMENT_ROWS_PRODUCT_ID + ")" +
                " REFERENCES " + TABLE_PRODUCTS + "(" + PRODUCTS_ID + ")," + "FOREIGN KEY(" + DOCUMENT_ROWS_LOT_ID + ")" +
                " REFERENCES " + TABLE_LOTS + "(" + LOTS_ID + ")," + "FOREIGN KEY(" + DOCUMENT_ROWS_DOCUMENT_ID + ")" +
                " REFERENCES " + TABLE_DOCUMENTS + "(" + DOCUMENTS_ID + ")" + ")";

        //create table SETTINGS
        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "(" +
                SETTINGS_ID + " TEXT PRIMARY KEY," + SETTINGS_NAME + " NVARCHAR," +
                SETTINGS_VALUE + " NVARCHAR," + SETTINGS_DESCRIPTION + " NVARCHAR" + ")";

        //create table BARCODES
        String CREATE_BARCODES_TABLE = "CREATE TABLE " + TABLE_BARCODES + "(" +
                BARCODES_ID + " TEXT PRIMARY KEY," + BARCODES_BARCODE + " NVARCHAR," +
                BARCODES_PRODUCT_ID + " TEXT," + "FOREIGN KEY(" + BARCODES_PRODUCT_ID + ")" +
                " REFERENCES " + TABLE_PRODUCTS + "(" + PRODUCTS_ID + ")" + ")";


        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_LOTS_TABLE);
        db.execSQL(CREATE_CONTRAGENTS_TABLE);
        db.execSQL(CREATE_DOCUMENTS_TABLE);
        db.execSQL(CREATE_DOCUMENT_ROWS_TABLE);
        db.execSQL(CREATE_SETTINGS_TABLE);
        db.execSQL(CREATE_BARCODES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTRAGENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENT_ROWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BARCODES);

        onCreate(db);
    }

    //table PRODUCTS
    public void addProduct(Products product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PRODUCTS_ID, product.getId());
        values.put(PRODUCTS_NAME, product.getName());
        values.put(PRODUCTS_QUANTITY, product.getQuantity());
        values.put(PRODUCTS_PRICE, product.getPrice());
        values.put(PRODUCTS_PRODUCT_NUMBER, product.getProduct_number());

        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public ArrayList<Products> getAllProducts() {
        ArrayList<Products> productsList = new ArrayList<>();

        String selectQuery = "SELECT *FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Products product = new Products();
                product.setId(cursor.getString(cursor.getColumnIndex(PRODUCTS_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(PRODUCTS_NAME)));
                product.setQuantity(cursor.getDouble(cursor.getColumnIndex(PRODUCTS_QUANTITY)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex(PRODUCTS_PRICE)));
                product.setProduct_number(cursor.getString(cursor.getColumnIndex(PRODUCTS_PRODUCT_NUMBER)));

                productsList.add(product);
            } while (cursor.moveToNext());
        }

        return productsList;
    }

    public int updateProduct(Products product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PRODUCTS_NAME, product.getName());
        values.put(PRODUCTS_QUANTITY, product.getQuantity());
        values.put(PRODUCTS_PRICE, product.getPrice());
        values.put(PRODUCTS_PRODUCT_NUMBER, product.getProduct_number());

        return db.update(TABLE_PRODUCTS, values, PRODUCTS_ID + "=?",
                new String[]{String.valueOf(product.getId())});
    }

    public void deleteProduct(Products product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, PRODUCTS_ID + "=?",
                new String[]{String.valueOf(product.getId())});
        db.close();
    }

    public void deleteAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_PRODUCTS);
        db.close();
    }

    //table LOTS
    public void addLot(Lots lot) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LOTS_ID, lot.getId());
        values.put(LOTS_PRODUCT_ID, lot.getProduct_id());
        values.put(LOTS_QUANTITY, lot.getQuantity());
        values.put(LOTS_EXPIRATION_DATE, lot.getExpiration_date().toString());
        values.put(LOTS_LOT_NUMBER, lot.getLot_number());
        values.put(LOTS_ISSYNCED, lot.isIs_synced());


        db.insert(TABLE_LOTS, null, values);
        db.close();
    }

    public ArrayList<Lots> getAllLots() {
        ArrayList<Lots> lotsList = new ArrayList<>();

        String selectQuery = "SELECT *FROM " + TABLE_LOTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Lots lot = new Lots();
                lot.setId(cursor.getString(cursor.getColumnIndex(LOTS_ID)));
                lot.setProduct_id(cursor.getString(cursor.getColumnIndex(LOTS_PRODUCT_ID)));
                lot.setQuantity(cursor.getDouble(cursor.getColumnIndex(LOTS_QUANTITY)));
                //get date from table
                String s = cursor.getString(cursor.getColumnIndex(LOTS_EXPIRATION_DATE));
                SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                        Locale.getDefault());
                Date date = new Date();
                try {
                    date = dateFormat.parse(s);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                lot.setExpiration_date(date);
                //end
                lot.setLot_number(cursor.getString(cursor.getColumnIndex(LOTS_LOT_NUMBER)));

                lotsList.add(lot);
            } while (cursor.moveToNext());
        }

        return lotsList;
    }

    public int updateLot(Lots lot) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LOTS_PRODUCT_ID, lot.getProduct_id());
        values.put(LOTS_QUANTITY, lot.getQuantity());
        values.put(LOTS_EXPIRATION_DATE, lot.getExpiration_date().toString());
        values.put(LOTS_LOT_NUMBER, lot.getLot_number());
        values.put(LOTS_ISSYNCED, 0);

        return db.update(TABLE_LOTS, values, LOTS_ID + "=?",
                new String[]{String.valueOf(lot.getId())});
    }

    public void deleteLot(Lots lot) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOTS, LOTS_ID + "=?",
                new String[]{String.valueOf(lot.getId())});
        db.close();
    }

    public void deleteAllLots() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_LOTS);
        db.close();
    }

    //table CONTRAGENTS
    public void addContragent(Contragents contragent) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTRAGENTS_ID, contragent.getId());
        values.put(CONTRAGENTS_NAME, contragent.getName());
        values.put(CONTRAGENTS_BULSTAT, contragent.getBulstat());
        values.put(CONTRAGENTS_VAT_NUMBER, contragent.getVAT_number());
        values.put(CONTRAGENTS_CITY, contragent.getCity());
        values.put(CONTRAGENTS_ADDRESS, contragent.getAddress());
        values.put(CONTRAGENTS_MRP, contragent.getMRP());
        values.put(CONTRAGENTS_PHONE_NUMBER, contragent.getPhone_number());
        if (contragent.is_synced) {
            values.put(CONTRAGENTS_ISSYNCED, 1);
        } else {
            values.put(CONTRAGENTS_ISSYNCED, 0);
        }


        db.insert(TABLE_CONTRAGENTS, null, values);
        db.close();
    }

    public void addContragentWithID(Contragents contragent) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTRAGENTS_ID, contragent.getId());
        values.put(CONTRAGENTS_NAME, contragent.getName());
        values.put(CONTRAGENTS_BULSTAT, contragent.getBulstat());
        values.put(CONTRAGENTS_VAT_NUMBER, contragent.getVAT_number());
        values.put(CONTRAGENTS_CITY, contragent.getCity());
        values.put(CONTRAGENTS_ADDRESS, contragent.getAddress());
        values.put(CONTRAGENTS_MRP, contragent.getMRP());
        values.put(CONTRAGENTS_PHONE_NUMBER, contragent.getPhone_number());
        values.put(CONTRAGENTS_ISSYNCED, contragent.isIs_synced());


        db.insert(TABLE_CONTRAGENTS, null, values);
        db.close();
    }

    public ArrayList<Contragents> getAllContragents() {
        ArrayList<Contragents> contragentsList = new ArrayList<>();

        String selectQuery = "SELECT *FROM " + TABLE_CONTRAGENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Contragents contragent = new Contragents();
                contragent.setId(cursor.getString(0));
                contragent.setName(cursor.getString(1));
                contragent.setBulstat(cursor.getString(2));
                contragent.setVAT_number(cursor.getString(3));
                contragent.setCity(cursor.getString(4));
                contragent.setAddress(cursor.getString(5));
                contragent.setMRP(cursor.getString(6));
                contragent.setPhone_number(cursor.getString(7));
                if (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CONTRAGENTS_ISSYNCED))) == true) {
                    contragent.setIs_synced(true);
                } else if (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CONTRAGENTS_ISSYNCED))) == false) {
                    contragent.setIs_synced(false);
                }


                contragentsList.add(contragent);
            } while (cursor.moveToNext());
        }

        return contragentsList;
    }


    public int updateContragent(Contragents contragent) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTRAGENTS_NAME, contragent.getName());
        values.put(CONTRAGENTS_BULSTAT, contragent.getBulstat());
        values.put(CONTRAGENTS_VAT_NUMBER, contragent.getVAT_number());
        values.put(CONTRAGENTS_CITY, contragent.getCity());
        values.put(CONTRAGENTS_ADDRESS, contragent.getAddress());
        values.put(CONTRAGENTS_MRP, contragent.getMRP());
        values.put(CONTRAGENTS_PHONE_NUMBER, contragent.getPhone_number());
        values.put(CONTRAGENTS_ISSYNCED, 0);


        return db.update(TABLE_CONTRAGENTS, values, CONTRAGENTS_ID + "=?",
                new String[]{String.valueOf(contragent.getId())});
    }

    public void deleteContragent(Contragents contragent) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTRAGENTS, CONTRAGENTS_ID + "=?",
                new String[]{String.valueOf(contragent.getId())});
        db.close();
    }

    public void deleteAllContragents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CONTRAGENTS);
        db.close();
    }

    //table DOCUMENTS
    public String addDocument(Documents document) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT *FROM " + TABLE_DOCUMENTS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        ContentValues values = new ContentValues();
        values.put(DOCUMENTS_ID,document.getId());
        values.put(DOCUMENTS_SOURCE_ID, document.getSource_id());
        values.put(DOCUMENTS_DESTINATION_ID, document.getDestination_id());
        values.put(DOCUMENTS_DOCUMENT_NUMBER, document.getDocument_number());
        values.put(DOCUMENTS_DATE, document.getDate().toString());
        values.put(DOCUMENTS_ISSYNCED, 0);

        String id = String.valueOf(db.insert(TABLE_DOCUMENTS, null, values));
        db.close();
        if (id.equals("-1")) {
            return "-1";
        } else {
            return document.getId();
        }
    }

    public String addDocumentWithId(Documents document) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT *FROM " + TABLE_DOCUMENTS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        ContentValues values = new ContentValues();
        values.put(DOCUMENTS_ID, document.getId());
        values.put(DOCUMENTS_SOURCE_ID, document.getSource_id());
        values.put(DOCUMENTS_DESTINATION_ID, document.getDestination_id());
        values.put(DOCUMENTS_DOCUMENT_NUMBER, document.getDocument_number());
        values.put(DOCUMENTS_DATE, document.getDate().toString());
        if (document.is_synced) {
            values.put(CONTRAGENTS_ISSYNCED, 1);
        } else {
            values.put(CONTRAGENTS_ISSYNCED, 0);
        }

        String id = String.valueOf(db.insert(TABLE_DOCUMENTS, null, values));
        db.close();
        if (id.equals("-1")) {
            return "-1";
        } else {
            return document.getId();
        }
    }

    public ArrayList<Documents> getAllDocuments() {
        ArrayList<Documents> documentsList = new ArrayList<>();

        String selectQuery = "SELECT *FROM " + TABLE_DOCUMENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Documents document = new Documents();
                document.setId(cursor.getString(cursor.getColumnIndex(DOCUMENTS_ID)));
                document.setSource_id(cursor.getString(cursor.getColumnIndex(DOCUMENTS_SOURCE_ID)));
                document.setDestination_id(cursor.getString(cursor.getColumnIndex(DOCUMENTS_DESTINATION_ID)));
                document.setDocument_number(cursor.getInt(cursor.getColumnIndex(DOCUMENTS_DOCUMENT_NUMBER)));
                String s = cursor.getString(cursor.getColumnIndex(DOCUMENTS_DATE));


                SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                        Locale.getDefault());
                Date date = new Date();
                try {
                    date = dateFormat.parse(s);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                document.setDate(date);
                if (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DOCUMENTS_ISSYNCED))) == true) {
                    document.setIs_synced(true);
                } else if (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DOCUMENTS_ISSYNCED))) == false) {
                    document.setIs_synced(false);
                }


                documentsList.add(document);
            } while (cursor.moveToNext());
        }
        return documentsList;
    }

    //new method added 06/01/21

    public int getDocumentCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DOCUMENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count + 1;
    }

    public int updateDocument(Documents document) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DOCUMENTS_SOURCE_ID, document.getSource_id());
        values.put(DOCUMENTS_DESTINATION_ID, document.getDestination_id());
        values.put(DOCUMENTS_DOCUMENT_NUMBER, document.getDocument_number());
        values.put(DOCUMENTS_DATE, document.getDate().toString());
        values.put(CONTRAGENTS_ISSYNCED, 0);


        return db.update(TABLE_DOCUMENTS, values, DOCUMENTS_ID + "=?",
                new String[]{String.valueOf(document.getId())});
    }

    public int updateDocumentSynced(Documents document) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DOCUMENTS_SOURCE_ID, document.getSource_id());
        values.put(DOCUMENTS_DESTINATION_ID, document.getDestination_id());
        values.put(DOCUMENTS_DOCUMENT_NUMBER, document.getDocument_number());
        values.put(DOCUMENTS_DATE, document.getDate().toString());
        values.put(CONTRAGENTS_ISSYNCED, 1);


        return db.update(TABLE_DOCUMENTS, values, DOCUMENTS_ID + "=?",
                new String[]{String.valueOf(document.getId())});
    }


    public void deleteDocument(Documents document) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DOCUMENTS, DOCUMENTS_ID + "=?",
                new String[]{String.valueOf(document.getId())});
        db.close();
    }

    public void deleteAllDocuments() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_DOCUMENTS);
        db.close();
    }

    public void deleteRowsAndDocument(Documents document) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DOCUMENT_ROWS, DOCUMENT_ROWS_DOCUMENT_ID + "=?",
                new String[]{String.valueOf(document.getDocument_number())});

        db.delete(TABLE_DOCUMENTS, DOCUMENTS_ID + "=?",
                new String[]{String.valueOf(document.getId())});
        db.close();
    }

    //table DOCUMENT_ROWS
    public void addDocumentRow(DocumentRows documentRow) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DOCUMENT_ROWS_ID,documentRow.getId());
        values.put(DOCUMENT_ROWS_PRODUCT_ID, documentRow.getProduct_id());
        values.put(DOCUMENT_ROWS_LOT_ID, documentRow.getLot_id());
        values.put(DOCUMENT_ROWS_QUANTITY, documentRow.getQuantity());
        values.put(DOCUMENT_ROWS_SUM, documentRow.getSum());
        values.put(DOCUMENT_ROWS_DOCUMENT_ID, documentRow.getDocument_id());
        values.put(DOCUMENT_ROWS_ISSYNCED, 0);

        db.insert(TABLE_DOCUMENT_ROWS, null, values);
        db.close();
    }

    public void addDocumentRowAndUpdateLot(DocumentRows documentRow) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DOCUMENT_ROWS_ID,documentRow.getId());
        values.put(DOCUMENT_ROWS_PRODUCT_ID, documentRow.getProduct_id());
        values.put(DOCUMENT_ROWS_LOT_ID, documentRow.getLot_id());
        values.put(DOCUMENT_ROWS_QUANTITY, documentRow.getQuantity());
        values.put(DOCUMENT_ROWS_SUM, documentRow.getSum());
        values.put(DOCUMENT_ROWS_DOCUMENT_ID, documentRow.getDocument_id());
        values.put(DOCUMENT_ROWS_ISSYNCED, 0);


        db.insert(TABLE_DOCUMENT_ROWS, null, values);

        int lotOriginalQuantity = -5;

        String selectQuery = "select quantity from lots where id = " + "\""+ documentRow.getLot_id() + "\"";
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            lotOriginalQuantity = cursor.getInt(cursor.getColumnIndex("quantity"));
        }

        lotOriginalQuantity -= documentRow.getQuantity();

        String updateQuery = "update lots set quantity = " + lotOriginalQuantity + " where id =" + "\""+ documentRow.getLot_id() + "\"";
        db.execSQL(updateQuery);
        String updateQuery2 = "update lots set is_synced = " + 0 + " where id =" + "\""+ documentRow.getLot_id() + "\"";
        db.execSQL(updateQuery2);


        db.close();
    }

    public ArrayList<DocumentRows> getAllDocumentRows(String id) {
        ArrayList<DocumentRows> documentRowsList = new ArrayList<>();

        String selectQuery = "SELECT *FROM " + TABLE_DOCUMENT_ROWS + " WHERE " + DOCUMENT_ROWS_DOCUMENT_ID + " = " + "\""+ id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DocumentRows documentRow = new DocumentRows();
                documentRow.setId(cursor.getString(cursor.getColumnIndex(DOCUMENT_ROWS_ID)));
                documentRow.setProduct_id(cursor.getString(cursor.getColumnIndex(DOCUMENT_ROWS_PRODUCT_ID)));
                documentRow.setLot_id(cursor.getString(cursor.getColumnIndex(DOCUMENT_ROWS_LOT_ID)));
                documentRow.setQuantity(cursor.getDouble(cursor.getColumnIndex(DOCUMENT_ROWS_QUANTITY)));
                documentRow.setSum(cursor.getDouble(cursor.getColumnIndex(DOCUMENT_ROWS_SUM)));
                documentRow.setDocument_id(cursor.getString(cursor.getColumnIndex(DOCUMENT_ROWS_DOCUMENT_ID)));
                if (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DOCUMENT_ROWS_ISSYNCED))) == true) {
                    documentRow.setIs_synced(true);
                } else if (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DOCUMENT_ROWS_ISSYNCED))) == false) {
                    documentRow.setIs_synced(false);
                }



                documentRowsList.add(documentRow);
            } while (cursor.moveToNext());
        }
        return documentRowsList;
    }

    public ArrayList<DocumentRows> getAllDocumentRows() {
        ArrayList<DocumentRows> documentRowsList = new ArrayList<>();

        String selectQuery = "SELECT *FROM " + TABLE_DOCUMENT_ROWS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DocumentRows documentRow = new DocumentRows();
                documentRow.setId(cursor.getString(cursor.getColumnIndex(DOCUMENT_ROWS_ID)));
                documentRow.setProduct_id(cursor.getString(cursor.getColumnIndex(DOCUMENT_ROWS_PRODUCT_ID)));
                documentRow.setLot_id(cursor.getString(cursor.getColumnIndex(DOCUMENT_ROWS_LOT_ID)));
                documentRow.setQuantity(cursor.getDouble(cursor.getColumnIndex(DOCUMENT_ROWS_QUANTITY)));
                documentRow.setSum(cursor.getDouble(cursor.getColumnIndex(DOCUMENT_ROWS_SUM)));
                documentRow.setDocument_id(cursor.getString(cursor.getColumnIndex(DOCUMENT_ROWS_DOCUMENT_ID)));
                if (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DOCUMENT_ROWS_ISSYNCED))) == true) {
                    documentRow.setIs_synced(true);
                } else if (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DOCUMENT_ROWS_ISSYNCED))) == false) {
                    documentRow.setIs_synced(false);
                }



                documentRowsList.add(documentRow);
            } while (cursor.moveToNext());
        }
        return documentRowsList;
    }

    public int updateDocumentRow(DocumentRows documentRow) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DOCUMENT_ROWS_PRODUCT_ID, documentRow.getProduct_id());
        values.put(DOCUMENT_ROWS_LOT_ID, documentRow.getLot_id());
        values.put(DOCUMENT_ROWS_QUANTITY, documentRow.getQuantity());
        values.put(DOCUMENT_ROWS_SUM, documentRow.getSum());
        values.put(DOCUMENT_ROWS_DOCUMENT_ID, documentRow.getDocument_id());
        values.put(DOCUMENT_ROWS_ISSYNCED, 0);

        return db.update(TABLE_DOCUMENT_ROWS, values, DOCUMENT_ROWS_ID + "=?",
                new String[]{String.valueOf(documentRow.getId())});
    }

    public int updateDocumentRowSynced(DocumentRows documentRow) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DOCUMENT_ROWS_PRODUCT_ID, documentRow.getProduct_id());
        values.put(DOCUMENT_ROWS_LOT_ID, documentRow.getLot_id());
        values.put(DOCUMENT_ROWS_QUANTITY, documentRow.getQuantity());
        values.put(DOCUMENT_ROWS_SUM, documentRow.getSum());
        values.put(DOCUMENT_ROWS_DOCUMENT_ID, documentRow.getDocument_id());
        values.put(DOCUMENT_ROWS_ISSYNCED, 1);

        return db.update(TABLE_DOCUMENT_ROWS, values, DOCUMENT_ROWS_ID + "=?",
                new String[]{String.valueOf(documentRow.getId())});
    }
    public void deleteDocumentRow(DocumentRows documentRow) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DOCUMENT_ROWS, DOCUMENT_ROWS_ID + "=?",
                new String[]{String.valueOf(documentRow.getId())});
        db.close();
    }

    public void deleteDocumentRowAndUpdateLots(DocumentRows documentRow) {
        SQLiteDatabase db = this.getWritableDatabase();

        double lotOriginalQuantity = -5;

        String selectQuery = "select quantity from lots where id = " + "\""+ documentRow.getLot_id() + "\"";


        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            lotOriginalQuantity = cursor.getDouble(cursor.getColumnIndex("quantity"));
        }

        lotOriginalQuantity += documentRow.getQuantity();

        String updateQuery = "update lots set quantity = " + lotOriginalQuantity + " where id =" + "\""+ documentRow.getLot_id() + "\"";
        db.execSQL(updateQuery);

        String updateQuery2 = "update lots set is_synced = " + 0 + " where id =" + "\""+ documentRow.getLot_id() + "\"";
        db.execSQL(updateQuery2);


        db.delete(TABLE_DOCUMENT_ROWS, DOCUMENT_ROWS_ID + "=?",
                new String[]{String.valueOf(documentRow.getId())});
        db.close();
    }

    public void deleteAllDocumentRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_DOCUMENT_ROWS);
        db.close();
    }


    //table SETTINGS
    public void addSetting(Settings setting) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SETTINGS_ID,setting.getId());
        values.put(SETTINGS_NAME, setting.getName());
        values.put(SETTINGS_VALUE, setting.getValue());
        values.put(SETTINGS_DESCRIPTION, setting.getDescription());

        db.insert(TABLE_SETTINGS, null, values);
        db.close();
    }

    public ArrayList<Settings> getAllSettings() {
        ArrayList<Settings> settingsList = new ArrayList<>();

        String selectQuery = "SELECT *FROM " + TABLE_SETTINGS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Settings setting = new Settings();
                setting.setId(cursor.getString(0));
                setting.setName(cursor.getString(1));
                setting.setValue(cursor.getString(2));
                setting.setDescription(cursor.getString(3));


                settingsList.add(setting);
            } while (cursor.moveToNext());
        }

        return settingsList;
    }

    public int updateSetting(Settings setting) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SETTINGS_NAME, setting.getName());
        values.put(SETTINGS_VALUE, setting.getValue());
        values.put(SETTINGS_DESCRIPTION, setting.getDescription());

        return db.update(TABLE_SETTINGS, values, SETTINGS_ID + "=?",
                new String[]{String.valueOf(setting.getId())});
    }

    public void deleteSetting(Settings setting) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SETTINGS, SETTINGS_ID + "=?",
                new String[]{String.valueOf(setting.getId())});
        db.close();
    }

    //table BARCODES
    public void addBarcodes(Barcodes barcodes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BARCODES_ID,barcodes.getId());
        values.put(BARCODES_BARCODE, barcodes.getBarcode());
        values.put(BARCODES_PRODUCT_ID, barcodes.getProduct_id());

        db.insert(TABLE_BARCODES, null, values);
        db.close();
    }

    public ArrayList<Barcodes> getAllBarcodes() {
        ArrayList<Barcodes> barcodesList = new ArrayList<>();

        String selectQuery = "SELECT *FROM " + TABLE_BARCODES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Barcodes barcode = new Barcodes();
                barcode.setId(cursor.getString(cursor.getColumnIndex(BARCODES_ID)));
                barcode.setBarcode(cursor.getString(cursor.getColumnIndex(BARCODES_BARCODE)));
                barcode.setProduct_id(cursor.getString(cursor.getColumnIndex(BARCODES_PRODUCT_ID)));

                barcodesList.add(barcode);
            } while (cursor.moveToNext());
        }

        return barcodesList;
    }

    public int updateBarcode(Barcodes barcode) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BARCODES_BARCODE, barcode.getBarcode());
        values.put(BARCODES_PRODUCT_ID, barcode.getProduct_id());

        return db.update(TABLE_BARCODES, values, BARCODES_ID + "=?",
                new String[]{String.valueOf(barcode.getId())});
    }

    public void deleteBarcode(Barcodes barcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BARCODES, BARCODES_ID + "=?",
                new String[]{String.valueOf(barcode.getId())});
        db.close();
    }

    public void deleteAllBarcodes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_BARCODES);
        db.close();
    }


}
