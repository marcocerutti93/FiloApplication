package com.tesi.marco.filo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TherapyActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference therapiesRef;
    private String Uid;
    private TextView tv8, tv9, tv10, tv11, tv12, tv13, tv14, tv15, tv16, tv17, tv18, tv19, tv20;
    private RelativeLayout rl8, rl9, rl10, rl11, rl12, rl13, rl14, rl15, rl16, rl17, rl18, rl19, rl20;
    private Map<String,ArrayList<String>> hmDrugs;
    private ArrayList<String> alDrugs;
    private String[] drug8, drug9, drug10, drug11, drug12, drug13, drug14, drug15, drug16, drug17, drug18, drug19, drug20;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy);

        mProgressView = findViewById(R.id.therapy_progress_bar);
        mProgressView.setVisibility(View.VISIBLE);

        //Views
        tv8 = (TextView) findViewById(R.id.time8_drugs);
        rl8 = (RelativeLayout) findViewById(R.id.time8);
        tv9 = (TextView) findViewById(R.id.time9_drugs);
        rl9 = (RelativeLayout) findViewById(R.id.time9);
        tv10 = (TextView) findViewById(R.id.time10_drugs);
        rl10 = (RelativeLayout) findViewById(R.id.time10);
        tv11 = (TextView) findViewById(R.id.time11_drugs);
        rl11 = (RelativeLayout) findViewById(R.id.time11);
        tv12 = (TextView) findViewById(R.id.time12_drugs);
        rl12 = (RelativeLayout) findViewById(R.id.time12);
        tv13 = (TextView) findViewById(R.id.time13_drugs);
        rl13 = (RelativeLayout) findViewById(R.id.time13);
        tv14 = (TextView) findViewById(R.id.time14_drugs);
        rl14 = (RelativeLayout) findViewById(R.id.time14);
        tv15 = (TextView) findViewById(R.id.time15_drugs);
        rl15 = (RelativeLayout) findViewById(R.id.time15);
        tv16 = (TextView) findViewById(R.id.time16_drugs);
        rl16 = (RelativeLayout) findViewById(R.id.time16);
        tv17 = (TextView) findViewById(R.id.time17_drugs);
        rl17 = (RelativeLayout) findViewById(R.id.time17);
        tv18 = (TextView) findViewById(R.id.time18_drugs);
        rl18 = (RelativeLayout) findViewById(R.id.time18);
        tv19 = (TextView) findViewById(R.id.time19_drugs);
        rl19 = (RelativeLayout) findViewById(R.id.time19);
        tv20 = (TextView) findViewById(R.id.time20_drugs);
        rl20 = (RelativeLayout) findViewById(R.id.time20);

        auth = FirebaseAuth.getInstance();
        Uid = auth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance();
        therapiesRef = db.getReference().child("patients").child(Uid).child("Therapies");

        therapiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hmDrugs = new HashMap<String,ArrayList<String>>();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    String[] assumption = childSnapshot.child("assumption").getValue().toString().split(";");

                    for (int i=0; i<assumption.length; i++){
                        /*if (hmDrugs.containsKey(assumption[i])){
                            alDrugs = hmDrugs.get(assumption[i]);
                        } else {
                            alDrugs = new ArrayList<String>();
                        }
                        alDrugs.add(childSnapshot.child("drug").getValue().toString());
                        hmDrugs.put(assumption[i],alDrugs);*/

                        if (assumption[i].equals("8.00")){
                            tv8.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv8.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv8.append("\n");
                            }
                            rl8.setVisibility(View.VISIBLE);
                        }
                        if (assumption[i].equals("9.00")){
                            tv9.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv9.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv9.append("\n");
                            }
                            rl9.setVisibility(View.VISIBLE);
                        }
                        if (assumption[i].equals("10.00")){
                            tv10.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv10.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv10.append("\n");
                            }
                            rl10.setVisibility(View.VISIBLE);
                        }
                        if (assumption[i].equals("11.00")){
                            tv11.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv11.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv11.append("\n");
                            }
                            rl11.setVisibility(View.VISIBLE);
                        }
                        if (assumption[i].equals("12.00")){
                            tv12.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv12.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv12.append("\n");
                            }
                            rl12.setVisibility(View.VISIBLE);
                        }
                        if (assumption[i].equals("13.00")){
                            tv13.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv13.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv13.append("\n");
                            }
                            rl13.setVisibility(View.VISIBLE);
                        }
                        if (assumption[i].equals("14.00")){
                            tv14.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv14.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv14.append("\n");
                            }
                            rl14.setVisibility(View.VISIBLE);
                        }
                        if (assumption[i].equals("15.00")){
                            tv15.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv15.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv15.append("\n");
                            }
                            rl15.setVisibility(View.VISIBLE);
                        }
                        if (assumption[i].equals("16.00")){
                            tv16.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv16.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv16.append("\n");
                            }
                            rl16.setVisibility(View.VISIBLE);
                        }
                        if (assumption[i].equals("17.00")){
                            tv17.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv17.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv17.append("\n");
                            }
                            rl17.setVisibility(View.VISIBLE);
                        }
                        if (assumption[i].equals("18.00")){
                            tv18.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv18.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv18.append("\n");
                            }
                            rl18.setVisibility(View.VISIBLE);
                        }
                        if (assumption[i].equals("19.00")){
                            tv19.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv19.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv19.append("\n");
                            }
                            rl19.setVisibility(View.VISIBLE);
                        }
                        if (assumption[i].equals("20.00")){
                            tv20.append(childSnapshot.child("drug").getValue().toString());
                            if (childSnapshot.child("afterMeal").getValue().toString().equals("SI")){
                                tv20.append(", " + getString(R.string.after_meal) + "\n");
                            } else {
                                tv20.append("\n");
                            }
                            rl20.setVisibility(View.VISIBLE);
                        }



                    }



                }

                /*if (hmDrugs.containsKey("8.00")){
                    for (int i=0; i<hmDrugs.get("8.00").size(); i++){
                        Toast.makeText(TherapyActivity.this, hmDrugs.get("8.00").get(i), Toast.LENGTH_SHORT).show();
                    }
                }*/

                mProgressView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
