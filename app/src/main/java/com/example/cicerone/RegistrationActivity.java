package com.example.cicerone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cicerone.data.model.DBhelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity {

    DBhelper db;

    private Utente nuovoUtente = new Utente();

    private  EditText nomeUtente;
    private  EditText cognomeUtente;
    private  EditText passwordUtente ;
    private  EditText passwordUtente2 ;
    private  EditText emailUtente;
    private  EditText dataNascitaUtente;
    private  Button inviaDatiUtente;
    private  TextView collegamentoLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        nomeUtente = ( EditText ) findViewById(R.id.nome);
        cognomeUtente = ( EditText ) findViewById(R.id.cognome);
        passwordUtente = ( EditText ) findViewById(R.id.password);
        passwordUtente2 = ( EditText ) findViewById(R.id.password2);
        emailUtente = (EditText ) findViewById(R.id.email);
        inviaDatiUtente = (Button) findViewById(R.id.bottoneInvia);
        collegamentoLogin  = findViewById(R.id.link_login);

       inviaDatiUtente.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String nomeUtenteStr = nomeUtente.getText().toString().trim();
               String cognomeUtenteStr = cognomeUtente.getText().toString().trim();
               String emailUtenteStr = emailUtente.getText().toString().trim();
               String passwordUtenteStr = passwordUtente.getText().toString().trim();
               String passwordUtende2Str = passwordUtente2.getText().toString().trim();

               nuovoUtente.setNome(nomeUtenteStr);
               nuovoUtente.setCognome(cognomeUtenteStr);
               nuovoUtente.setEmail( emailUtenteStr);
               nuovoUtente.setPassword( passwordUtenteStr );

               if (!passwordUtenteStr.equals(passwordUtende2Str))
               {
                   //Password e conferma non coincidono!
                   Toast.makeText( RegistrationActivity.this, "Le password non coincidono!", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   //Se le password coincidono, bisogna controllare che l'utente (email) è gia usato e nel caso scrivere l'utente nel db
                   long val = db.inserisciUtente(nuovoUtente);
                   if (val > 0)
                       finish();
               }
            }
        }
        );

        collegamentoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityLogin();
            }
        });

    }

    public void openActivityLogin()
    {
        Intent Loginintent = new Intent(this, LoginActivity.class );
        startActivity(Loginintent);
    }

}
