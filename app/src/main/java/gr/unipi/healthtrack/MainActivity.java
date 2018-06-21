package gr.unipi.healthtrack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity{
    private Button button_login;
    private Button button_info;
    private Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_info = findViewById(R.id.button_info);
        button_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openInfo();
            }
        });

        button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openLogin();
            }
        });

        button_register = findViewById(R.id.button_register);
        button_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRegister();
            }
        });

    }

    public void openInfo(){
        Intent info_intent = new Intent(this, Info.class);
        startActivity(info_intent);
    }
    public void openLogin(){
        Intent login_intent = new Intent(this, Login.class);
        startActivity(login_intent);
    }
    public void openRegister(){
        Intent register_intent = new Intent(this, Register.class);
        startActivity(register_intent);
    }
}
