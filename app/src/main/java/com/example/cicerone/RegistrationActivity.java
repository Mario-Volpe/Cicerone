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


public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";
    DBhelper utente;

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

        nomeUtente = findViewById(R.id.nome);
        cognomeUtente = findViewById(R.id.cognome);
        passwordUtente = findViewById(R.id.password);
        emailUtente = findViewById(R.id.email);
        dataNascitaUtente = findViewById(R.id.dataNascita);
        inviaDatiUtente = (Button) findViewById(R.id.bottoneInvia);
        collegamentoLogin  = findViewById(R.id.link_login);

       inviaDatiUtente.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try {
                   signup();
               } catch (ParseException e) {
                   e.printStackTrace();
               }
           }
       });

        collegamentoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() throws ParseException {
        Log.d(TAG, "Accedi");

        if (!validate()) {
            onSignupFailed();
            return;
        }else{
            Utente u = new Utente();

            u.setNome( nomeUtente.toString() );
            u.setCognome( cognomeUtente.toString());
            u.setEmail( emailUtente.toString() );
            u.getPassword( passwordUtente.toString() );


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
    }


    public void onSignupSuccess() {
        inviaDatiUtente.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login fallito", Toast.LENGTH_LONG).show();
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
