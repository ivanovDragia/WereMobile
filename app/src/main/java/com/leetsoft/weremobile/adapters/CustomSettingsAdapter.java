package com.leetsoft.weremobile.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TooltipCompat;

import com.leetsoft.weremobile.R;
import com.leetsoft.weremobile.database.DatabaseHelper;
import com.leetsoft.weremobile.database.Settings;
import com.leetsoft.weremobile.interfaces.OnSettingsAdapterButtonClickListener;

import java.util.ArrayList;
import java.util.List;

public class CustomSettingsAdapter extends ArrayAdapter<Settings> {
    private Context sContext;
    private List<Settings> settingsList=new ArrayList<>();
    DatabaseHelper db = new DatabaseHelper(getContext());
    OnSettingsAdapterButtonClickListener mInterface;
    String i;
    String lastSelectedName;
    String lastSelectedValue;
    String lastSelectedDescription;


    public CustomSettingsAdapter(@NonNull Context context, ArrayList<Settings> list, OnSettingsAdapterButtonClickListener mInterface) {
        super(context, 0, list);
        sContext=context;
        settingsList=list;
        this.mInterface=mInterface;
    }

    @Override
    public int getCount() {
        return settingsList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem=convertView;
        if(listItem==null)
            listItem= LayoutInflater.from(sContext).inflate(R.layout.custom_settings_row,parent,false);

        Settings currentSettings = settingsList.get(position);

        TextView name=listItem.findViewById(R.id.nameTextView);
        name.setText(currentSettings.getName());
        TooltipCompat.setTooltipText(name, "Setting name");

        TextView value=listItem.findViewById(R.id.valueTextView);
        value.setText(currentSettings.getValue());
        TooltipCompat.setTooltipText(value, "Setting value");

        TextView description=listItem.findViewById(R.id.descriptionTextView);
        description.setText(currentSettings.getDescription());
        TooltipCompat.setTooltipText(description, "Setting description");

        ImageButton deleteButton=listItem.findViewById(R.id.buttonDelete);
        ImageButton editButton=listItem.findViewById(R.id.buttonEdit);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle(currentSettings.getName());
                alertDialog.setMessage("Confirm Delete?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", (dialog, which) -> {
                    db.deleteSetting(new Settings(currentSettings.getId(),currentSettings.getName(),currentSettings.getValue(),currentSettings.getDescription()));
                    mInterface.onDeleteClicked();
                    db.close();
                    dialog.dismiss();
                });


                alertDialog.show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=currentSettings.getId();
                lastSelectedName=currentSettings.getName();
                lastSelectedValue=currentSettings.getValue();
                lastSelectedDescription=currentSettings.getDescription();
                mInterface.onEditClicked(convertView, position);
            }
        });

        return listItem;
    }

    public String getI() {
        return i;
    }

    public String getLastSelectedName() {
        return lastSelectedName;
    }

    public String getLastSelectedValue() {
        return lastSelectedValue;
    }

    public String getLastSelectedDescription() {
        return lastSelectedDescription;
    }


}
