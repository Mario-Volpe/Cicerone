package com.example.cicerone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private TextView mTextMessage,info;
    private Button logoutButton;
    String nome,cognome,datanascita,email;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    logoutButton.setVisibility(View.VISIBLE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    info.setVisibility(View.INVISIBLE);
                    mTextMessage.setText("Ciao "+nome+"!");
                    return true;
                case R.id.navigation_dashboard:
                    logoutButton.setVisibility(View.INVISIBLE);
                    info.setVisibility(View.INVISIBLE);
                    mTextMessage.setText(R.string.title_dashboard);
                    mTextMessage.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_profilo:
                    info.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.VISIBLE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText("Riepilogo dei tuoi dati:");
                    info.setText(nome+"\n"+cognome+"\n"+datanascita+"\n"+email);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        nome = getIntent().getExtras().getString("nome");
        cognome = getIntent().getExtras().getString("cognome");
        datanascita = getIntent().getExtras().getString("datanascita");
        email = getIntent().getExtras().getString("email");
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        info = findViewById(R.id.info);
        info.setVisibility(View.INVISIBLE);
        mTextMessage.setText("Ciao "+nome+"!");
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        logoutButton = (Button) findViewById(R.id.logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Utente(HomeActivity.this,"","","","","").rimuoviUtente();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finish();
            }
        });
    }

}
