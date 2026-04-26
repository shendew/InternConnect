package com.kingdew.internconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.kingdew.internconnect.R;
import com.kingdew.internconnect.interfaces.OnFilterAppliedListner;

public class FilterDialogAdapter {

    private Context context;
    private OnFilterAppliedListner listner;
    private AlertDialog dialog;

    public FilterDialogAdapter(Context context, OnFilterAppliedListner listner) {
        this.context = context;
        this.listner = listner;
    }

    public void showFilterDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.filter_dialog,null);
        builder.setView(view);

        dialog= builder.create();

        Spinner spWorkType=view.findViewById(R.id.spinner_work_type);
        Spinner spPaidType=view.findViewById(R.id.spinner_paid);
        Spinner spFulltimeType=view.findViewById(R.id.spinner_fulltime);
        Button applyBtn=view.findViewById(R.id.btn_apply_filter);
        Button resetBtn=view.findViewById(R.id.btn_reset_filter);

        String[] workTypes = {"All","Onsite", "Remote", "Hybrid"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, workTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWorkType.setAdapter(adapter);

        String[] fulltimeTypes = {"All", "Full-Time", "Part-Time"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, fulltimeTypes);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFulltimeType.setAdapter(adapter2);

        String[] paidTypes = {"All", "Paid", "Non-Paid"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, paidTypes);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPaidType.setAdapter(adapter3);


        applyBtn.setOnClickListener(v -> {
            if (listner != null){
                listner.onFilterSelected(
                        spPaidType.getSelectedItemPosition()==0?null:spPaidType.getSelectedItemPosition()==1?true:false,
                        spFulltimeType.getSelectedItemPosition()==0?null:spFulltimeType.getSelectedItemPosition()==1?true:false,
                        spWorkType.getSelectedItemPosition()==0?null:spWorkType.getSelectedItemPosition()-1
                );
                dialog.dismiss();
            }
        });
        resetBtn.setOnClickListener(v -> {
            spFulltimeType.setSelection(0);
            spPaidType.setSelection(0);
            spWorkType.setSelection(0);
            listner.onFilterSelected(null,null,null);
            dialog.dismiss();
        });

        dialog.show();


    }
}
