package gr.unipi.healthtrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;
import android.provider.AlarmClock.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class PatientList extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    FirebaseAuth mAuth;
    private String userID;
    static String uname;
    String type;
    ArrayList<String> patient_List, patient_uid_List;


    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
        mList = findViewById(R.id.listview);

        patient_List = new ArrayList<String>();
        patient_uid_List = new ArrayList<String>();

        patient_List.add("You have been selected by these patients: ");
        patient_List.add("---------------------------------");
        patient_uid_List.add("Patients uid: ");
        patient_uid_List.add("---------------------------------");

        ArrayAdapter list_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, patient_List);
        mList.setAdapter(list_adapter);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadUserInformation();

        loadPatients();

        mList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent patient_intent = new Intent(PatientList.this, PatientPage.class);
                String uid = patient_uid_List.get(position);
                patient_intent.putExtra("p_uid", uid);
                startActivity(patient_intent);
                //Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void loadPatients(){

        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Patients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String patient_name = ds.child("patient_name").getValue(String.class);
                    String patient_uid = ds.getKey();

                    patient_List.add("Patient name: " + patient_name + " ");
                    patient_uid_List.add(patient_uid);
                    //patient_List.add("Symptoms and timestamps: ");
                    //showSymptoms(patient_uid);
                }
                patient_List.add("-----------------------------------------------------------");
                patient_uid_List.add("-----------------------------------------------------------");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
/*
    public void showSymptoms(String uid){
        FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Symptoms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot SymptomSnapshot) {
                if(SymptomSnapshot.exists()) {
                    for(DataSnapshot ds : SymptomSnapshot.getChildren()) {
                        //Toast.makeText(getApplicationContext(), ds.child("symptom").getValue().toString(), Toast.LENGTH_LONG).show();
                        patient_List.add("Symptom: " + ds.child("symptom").getValue().toString());
                        patient_List.add("Time: " + ds.child("timestamp").getValue().toString());
                        patient_List.add("Comment: " + ds.child("comment").getValue().toString());
                        patient_List.add("---------------------------------");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    */

    private void loadUserInformation() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot){
        TextView textViewWelcome;
        textViewWelcome = findViewById(R.id.textViewWelcome);
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserInformation uInfo = new UserInformation();
            uInfo.setName(ds.child(userID).getValue(UserInformation.class).getName()); //set the name
            uname = ds.child(userID).getValue(UserInformation.class).getName();
            uInfo.setBdate(ds.child(userID).getValue(UserInformation.class).getBdate()); //set the bdate
            uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail()); //set the email
            uInfo.setClinic_spinner_text(ds.child(userID).getValue(UserInformation.class).getClinic_spinner_text()); //set the clinic_spinner_text
            uInfo.setSex_spinner_text(ds.child(userID).getValue(UserInformation.class).getSex_spinner_text()); //set the sex_spinner_text
            uInfo.setType(ds.child(userID).getValue(UserInformation.class).getType());
            uInfo.setPhone(ds.child(userID).getValue(UserInformation.class).getPhone()); //set the phone

            textViewWelcome.setText("Welcome " + uname + ".");
        }
    }

    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            //handle the already login user
            finish();
            openLogin();
        }
    }

    public void openLogin(){
        Intent signin_intent = new Intent(this, Login.class);
        finish();
        startActivity(signin_intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));

                break;
        }

        return true;
    }
}