package com.example.cicerone;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.example.cicerone.Utente;


public class RegistrationActivity extends AppCompatActivity {

    DBhelper db;

    private Utente = new Utente();

    private  EditText nomeUtente;
    private  EditText cognomeUtente;
    private  EditText passwordUtente ;
    private  EditText emailUtente;
    private  EditText dataNascitaUtente;
    private  Button inviaDatiUtente;
    private  TextView collegamentoLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //ButterKnife.bind(this);

        nomeUtente = ( EditText ) findViewById(R.id.nome);
        cognomeUtente = ( EditText ) findViewById(R.id.cognome);
        passwordUtente = ( EditText ) findViewById(R.id.password);
        emailUtente = (EditText ) findViewById(R.id.email);
        dataNascitaUtente = (EditText ) findViewById(R.id.dataNascita);
        inviaDatiUtente = (Button) findViewById(R.id.bottoneInvia);
        collegamentoLogin  = findViewById(R.id.link_login);

       inviaDatiUtente.setOnClickListener( new View.OnClickListener() ) {
           @Override
           public void onClick( View v ) {

               Utente u = new Utente();
               String nomeUtenteStr = nomeUtente.getText().toString().trim();
               String cognomeUtenteStr = cognomeUtente.getText().toString().trim();
               String emailUtenteStr = emailUtente.getText().toString().trim();
               String passwordUtenteStr = passwordUtente.getText().toString().trim();

               u.setNome(nomeUtenteStr);
               u.setCognome(cognomeUtenteStr);
               //u.setEmail( emailUtenteStr );
               u.setPassword( passwordUtenteStr );

               long val = db.inserisciUtente( u );

               if (val > 0) {
                  Toast.makeText( RegistrationActivity.this, "Ti sei registrato Pingone", Toast.LENGTH_SHORT ).show();
                  Intent moveToLogin = new Intent ( RegistrationActivity.this, LoginActivity.class );
                  startActivity( moveToLogin );
               }

           }
       }

        collegamentoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        }
    }

    /*public void signup() throws ParseException {
        Log.d(TAG, "Accedi");

        DBhelper helper = new DBhelper( this );


        if (!validate()) {
            onSignupFailed();
            return;
        }else{
            Utente u = new Utente();


        }

        inviaDatiUtente.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this, R.style.AppTheme);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        utente = new DBhelper( this );

        String nome = nomeUtente.getText().toString();
        String cognome = cognomeUtente.getText().toString();
        String email = emailUtente.getText().toString();
        String password = passwordUtente.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = sdf.parse(dataNascitaUtente.getText().toString());
        Calendar dataNascita = Calendar.getInstance();
        dataNascita.setTime(date);


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }*/


    public void onSignupSuccess() {
        inviaDatiUtente.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Registrazione fallita", Toast.LENGTH_LONG).show();
        inviaDatiUtente.setEnabled(true);
    }

    public boolean validate() throws ParseException {
        boolean valid = true;

        String nome = nomeUtente.getText().toString();
        String cognome = cognomeUtente.getText().toString();
        String email = emailUtente.getText().toString();
        String password = passwordUtente.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = sdf.parse(dataNascitaUtente.getText().toString());
        Calendar dataNascita = Calendar.getInstance();
        dataNascita.setTime(date);

        if (nome.isEmpty() || nome.length() < 3) {
            nomeUtente.setError("almeno 3 caratteri");
            valid = false;
        } else {
            nomeUtente.setError(null);
        }

        if (cognome.isEmpty() || cognome.length() < 3) {
            cognomeUtente.setError("almeno 3 caratteri");
            valid = false;
        } else {
            cognomeUtente.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailUtente.setError("inserisci un indirizzo email valido");
            valid = false;
        } else {
            emailUtente.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordUtente.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordUtente.setError(null);
        }

        return valid;
    }
}
