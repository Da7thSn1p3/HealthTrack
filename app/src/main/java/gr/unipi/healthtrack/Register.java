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


public class Register extends AppCompatActivity {

    String TYPE;

    private TextView textViewSignIn;
    private EditText editTextName,editTextEmail, editTextPassword,editTextPhone,editTextBdate;
    private FirebaseAuth mAuth;
    public ProgressBar progressBar;
    public String clinic_spinner_text="",sex_spinner_text="", type_spinner_text="";
    public Button button_register;
    public Spinner sex_spinner,clinic_spinner,type_spinner;
    public ArrayAdapter<CharSequence> sex_adapter,clinic_adapter, type_adapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    String userID;
    UserInformation uInfo = new UserInformation();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextBdate = findViewById(R.id.edit_text_bdate);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        type_spinner = (Spinner) findViewById(R.id.type_spinner);
        sex_spinner = (Spinner) findViewById(R.id.sex_spinner);
        clinic_spinner = (Spinner) findViewById(R.id.clinic_spinner);

        button_register = findViewById(R.id.button_register);
        button_register.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                registerUser();
            }
        });

        textViewSignIn = findViewById(R.id.textViewSignIn);
        textViewSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

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

                new DatePickerDialog(Register.this, date, myCalendar
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

        type_adapter = ArrayAdapter.createFromResource(this, R.array.type_array, android.R.layout.simple_spinner_item);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(type_adapter);
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type_spinner_text = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //SPINNERS END
    }

    private void registerUser(){

        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
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

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
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
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            User user = new User(
                                    name,
                                    email,
                                    phone,
                                    bdate,
                                    type_spinner_text,
                                    clinic_spinner_text,
                                    sex_spinner_text
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                        if(type_spinner_text.equals("Doctor")){
                                            openPatientList();
                                            finish();
                                        }else if(type_spinner_text.equals("Patient")){
                                            openSymptomList();
                                            finish();
                                        }
                                    } else {

                                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                            Toast.makeText(Register.this, "You are already registered.", Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void openSymptomList(){
        Intent symptomlis_intent = new Intent(this, SymptomList.class);
        startActivity(symptomlis_intent);
    }

    private void showData(DataSnapshot dataSnapshot){
        FirebaseUser user = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = user.getUid();
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            uInfo.setName(ds.child(userID).getValue(UserInformation.class).getName()); //set the name
            uInfo.setBdate(ds.child(userID).getValue(UserInformation.class).getBdate()); //set the bdate
            uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail()); //set the email
            uInfo.setClinic_spinner_text(ds.child(userID).getValue(UserInformation.class).getClinic_spinner_text()); //set the clinic_spinner_text
            uInfo.setSex_spinner_text(ds.child(userID).getValue(UserInformation.class).getSex_spinner_text()); //set the sex_spinner_text
            uInfo.setType(ds.child(userID).getValue(UserInformation.class).getType());//set the type
            uInfo.setPhone(ds.child(userID).getValue(UserInformation.class).getPhone()); //set the phone
            TYPE = ds.child(userID).child("type").getValue(String.class);
        }
        if(TYPE != null && TYPE.equals("Doctor") && TYPE != ""){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Successful login "+ TYPE +" .", Toast.LENGTH_LONG ).show();
            finish();
            openPatientList();
        }else if(TYPE != null && TYPE.equals("Patient") && TYPE != ""){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Successful login "+ TYPE +" .", Toast.LENGTH_LONG ).show();
            finish();
            openSymptomList();
        }else if(TYPE == null){
            progressBar.setVisibility(View.GONE);
            finish();
            Toast.makeText(getApplicationContext(), "Something went wrong, and can't fetch user info.", Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            if (mAuth.getCurrentUser() != null) {
                //handle the already login user
                FirebaseUser user = mAuth.getCurrentUser();
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference();
                userID = user.getUid();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            showData(dataSnapshot);
                        }
                        //This method is called once with the initial value and again whenever data at this location is updated.
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        }
    }

    public void openLogin(){
        Intent signin_intent = new Intent(this, Login.class);
        finish();
        startActivity(signin_intent);
    }

    public void openPatientList(){
        Intent patientlist_intent = new Intent(this, PatientList.class);
        startActivity(patientlist_intent);
    }
}
