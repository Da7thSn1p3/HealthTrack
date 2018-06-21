package gr.unipi.healthtrack;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class EditProfile extends AppCompatActivity {

    String TYPE, NAME, BDATE, PHONE, SEX, CLINIC;

    EditText editTextName,editTextPhone,editTextBdate;
    FirebaseAuth mAuth;
    public ProgressBar progressBar;
    public String clinic_spinner_text="",sex_spinner_text="";
    public Button button_save;
    public Spinner sex_spinner,clinic_spinner,type_spinner;
    public ArrayAdapter<CharSequence> sex_adapter,clinic_adapter;
    DatabaseReference myRef;
    String userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        editTextName = findViewById(R.id.edit_text_name);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextBdate = findViewById(R.id.edit_text_bdate);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        sex_spinner = (Spinner) findViewById(R.id.sex_spinner);
        clinic_spinner = (Spinner) findViewById(R.id.clinic_spinner);

        button_save = findViewById(R.id.button_save);
        button_save.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                saveUser();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            NAME = extras.getString("name");
            PHONE = extras.getString("phone");
            BDATE = extras.getString("bdate");
        }

        editTextPhone.setText(PHONE);
        editTextName.setText(NAME);
        editTextBdate.setText(BDATE);

        //CALENDAR START
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editTextBdate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        editTextBdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(EditProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //CALENDAR END

        //SPINNERS START


        sex_adapter = ArrayAdapter.createFromResource(this, R.array.sex_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        sex_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sex_spinner.setAdapter(sex_adapter);
        sex_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex_spinner_text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        clinic_adapter = ArrayAdapter.createFromResource(this, R.array.clinic_array, android.R.layout.simple_spinner_item);
        clinic_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clinic_spinner.setAdapter(clinic_adapter);
        clinic_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clinic_spinner_text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //NAME = dataSnapshot.child("name").getValue().toString();
                //BDATE = dataSnapshot.child("bdate").getValue().toString();
                //PHONE = dataSnapshot.child("phone").getValue().toString();
                //CLINIC = dataSnapshot.child("clinic_spinner_text").getValue().toString();
                //SEX = dataSnapshot.child("sex_spinner_text").getValue().toString();
                TYPE = dataSnapshot.child("type").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //SPINNERS END
    }

    private void saveUser(){

        final String name = editTextName.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String bdate = editTextBdate.getText().toString().trim();

        if (name.isEmpty()) {
            editTextName.setError(getString(R.string.input_error_name));
            editTextName.requestFocus();
            return;
        }

        if (bdate.isEmpty()) {
            editTextBdate.setError(getString(R.string.input_error_bdate));
            editTextBdate.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            editTextPhone.setError(getString(R.string.input_error_phone));
            editTextPhone.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            editTextPhone.setError(getString(R.string.input_error_phone_invalid));
            editTextPhone.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("name").setValue(name);
        FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("phone").setValue(phone);
        FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("bdate").setValue(bdate);
        FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("clinic_spinner_text").setValue(clinic_spinner_text);
        FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("sex_spinner_text").setValue(sex_spinner_text).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Successfully edited profile.!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    if(TYPE.equals("Doctor")){
                        startActivity(new Intent(getApplicationContext(), PatientList.class));
                    }if(TYPE.equals("Patient")){
                        startActivity(new Intent(getApplicationContext(), SymptomList.class));
                    }

                }
            }
        });

    }
}
