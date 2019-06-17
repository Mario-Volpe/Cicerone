package com.example.cicerone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.cicerone.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bottone che passa all'activity login
        buttonLogin = (Button) findViewById(R.id.button_login);
        //bottone che passa all'activity registrazione
        buttonRegistration = (Button) findViewById(R.id.button_registrazione);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityLogin();
            }
        });

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityRegistration();
            }
        });
    }

    public void openActivityLogin()
    {
        Intent Loginintent = new Intent(this, LoginActivity.class );
        startActivity(Loginintent);
    }

    //bisogna creare registration activity
    public void openActivityRegistration()
    {
        Intent RegistrationIntent = new Intent(this, RegistrationActivity.class);
        startActivity(RegistrationIntent);
    }
}
