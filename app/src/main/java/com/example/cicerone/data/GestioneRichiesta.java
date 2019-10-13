package com.example.cicerone.data;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cicerone.R;

public class GestioneRichiesta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_richiesta);

        Button rifiuta = findViewById(R.id.rifiuta);
        Button accetta = findViewById(R.id.accetta);

        final Prenotazione p = (Prenotazione) getIntent().getExtras().get("prenotazione");

        accetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                azione(1,p);
            }
        });

        rifiuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                azione(2,p);
            }
        });
    }

    void azione(int flag,Prenotazione p){
        final EditText commenti = findViewById(R.id.commenti);

        String commentiStr = commenti.getText().toString().trim();
        if(commentiStr.length()<=1)
            Toast.makeText(GestioneRichiesta.this, "Il campo è obbligatorio.", Toast.LENGTH_SHORT).show();
        else{
            p.setFlagConferma(flag);
            p.setCommenti(commentiStr);
            if(new DBhelper(GestioneRichiesta.this).updatePrenotazione(p)<=0)
                Toast.makeText(GestioneRichiesta.this, "Errore.", Toast.LENGTH_SHORT).show();
            else {
                String subject;
                String corpo;

                Toast.makeText(GestioneRichiesta.this, "Operazione effettuata.", Toast.LENGTH_SHORT).show();
                Attivita a= new DBhelper(this).getAttivita(p.getIdAttivita());
                if(flag==1) {
                    subject = "Conferma prenotazione";
                    corpo = "Ciao!\n\nIl Cicerone " + a.getCicerone() + " ha confermato la tua prenotazione dall'attività n " + p.getIdAttivita() +
                            " che si svolge a " + a.getCitta() + " il " + a.getData() + ". Commento: "+commentiStr+"\n\nIl team Step di Cicerone.";
                }
                else {
                    subject = "Annullamento prenotazione";
                    corpo = "Ciao!\n\nIl Cicerone " + a.getCicerone() + " ha rifiutato la tua richiesta di partecipazione all'attività n " + p.getIdAttivita() +
                            " che si svolge a " + a.getCitta() + " il " + a.getData() + ". Commento: "+commentiStr+"\n\nIl team Step di Cicerone.";
                }
                SendIt sendIt = new SendIt(p.getEmail(), subject, corpo, this);
                sendIt.execute();
                finish();
            }
            finish();
        }
    }
}
