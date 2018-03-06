package com.tesi.marco.filo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class TherapyActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private RadioGroup eMedicine;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TherapyFragmentPagerAdapter(getSupportFragmentManager(),
                TherapyActivity.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab tab = tabLayout.getTabAt(getIntent().getIntExtra("tab",0));
        tab.select();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN);
        String myDate = dateFormat.format(Calendar.getInstance().getTime());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String Uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("data").child(Uid).child("Therapy").child(myDate);

        if (getIntent().hasExtra("confirmation")){
            if (getIntent().getStringExtra("confirmation").equals("confirm")){
                ConfirmMedicineAssumption();
            }
        }

    }

    public class TherapyFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 7;
        private String tabTitles[] = new String[]{getString(R.string.mon),
                getString(R.string.tue), getString(R.string.wed),
                getString(R.string.thu), getString(R.string.fri),
                getString(R.string.sat), getString(R.string.sun),};
        private Context context;

        public TherapyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position + 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_therapy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_confirm:
                ConfirmMedicineAssumption();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ConfirmMedicineAssumption(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View medicineDialogueView = inflater.inflate(R.layout.medicine_dialogue,null);
        builder.setTitle(R.string.medicine_today);
        builder.setView(medicineDialogueView);
        eMedicine = (RadioGroup) medicineDialogueView.findViewById(R.id.medicine_radiogroup);

        builder.setCancelable(false);

        builder.setPositiveButton(R.string.ok,null);
        builder.setNegativeButton(
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog medicineDialogue = builder.create();
        medicineDialogue.show();
        Button ok = medicineDialogue.getButton(DialogInterface.BUTTON_POSITIVE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eMedicine.getCheckedRadioButtonId()!=-1){
                    int id = eMedicine.getCheckedRadioButtonId();
                    int radioId = eMedicine.indexOfChild(eMedicine.findViewById(id));
                    RadioButton b = (RadioButton) eMedicine.getChildAt(radioId);
                    String therapy = b.getText().toString();
                    myRef.child("Therapy").setValue(therapy);
                    medicineDialogue.cancel();
                } else {
                    Toast.makeText(TherapyActivity.this, getString(R.string.error_dialog_ok), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
