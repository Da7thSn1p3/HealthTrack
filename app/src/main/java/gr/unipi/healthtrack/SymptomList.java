package gr.unipi.healthtrack;

import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class SymptomList extends AppCompatActivity {

    String NAME, PHONE, BDATE, date="", date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;

    private DatabaseReference myRef;
    FirebaseAuth mAuth;
    private String userID;
    static String delete_uid, uname;
    String type;
    AlertDialog AlertDialog;
    UserInformation uInfo = new UserInformation();
    Button button_add_symptoms,button_remove_symptoms,button_add_doctor;

    private ListView mList;
    ArrayList<String> symptom_List = new ArrayList<String>();
    ArrayList<String> symptom_uid_List = new ArrayList<String>();
    ArrayAdapter list_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_list);
        button_add_symptoms = findViewById(R.id.button_add_symptoms);
        button_add_symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddSymptom();
            }
        });

        button_add_doctor = findViewById(R.id.button_add_doctor);
        button_add_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDoctor();
            }
        });

        button_remove_symptoms = findViewById(R.id.button_remove_symptom);
        button_remove_symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSymptoms();
            }
        });
        mList = findViewById(R.id.listview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        symptom_List.add("You have saved the following info: ");
        symptom_uid_List.add("");


        list_adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, symptom_List);
        mList.setAdapter(list_adapter);

        loadUserInformation();

        loadSymptoms();

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(symptom_uid_List.get(position+1).equals("Time")){
                    delete_uid = symptom_uid_List.get(position);
                    AlertDialog.Builder adb = new AlertDialog.Builder(SymptomList.this);
                    adb.setTitle("Do you want to delete Symptom?");
                    adb.setMessage("Selected "
                            +parent.getItemAtPosition(position));
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // delete button
                            removeSymptom(delete_uid);
                        }
                    });
                    adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // null button
                        }
                    });

                    adb.show();
                }if(symptom_uid_List.get(position).equals("EndTime")){
                    delete_uid = symptom_uid_List.get(position-2);
                    AlertDialog.Builder adb = new AlertDialog.Builder(SymptomList.this);
                    adb.setTitle("Do you want to delete End Time");
                    adb.setMessage("For selected "
                            +parent.getItemAtPosition(position-2));
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // delete button
                            removeEndTime(delete_uid);
                        }
                    });
                    adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // null button
                        }
                    });

                    adb.show();
                }if(symptom_uid_List.get(position).equals("Time")){
                    delete_uid = symptom_uid_List.get(position-1);
                    AlertDialog.Builder adb = new AlertDialog.Builder(SymptomList.this);
                    adb.setTitle("Do you want to delete Time");
                    adb.setMessage("For selected "
                            +parent.getItemAtPosition(position-1));
                    adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // delete button
                            removeTime(delete_uid);
                        }
                    });
                    adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // null button
                        }
                    });

                    adb.show();
                }
                return true;
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(symptom_uid_List.get(position).equals("EndTime")){
                    String symptom_id = symptom_uid_List.get(position - 2);
                    datePicker(symptom_id, "endtime");
                    //pushEndTime(symptom_id, date_time);
                }
                if(symptom_uid_List.get(position).equals("Time")){
                    String symptom_id = symptom_uid_List.get(position - 1);
                    datePicker(symptom_id, "timestamp");
                }
            }
        });


    }

    private void loadSymptoms(){
        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Symptoms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        //Toast.makeText(getApplicationContext(), ds.child("symptom").getValue().toString(), Toast.LENGTH_LONG).show();
                        symptom_List.add("Symptom: " + ds.child("symptom").getValue().toString());
                        symptom_uid_List.add(ds.getKey());
                        symptom_List.add("Time: " + ds.child("timestamp").getValue().toString());
                        symptom_uid_List.add("Time");
                        if(ds.child("endtime").exists()){
                            symptom_List.add("Symptom End Time: " + ds.child("endtime").getValue().toString());
                            symptom_uid_List.add("EndTime");
                        }
                        symptom_List.add("Comment: " + ds.child("comment").getValue().toString());
                        symptom_uid_List.add("");
                        symptom_List.add("-----------------------------------------------------------");
                        symptom_uid_List.add("");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removeSymptom(String id){
        String delete_id = id;
        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Symptoms").child(delete_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    dataSnapshot.getRef().setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removeEndTime(String id){
        String delete_id = id;
        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Symptoms").child(delete_id).child("endtime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    dataSnapshot.getRef().setValue("Not set yet.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removeTime(String id){
        String delete_id = id;
        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Symptoms").child(delete_id).child("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    dataSnapshot.getRef().setValue("Time not set.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removeSymptoms(){
        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Symptoms").addListenerForSingleValueEvent(new ValueEventListener() {
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

    private void loadUserInformation() {

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
            uInfo.setName(ds.child(userID).getValue(UserInformation.class).getName()); //set the name
            uInfo.setBdate(ds.child(userID).getValue(UserInformation.class).getBdate()); //set the bdate
            uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail()); //set the email
            uInfo.setClinic_spinner_text(ds.child(userID).getValue(UserInformation.class).getClinic_spinner_text()); //set the clinic_spinner_text
            uInfo.setSex_spinner_text(ds.child(userID).getValue(UserInformation.class).getSex_spinner_text()); //set the sex_spinner_text
            uInfo.setType(ds.child(userID).getValue(UserInformation.class).getType());
            uInfo.setPhone(ds.child(userID).getValue(UserInformation.class).getPhone()); //set the phone
            uname = ds.child(userID).getValue(UserInformation.class).getName();
            NAME = ds.child(userID).getValue(UserInformation.class).getName();
            PHONE = ds.child(userID).getValue(UserInformation.class).getPhone();
            BDATE = ds.child(userID).getValue(UserInformation.class).getBdate();

            textViewWelcome.setText("Welcome " + uname + ".");
            symptom_List.add("Patient name: " + uname + " .");
            symptom_uid_List.add("");
            symptom_List.add("-----------------------------------------------------------");
            symptom_uid_List.add("");
        }
    }

    public void openAddSymptom(){
        Intent intent = new Intent(this, AddSymptom.class);
        startActivity(intent);
    }

    public void openAddDoctor(){
        Intent intent = new Intent(this, AddDoctor.class);
        startActivity(intent);
    }

    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            //handle the already login user
            //finish();
            //openLogin();
        }
    }

    public void openLogin(){
        Intent signin_intent = new Intent(this, Login.class);
        startActivity(signin_intent);
    }

    private void datePicker(final String j, final String where1){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                        date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        //*************Call Time Picker Here ********************
                        timePicker(j, where1);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker(final String i, final String where2){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        String time= String.format("%02d:%02d", hourOfDay, minute);

                        date_time = date + " " + time;
                        pushEndTime(i, date_time, where2);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void pushEndTime(String symptom_id, String Time, final String where){

        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Symptoms").child(symptom_id).child(where).setValue(Time).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if(where.equals("endtime")){
                        Toast.makeText(getApplicationContext(), "Successfully added End Time.!", Toast.LENGTH_SHORT).show();
                    }else if(where.equals("timestamp")){
                        Toast.makeText(getApplicationContext(), "Successfully added Time.!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
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

                Intent i = new Intent(SymptomList.this, EditProfile.class);
                i.putExtra("phone", PHONE);
                i.putExtra("bdate", BDATE);
                i.putExtra("name", NAME);
                startActivity(i);
                break;
        }

        return true;
    }
}