package gr.unipi.healthtrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity{

    String TYPE;
    private TextView textViewSignUp;
    private Button button_login;
    private FirebaseAuth mAuth;
    EditText editTextPassword, editTextEmail;
    ProgressBar progressBar;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    String userID;
    UserInformation uInfo = new UserInformation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);

        mAuth = FirebaseAuth.getInstance();

        button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });
    }

    public void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


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

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()/* && isPatient()*/){
                    FirebaseUser user = mAuth.getCurrentUser();
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    myRef = mFirebaseDatabase.getReference();
                    userID = user.getUid();
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //This method is called once with the initial value and again whenever data at this location is updated.
                            showData(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG ).show();
                }
            }
        });
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
            finish();
            //Toast.makeText(getApplicationContext(), "Successful login "+ TYPE +" .", Toast.LENGTH_LONG ).show();
            openPatientList();
        }else if(TYPE != null && TYPE.equals("Patient") && TYPE != ""){
            progressBar.setVisibility(View.GONE);
            finish();
            //Toast.makeText(getApplicationContext(), "Successful login "+ TYPE +" .", Toast.LENGTH_LONG ).show();
            openSymptomList();
        }else if(TYPE == null){
            progressBar.setVisibility(View.GONE);
            finish();
            Toast.makeText(getApplicationContext(), "Something went wrong, and can't fetch user info.", Toast.LENGTH_LONG ).show();
        }
    }

    public void openSymptomList(){
        Intent symptomlist_intent = new Intent(this, SymptomList.class);
        startActivity(symptomlist_intent);
    }

    public void openRegister(){
        Intent register_intent = new Intent(this, Register.class);
        finish();
        startActivity(register_intent);
    }

    protected void onStart() {
        super.onStart();

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

    public void openPatientList(){
        Intent patientlist_intent = new Intent(this, PatientList.class);
        startActivity(patientlist_intent);
    }
}
