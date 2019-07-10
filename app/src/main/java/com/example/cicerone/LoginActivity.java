package com.example.cicerone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cicerone.data.model.DBhelper;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private Utente u;
    private String email,password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        u = new Utente(LoginActivity.this,"","","","","");

        ImageView img = findViewById(R.id.imageView2);

        emailText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        TextView signUpLink = findViewById(R.id.button_registrazione2);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityMain();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticazione...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP && resultCode == RESULT_OK)
            this.finish();
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        u.setEmail(email);
        u.setPassword(password);
        u=new DBhelper(this).getInfoUtente(u);
        loginButton.setEnabled(true);
        Intent Homeintent = new Intent(this,HomeActivity.class);
        Homeintent.putExtra("nome",u.getNome());
        Homeintent.putExtra("cognome",u.getCognome());
        Homeintent.putExtra("datanascita",u.getDatanascita());
        Homeintent.putExtra("email",u.getEmail());
        startActivity(Homeintent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        Utente validateUser = new Utente (LoginActivity.this,password,"","",email,"");

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
            emailText.setError("Inserisci un indirizzo email valido");
            valid = false;
        } else {
            emailText.setError(null);
        }

        DBhelper db = new DBhelper(this);

        /*Verifico che l'email inserita esista per un utente registrato*/
        if (!db.isSignedUp(validateUser))
        {
            emailText.setError("Nessun utente registrato con questo indirizzo mail!");
            valid = false;
        }
        else
        {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 5 || password.length() > 15) {
            passwordText.setError("La password è di almeno 5 caratteri e massimo 15");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        /* Verifico che la password corrisponda a quella dell'utente*/
        if (!db.searchPassword(validateUser).equals(password))
        {
            passwordText.setError("La password inserita non coincide con quella dell'utente!");
            valid = false;
        }
        else {
            passwordText.setError(null);
        }

        return valid;
    }

    public void openActivityMain()
    {
        Intent Mainintent = new Intent(LoginActivity.this, MainActivity.class );
        startActivity(Mainintent);
    }
}