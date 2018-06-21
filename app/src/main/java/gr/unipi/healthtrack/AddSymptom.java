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

public class AddSymptom extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    FirebaseAuth mAuth;
    public ProgressBar progressBar;
    private String userID;
    static String uname;
    Button add_symptom_button;
    public String ICPC2_spinner_text;
    public String Symptom="";
    public EditText comment_editText;
    public Spinner ICPC2i_spinner;
    ArrayAdapter<CharSequence> ICPC2i_adapter;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    Date date = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptom);

        ICPC2i_spinner = findViewById(R.id.ICD10i_spinner);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        comment_editText = findViewById(R.id.comment_editText);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        loadUserInformation();

        final Spinner ICPC2_spinner = (Spinner) findViewById(R.id.ICD10_spinner);
        ArrayAdapter<CharSequence> ICPC2_adapter = ArrayAdapter.createFromResource(this, R.array.ICPC2_array, android.R.layout.simple_spinner_item);
        ICPC2_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ICPC2_spinner.setAdapter(ICPC2_adapter);
        ICPC2_spinner_text = ICPC2_spinner.getSelectedItem().toString();

        ICPC2_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ICPC2_spinner_text = ICPC2_spinner.getSelectedItem().toString();
                if(ICPC2_spinner_text.equals("A - General and Unspecified")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.A, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("B - Blood, Blood Forming Organs and Immune Mechanism")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.B, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("D - Digestive")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.D, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("F - Eye")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.F, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("H - Ear")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.H, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("K - Cardiovascular")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.K, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("L - Musculoskeletal")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.L, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("N - Neurological")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.N, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("P - Psychological")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.P, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("R - Respiratory")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.R, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("S - Skin")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.S, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("T - Endocrine/Metabolic and Nutritional")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("U - Urological")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.U, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("W - Pregnancy, Childbearing, Family Planning")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.W, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("X - Female Genital")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.X, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("Y - Male Genital")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Y, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if(ICPC2_spinner_text.equals("Z - Social Problems")){
                    ICPC2i_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Z, android.R.layout.simple_spinner_item);
                    ICPC2i_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ICPC2i_spinner.setAdapter(ICPC2i_adapter);
                    ICPC2i_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Symptom = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        add_symptom_button = findViewById(R.id.button_add_symptom);
        add_symptom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSymptom();
            }
        });

    }

    private void addSymptom(){
        final String symptom = Symptom;
        final String endtime = "Not set yet.";
        final String comment = comment_editText.getText().toString().trim();
        String timestamp = dateFormat.format(date);

        progressBar.setVisibility(View.VISIBLE);

        Symptom Symptom = new Symptom(
                timestamp,
                symptom,
                comment,
                endtime
        );

        DatabaseReference pushedSymptomRef = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Symptoms").push();
        String symptomId = pushedSymptomRef.getKey();

        FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Symptoms").child(symptomId).setValue(Symptom).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Successfully added symptom.!", Toast.LENGTH_LONG).show();
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

            textViewWelcome.setText("Welcome " + uname + ".");
        }
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
