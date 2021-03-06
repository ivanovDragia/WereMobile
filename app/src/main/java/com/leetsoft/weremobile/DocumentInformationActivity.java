package com.leetsoft.weremobile;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leetsoft.weremobile.adapters.DocumentRowsAdapterNoBTN;
import com.leetsoft.weremobile.database.Contragents;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.DocumentRows;
import com.leetsoft.weremobile.database.Documents;
import com.leetsoft.weremobile.interfaces.OnDocumentInformationEditClickListener;
import com.leetsoft.weremobile.util.DataUtil;

import java.util.List;

public class DocumentInformationActivity extends AppCompatActivity {
    private static final String TAG = "DocumentInformationActi";

    TextView documentSourceName;
    TextView documentSourceBulstat;
    TextView documentSourceVAT;
    TextView documentSourceCity;
    TextView documentSourceAddress;
    TextView documentSourceMRP;
    TextView documentSourcePhone;

    TextView documentDestinationName;
    TextView documentDestinationBulstat;
    TextView documentDestinationVAT;
    TextView documentDestinationCity;
    TextView documentDestinationAddress;
    TextView documentDestinationMRP;
    TextView documentDestinationPhone;

    TextView documentNumberLable;
    TextView documentDate;

    DatabaseHelper db = new DatabaseHelper(this);

    DocumentRowsAdapterNoBTN adapter;
    List<DocumentRows> documentRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_information);
        Log.d(TAG, "onCreate: started.");

        documentSourceName = findViewById(R.id.documentSourceName);
        documentSourceBulstat = findViewById(R.id.documentSourceBulstat);
        documentSourceVAT = findViewById(R.id.documentSourceVAT);
        documentSourceCity = findViewById(R.id.documentSourceCity);
        documentSourceAddress = findViewById(R.id.documentSourceAddress);
        documentSourceMRP = findViewById(R.id.documentSourceMRP);
        documentSourcePhone = findViewById(R.id.documentSourcePhone);

        documentDestinationName = findViewById(R.id.documentDestinationName);
        documentDestinationBulstat = findViewById(R.id.documentDestinationBulstat);
        documentDestinationVAT = findViewById(R.id.documentDestinationVAT);
        documentDestinationCity = findViewById(R.id.documentDestinationCity);
        documentDestinationAddress = findViewById(R.id.documentDestinationAddress);
        documentDestinationMRP = findViewById(R.id.documentDestinationMRP);
        documentDestinationPhone = findViewById(R.id.documentDestinationPhone);

        documentNumberLable = findViewById(R.id.documentNumberLable);
        documentDate = findViewById(R.id.documentDate);


        Bundle data = getIntent().getExtras();

        if (data == null) {
            return;
        }
        String docId = data.getString("document_number");


        Documents doc = getDocumentById(docId);
        Contragents source = getContragentById(doc.getSource_id());
        Contragents destination = getContragentById(doc.getDestination_id());

        //source
        documentSourceName.setText(source.getName());
        documentSourceBulstat.setText(source.getBulstat());
        documentSourceVAT.setText(source.getVAT_number());
        documentSourceCity.setText(source.getCity());
        documentSourceAddress.setText(source.getAddress());
        documentSourceMRP.setText(source.getMRP());
        documentSourcePhone.setText(source.getPhone_number());

        //destination
        documentDestinationName.setText(destination.getName());
        documentDestinationBulstat.setText(destination.getBulstat());
        documentDestinationVAT.setText(destination.getVAT_number());
        documentDestinationCity.setText(destination.getCity());
        documentDestinationAddress.setText(destination.getAddress());
        documentDestinationMRP.setText(destination.getMRP());
        documentDestinationPhone.setText(destination.getPhone_number());

        //other
//        String docIDNumber = doc.getId();
        documentNumberLable.setText(String.valueOf(doc.getDocument_number()));

        DataUtil dataUtil = new DataUtil();
        documentDate.setText(dataUtil.formatDate(doc.getDate()));

        documentRows = db.getAllDocumentRows(docId);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_doc_info);
        adapter = new DocumentRowsAdapterNoBTN(this, documentRows, docId, click);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    //using for logo at top right corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    private Documents getDocumentById(String id) {
        List<Documents> documents = db.getAllDocuments();
        for (Documents doc : documents) {
            if(doc.getId().equals(id))return doc;
        }

        return null;
    }

    private Contragents getContragentById(String id) {
        List<Contragents> contragents = db.getAllContragents();
        for (Contragents contragent : contragents) {
            if(contragent.getId().equals(id))return contragent;
        }

        return null;
    }

    public void updateRows(int id) {

    }

    OnDocumentInformationEditClickListener click = new OnDocumentInformationEditClickListener() {

        @Override
        public void onEditClicked() {
            documentRows=db.getAllDocumentRows(adapter.getDocNumber());
            adapter.notifyDataSetChanged();
        }

    };
}