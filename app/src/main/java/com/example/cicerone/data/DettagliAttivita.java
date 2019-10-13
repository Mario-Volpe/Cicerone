package com.example.cicerone.data;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cicerone.R;

import java.util.ArrayList;

public class DettagliAttivita extends AppCompatActivity {
    private String chiamante;
    private TextView description;
    private Button feedback;
    private ArrayList<Prenotazione> p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_attivita);

        description = findViewById(R.id.description);
        TextView city = findViewById(R.id.city);
        TextView date = findViewById(R.id.date);
        TextView hour = findViewById(R.id.hour);
        TextView tongue = findViewById(R.id.tongue);
        final TextView npartecipanti = findViewById(R.id.npartecipanti);
        TextView npartecipantitxt = findViewById(R.id.npartecipantitxt);
        TextView cicerone = findViewById(R.id.cicerone);
        TextView ciceronetxt = findViewById(R.id.ciceronetxt);
        Button rimuovi = findViewById(R.id.rimozione);
        TextView alert = findViewById(R.id.alert);
        feedback = findViewById(R.id.feedback);

        final Integer id = getIntent().getExtras().getInt("id");

        final Attivita a = new DBhelper(DettagliAttivita.this).getAttivita(id);
        final Utente u = new DBhelper(this).getInfoUtentebyID(a.getCicerone());

        chiamante = getIntent().getExtras().getString("chiamante");

        inizializza(rimuovi,alert,npartecipantitxt,npartecipanti,cicerone,ciceronetxt,a);

        city.setText(a.getCitta());
        date.setText(a.getData());
        hour.setText(a.getOra().toString());

        ArrayList<Lingua> l = new DBhelper(this).getAllLingue();

        String lingua="";
        for(Lingua l2:l){
            if(l2.getId().equals(a.getLingua()))
                lingua=l2.getNome();
        }

        tongue.setText(lingua);

        p = new DBhelper(this).getAllPrenotazioni(id,chiamante);

        rimuovi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottone(chiamante,a,id,u);
            }
        });
    }

    private void inizializza(Button rimuovi, TextView alert, TextView npartecipantitxt, TextView npartecipanti, TextView cicerone, TextView ciceronetxt, final Attivita a){

        if(chiamante.equals("cerca")) {
            rimuovi.setText("Partecipa");
            alert.setText("Inoltrando la richiesta di partecipazione bisogna aspettare di essere accettati dal Cicerone.");
            npartecipantitxt.setText("Posti disponibili:");
            npartecipanti.setText(""+getIntent().getExtras().getInt("postidisponibili"));
            cicerone.setText(a.getCicerone());
            description.setText(a.getDescrizione());

            feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inte = new Intent(DettagliAttivita.this,ElencoFeedback.class);
                    inte.putExtra("id",a.getCicerone());
                    startActivity(inte);
                }
            });

        }
        if(chiamante.equals("modifica")) {
            npartecipanti.setText(""+a.getMaxPartecipanti());
            cicerone.setVisibility(View.INVISIBLE);
            ciceronetxt.setVisibility(View.INVISIBLE);
            feedback.setVisibility(View.INVISIBLE);
            description.setText(a.getDescrizione());
        }
        if(chiamante.equals("inoltrate")) {
            rimuovi.setText("Annulla prenotazione");
            alert.setVisibility(View.INVISIBLE);
            feedback.setVisibility(View.INVISIBLE);
            npartecipantitxt.setText("Posti prenotati:");
            npartecipanti.setText(""+getIntent().getExtras().getInt("prenotati"));
            cicerone.setText(a.getCicerone());
            if(getIntent().getExtras().getInt("flag")==0)
                description.setText(a.getDescrizione());
            else description.setText(getIntent().getExtras().getString("descrizione"));
        }
    }

    private void bottone(String chiamante,Attivita a,Integer id,Utente u){
        if(chiamante.equals("modifica")){
            if(new DBhelper(DettagliAttivita.this).rimuoviAttivita(a.getIdAttivita())==0)
                Toast.makeText(DettagliAttivita.this, "Errore nella rimozione.", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(DettagliAttivita.this, "Rimozione completata.", Toast.LENGTH_SHORT).show();
                for(Prenotazione p2:p){
                    String subject = "Rimozione attività";
                    String corpo = "Ciao!\n\nPurtroppo il Cicerone "+a.getCicerone()+" ha rimosso la sua attività n "+a.getIdAttivita()+
                            " che si sarebbe svolta a "+a.getCitta()+" il "+a.getData()+".\n\nIl team Step di Cicerone.";
                    SendIt sendIt = new SendIt(p2.getEmail(),subject,corpo,this);
                    sendIt.execute();
                }
                finish();
            }
        }

        if(chiamante.equals("cerca")) {
            //richiesta di partecipazione
            int partecipanti = getIntent().getExtras().getInt("npartecipanti");
            String email = getIntent().getExtras().getString("email");

            if (new DBhelper(DettagliAttivita.this).richiestaPartecipazione(partecipanti,id,email)==-1)
                Toast.makeText(DettagliAttivita.this, "Errore nell'inoltro della richiesta.", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(DettagliAttivita.this, "Richiesta inoltrata.", Toast.LENGTH_SHORT).show();
                String subject = "Richiesta di partecipazione";
                String corpo = "Ciao!\n\nIl Globetrotter "+email+" vorrebbe partecipare all'attività n "+a.getIdAttivita()+
                        " che si svolge a "+a.getCitta()+" il "+a.getData()+".\nCorri nella sezione 'Gestione richieste'"+
                        " e fai la tua scelta.\n\nIl team Step di Cicerone.";
                SendIt sendIt = new SendIt(u.getEmail(),subject,corpo,this);
                sendIt.execute();
                finish();
            }
        }
        if(chiamante.equals("inoltrate")){
            if(new DBhelper(DettagliAttivita.this).rimuoviPrenotazione(a.getIdAttivita())==0)
                Toast.makeText(DettagliAttivita.this, "Errore nell'annullamento.", Toast.LENGTH_SHORT).show();
            else {
                String email = getIntent().getExtras().getString("email");
                Toast.makeText(DettagliAttivita.this, "Annullamento completato.", Toast.LENGTH_SHORT).show();
                String subject = "Annullamento prenotazione";
                String corpo = "Ciao!\n\nL'utente "+email+" ha rimosso la sua prenotazione dall'attività n "+a.getIdAttivita()+
                        " che si svolge a "+a.getCitta()+" il "+a.getData()+".\n\nIl team Step di Cicerone.";
                SendIt sendIt = new SendIt(u.getEmail(),subject,corpo,this);
                sendIt.execute();
                finish();
            }
        }
    }
}
