package com.tesi.marco.filo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Marco on 07/02/2018.
 */

public class MedicineAdapter extends BaseAdapter {

    Context context;
    ArrayList<Medicine> medicineList;
    LayoutInflater inflater;
    private FirebaseDatabase database;
    private Medicine medicine;

    public MedicineAdapter(Context context, ArrayList<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @Override
    public int getCount() {
        return medicineList.size();
    }

    @Override
    public Medicine getItem(int position) {
        return medicineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}
