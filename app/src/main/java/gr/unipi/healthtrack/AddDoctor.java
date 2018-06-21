package gr.unipi.healthtrack;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class AddDoctor extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    FirebaseAuth mAuth;
    public ProgressBar progressBar;
    private String userID;
    static String uname;
    Button add_doctor_button, remove_doctor_button;
    public String doctor_spinner_text, doctor_uid;
    public Spinner doctorSpinner;
    ArrayAdapter<String> doctorAdapter;
    String NAME, PHONE, BDATE;

    private ListView mList;
    ArrayList<String> doctor_List = new ArrayList<String>();
    ArrayAdapter list_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        doctorSpinner = findViewById(R.id.doctor_spinner);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mList = findViewById(R.id.listview);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        DatabaseReference userRef = mFirebaseDatabase.getReference("Users");

        doctor_List.add("You have selected the following doctors: ");
        doctor_List.add("-------------------------------------------------------------------");
        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        doctor_List.add("Doctor name: " + ds.child("doctor_name").getValue().toString());
                        doctor_List.add(" ");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        list_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, doctor_List);
        mList.setAdapter(list_adapter);

        loadUserInformation();


        userRef.orderByChild("type").equalTo("Doctor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> doctors = new ArrayList<String>();
                final List<String> uids = new ArrayList<String>();

                for (DataSnapshot doctorSnapshot : dataSnapshot.getChildren()){
                    String doctorName = doctorSnapshot.child("name").getValue(String.class);
                    String doctorUid = doctorSnapshot.getKey();
                    doctors.add(doctorName);
                    uids.add(doctorUid);
                }

                doctorAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, doctors);
                doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                doctorSpinner.setAdapter(doctorAdapter);
                doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        doctor_spinner_text = parent.getItemAtPosition(position).toString();
                        doctor_uid = uids.get(position);
                        //Toast.makeText(getApplicationContext(), doctor_uid, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        add_doctor_button = findViewById(R.id.button_add_doctor);
        add_doctor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDoctor();
            }
        });
        remove_doctor_button = findViewById(R.id.button_remove_doctor);
        remove_doctor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDoctor();
            }
        });

    }

    private void addDoctor(){

        final String Doctor_id = doctor_uid;
        final String Doctor_name = doctor_spinner_text;

        final String Patient_id = userID;
        final String Patient_name = uname;

        progressBar.setVisibility(View.VISIBLE);

        Doctor Doctor = new Doctor(
                Doctor_id,
                Doctor_name
        );

        Patient Patient = new Patient(
                Patient_id,
                Patient_name
        );

        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Doctors").child(doctor_uid).setValue(Doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(), "Successfully added doctor.!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        FirebaseDatabase.getInstance().getReference("Users").child(doctor_uid).child("Patients").child(userID).setValue(Patient).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(), "Successfully added patient.!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

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
        FirebaseUser user = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = user.getUid();
        TextView textViewWelcome;
        textViewWelcome = findViewById(R.id.textViewWelcome);
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserInformation uInfo = new UserInformation();
            uInfo.setName(ds.child(userID).getValue(UserInformation.class).getName()); //set the name
            uInfo.setBdate(ds.child(userID).getValue(UserInformation.class).getBdate()); //set the bdate
            uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail()); //set the email
            uInfo.setClinic_spinner_text(ds.child(userID).getValue(UserInformation.class).getClinic_spinner_text()); //set the clinic_spinner_text
            uInfo.setSex_spinner_text(ds.child(userID).getValue(UserInformation.class).getSex_spinner_text()); //set the sex_spinner_text
            uInfo.setType(ds.child(userID).getValue(UserInformation.class).getType());//set the type
            uInfo.setPhone(ds.child(userID).getValue(UserInformation.class).getPhone()); //set the phone
            uname = ds.child(userID).getValue(UserInformation.class).getName();
            NAME = ds.child(userID).getValue(UserInformation.class).getName();
            PHONE = ds.child(userID).getValue(UserInformation.class).getPhone();
            BDATE = ds.child(userID).getValue(UserInformation.class).getBdate();

            textViewWelcome.setText("Welcome " + uname + ".");
        }
    }

    private void removeDoctor(){
        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Doctors").child(doctor_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    dataSnapshot.getRef().setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("Users").child(doctor_uid).child("Patients").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    dataSnapshot.getRef().setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
            case R.id.menuEditProfile:

                Intent i = new Intent(AddDoctor.this, EditProfile.class);
                i.putExtra("phone", PHONE);
                i.putExtra("bdate", BDATE);
                i.putExtra("name", NAME);
                startActivity(i);
                break;
        }

        return true;
    }

}
