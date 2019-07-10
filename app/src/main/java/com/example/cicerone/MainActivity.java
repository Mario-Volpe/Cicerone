package com.example.cicerone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bottone che passa all'activity login
        Button buttonLogin = findViewById(R.id.button_login);
        //bottone che passa all'activity registrazione
        Button buttonRegistration = findViewById(R.id.button_registrazione);

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
        Intent loginIntent = new Intent(this, LoginActivity.class );
        startActivity(loginIntent);
    }

    public void openActivityRegistration()
    {
        Intent registrationIntent = new Intent(this, RegistrationActivity.class);
        startActivity(registrationIntent);
    }
}
