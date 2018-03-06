package com.tesi.marco.filo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

/**
 * Created by Marco on 06/03/2018.
 */

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference therapiesRef;
    private String Uid;
    private TextView tv8, tv9, tv10, tv11, tv12, tv13, tv14, tv15, tv16, tv17, tv18, tv19, tv20, tv21, tv22;
    private RelativeLayout rl8, rl9, rl10, rl11, rl12, rl13, rl14, rl15, rl16, rl17, rl18, rl19, rl20, rl21, rl22;
    private String[] days = {"lun", "mar", "mer", "gio", "ven", "sab", "dom"};

    private int mPage;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.therapy_day, container, false);

        tv8 = (TextView) view.findViewById(R.id.time8_drugs);
        rl8 = (RelativeLayout) view.findViewById(R.id.time8);
        tv9 = (TextView) view.findViewById(R.id.time9_drugs);
        rl9 = (RelativeLayout) view.findViewById(R.id.time9);
        tv10 = (TextView) view.findViewById(R.id.time10_drugs);
        rl10 = (RelativeLayout) view.findViewById(R.id.time10);
        tv11 = (TextView) view.findViewById(R.id.time11_drugs);
        rl11 = (RelativeLayout) view.findViewById(R.id.time11);
        tv12 = (TextView) view.findViewById(R.id.time12_drugs);
        rl12 = (RelativeLayout) view.findViewById(R.id.time12);
        tv13 = (TextView) view.findViewById(R.id.time13_drugs);
        rl13 = (RelativeLayout) view.findViewById(R.id.time13);
        tv14 = (TextView) view.findViewById(R.id.time14_drugs);
        rl14 = (RelativeLayout) view.findViewById(R.id.time14);
        tv15 = (TextView) view.findViewById(R.id.time15_drugs);
        rl15 = (RelativeLayout) view.findViewById(R.id.time15);
        tv16 = (TextView) view.findViewById(R.id.time16_drugs);
        rl16 = (RelativeLayout) view.findViewById(R.id.time16);
        tv17 = (TextView) view.findViewById(R.id.time17_drugs);
        rl17 = (RelativeLayout) view.findViewById(R.id.time17);
        tv18 = (TextView) view.findViewById(R.id.time18_drugs);
        rl18 = (RelativeLayout) view.findViewById(R.id.time18);
        tv19 = (TextView) view.findViewById(R.id.time19_drugs);
        rl19 = (RelativeLayout) view.findViewById(R.id.time19);
        tv20 = (TextView) view.findViewById(R.id.time20_drugs);
        rl20 = (RelativeLayout) view.findViewById(R.id.time20);
        tv21 = (TextView) view.findViewById(R.id.time21_drugs);
        rl21 = (RelativeLayout) view.findViewById(R.id.time21);
        tv22 = (TextView) view.findViewById(R.id.time22_drugs);
        rl22 = (RelativeLayout) view.findViewById(R.id.time22);

        auth = FirebaseAuth.getInstance();
        Uid = auth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        therapiesRef = db.getReference().child("patients").child(Uid).child("Therapies");

        therapiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    String[] assumptionDays = childSnapshot.child("assumptionDays").getValue().toString().split(",");
                    String[] assumptionHours = childSnapshot.child("assumptionHours").getValue().toString().split(",");

                    if (Arrays.asList(assumptionDays).contains(days[mPage-1])){
                        for (int i=0; i<assumptionHours.length; i++){
                            if (assumptionHours[i].equals("8.00")){
                                tv8.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv8.append(", " + extra + "\n");
                                } else {
                                    tv8.append("\n");
                                }
                                rl8.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("9.00")){
                                tv9.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv9.append(", " + extra + "\n");
                                } else {
                                    tv9.append("\n");
                                }
                                rl9.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("10.00")){
                                tv10.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv10.append(", " + extra + "\n");
                                } else {
                                    tv10.append("\n");
                                }
                                rl10.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("11.00")){
                                tv11.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv11.append(", " + extra + "\n");
                                } else {
                                    tv11.append("\n");
                                }
                                rl11.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("12.00")){
                                tv12.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv12.append(", " + extra + "\n");
                                } else {
                                    tv12.append("\n");
                                }
                                rl12.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("13.00")){
                                tv13.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv13.append(", " + extra + "\n");
                                } else {
                                    tv13.append("\n");
                                }
                                rl13.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("14.00")){
                                tv14.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv14.append(", " + extra + "\n");
                                } else {
                                    tv14.append("\n");
                                }
                                rl14.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("15.00")){
                                tv15.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv15.append(", " + extra + "\n");
                                } else {
                                    tv15.append("\n");
                                }
                                rl15.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("16.00")){
                                tv16.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv16.append(", " + extra + "\n");
                                } else {
                                    tv16.append("\n");
                                }
                                rl16.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("17.00")){
                                tv17.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv17.append(", " + extra + "\n");
                                } else {
                                    tv17.append("\n");
                                }
                                rl17.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("18.00")){
                                tv18.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv18.append(", " + extra + "\n");
                                } else {
                                    tv18.append("\n");
                                }
                                rl18.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("19.00")){
                                tv19.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv19.append(", " + extra + "\n");
                                } else {
                                    tv19.append("\n");
                                }
                                rl19.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("20.00")){
                                tv20.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv20.append(", " + extra + "\n");
                                } else {
                                    tv20.append("\n");
                                }
                                rl20.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("21.00")){
                                tv21.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv21.append(", " + extra + "\n");
                                } else {
                                    tv21.append("\n");
                                }
                                rl21.setVisibility(View.VISIBLE);
                            }
                            if (assumptionHours[i].equals("22.00")){
                                tv22.append(childSnapshot.child("drug").getValue().toString());
                                String extra = getInfoMeal(childSnapshot);
                                if (!extra.isEmpty()){
                                    tv22.append(", " + extra + "\n");
                                } else {
                                    tv22.append("\n");
                                }
                                rl22.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private String getInfoMeal(DataSnapshot snap){
        String info = "";
        if (snap.child("infoMeal").getValue().toString().equals("dp")){
            info = getString(R.string.after_meal);
        } else if (snap.child("infoMeal").getValue().toString().equals("sv")){
            info = getString(R.string.before_meal);
        }
        return info;
    }
}
