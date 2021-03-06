package com.example.cicerone.data;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cicerone.R;
import java.util.ArrayList;

public class ElencoAttivita extends AppCompatActivity {
    private static final String INOLTRATE = "inoltrate";
    private static final String MODIFICA = "modifica";
    private static final String RICHIESTE = "richieste";
    private static final String STORICO = "storico";
    private int j=0;
    private int flag=0; //0 se ci sono attività da mostrare, 1 altrimenti.
    private String avv=""; //non ci sono attività
    private Integer[] ids; //array degli id
    private ListView lista;
    private Integer globetrotter;
    private ArrayList<Boolean> f = new ArrayList<>(); //true: feedback presente, false: assente
    ArrayList<Attivita> sf = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_attivita);

        final String chiamante = getIntent().getExtras().getString("chiamante");
        ArrayAdapter<String> adapter;
        EAdapter fadapter=null;
        TextView partecipantitxt = findViewById(R.id.partecipantitxt);

        lista = findViewById(R.id.listaAttivita);
        globetrotter = getIntent().getExtras().getInt("idUtente");

        ArrayList<String> array = new ArrayList<>(); //visualizzati nella lista
        ArrayList<Attivita> s = new ArrayList<>();
        sf = new ArrayList<>();
        ArrayList<Prenotazione> p = new ArrayList<>();

        ArrayList<Integer> r= new ArrayList<>(); //n richieste per attività

        if(chiamante.equals(INOLTRATE)||chiamante.equals(STORICO)){
            p = DBhelper.getAllPrenotazioniUtente(globetrotter);
            Log.e("p size:",""+p.size());
            for(Prenotazione p2:p){
                Attivita a = DBhelper.getAttivita(p2.getIdAttivita());

                Integer[] data = DBhelper.parseData(a.getData());

                if(!DBhelper.checkData(data[2],data[1],data[0],"cerca")) { //controlla se la data è antecedente alla data odierna
                    sf.add(a); //se è antecedente devo mostrarla tra i feedback
                    if(DBhelper.getFeedback(a.getIdAttivita(),globetrotter)!=null)
                        f.add(true);
                    else f.add(false);
                }
                else
                    s.add(a); //in caso contrario dev'essere disponibile al resto delle view
            }

        } else {
            ArrayList<Attivita> s2 = DBhelper.getAllAttivita(getIntent().getExtras().getInt("idUtente"));

            for(Attivita a:s2){
                Integer[] data = DBhelper.parseData(a.getData());

                if(DBhelper.checkData(data[2],data[1],data[0],"cerca"))
                    s.add(a);
            }
        }

        inizializza(chiamante,partecipantitxt,s);

        if (flag == 0) {
                ids = new Integer[s.size()];
                for (Attivita b : s) {
                    array.add(b.toStringSearch());
                    ids[j] = b.getIdAttivita();
                    ArrayList<Prenotazione> p2 = DBhelper.getAllPrenotazioni(ids[j], chiamante);
                    r.add(p2.size());
                    j++;
                }
        }
        else {
            ids = new Integer[1];
            array.add(avv);
        }


        adapter = new ArrayAdapter<>(
                ElencoAttivita.this, android.R.layout.simple_list_item_1, array
        );

        if(flag==0) {
            if (chiamante.equals(MODIFICA))
                fadapter = new EAdapter(this, s, chiamante);
            if (chiamante.equals(RICHIESTE))
                fadapter = new EAdapter(this, s, r, chiamante);
            if (chiamante.equals(INOLTRATE))
                fadapter = new EAdapter(this, s, chiamante, p);
            if (chiamante.equals(STORICO))
                fadapter = new EAdapter(sf,this,f,chiamante);

            lista.setAdapter(fadapter);
            final ArrayList<Prenotazione> pf = p;
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bottone(chiamante,position,pf,ids,sf);
                }
        });
        }
        else lista.setAdapter(adapter);
    }

    private void inizializza(String chiamante,TextView partecipantitxt,ArrayList<Attivita> s){
        if(chiamante.equals(RICHIESTE))
            partecipantitxt.setText("Richieste");
        if(chiamante.equals(INOLTRATE))
            partecipantitxt.setText("Stato");
        if(chiamante.equals(STORICO))
            partecipantitxt.setText("Valutata");

        Log.e("s size:",""+s.size());

        if((s==null||s.size()==0)&&!chiamante.equals(STORICO)) {
            if(chiamante.equals(INOLTRATE)){
                avv = "Nessuna richiesta inoltrata";
                Toast.makeText(ElencoAttivita.this, avv, Toast.LENGTH_SHORT).show();
            }
            else{
                avv = "Nessuna attività creata";
                Toast.makeText(ElencoAttivita.this, avv, Toast.LENGTH_SHORT).show();
            }
            lista.setClickable(false);
            flag=1;
        }

        if(chiamante.equals(STORICO)&&(sf.size()==0||sf==null)){
            avv = "Non hai ancora partecipato ad alcuna attività, perché non provi la funzione \"cerca\"?";
            Toast.makeText(ElencoAttivita.this, avv, Toast.LENGTH_SHORT).show();
            flag=1;
            lista.setClickable(false);
        }
    }

    private void bottone(String chiamante,int position,ArrayList<Prenotazione> pf,Integer[] ids,ArrayList<Attivita> sf){
        Intent inte=new Intent(ElencoAttivita.this, DettagliAttivita.class);
        if(chiamante.equals(MODIFICA))
            inte= new Intent(ElencoAttivita.this, DettagliAttivita.class);
        if(chiamante.equals(RICHIESTE))
            inte= new Intent(ElencoAttivita.this,DettaglioRichieste.class);
        if(chiamante.equals(INOLTRATE)){
            inte = new Intent(ElencoAttivita.this, DettagliAttivita.class);
            inte.putExtra("prenotati",pf.get(position).getPartecipanti());
            inte.putExtra("flag",pf.get(position).getFlagConferma());
            inte.putExtra("descrizione",pf.get(position).getCommenti());
            inte.putExtra("idAttivita",pf.get(position).getIdAttivita());
        }
        if(chiamante.equals(STORICO)) {
            inte = new Intent(ElencoAttivita.this, DettagliFeedback.class);
            inte.putExtra("id",globetrotter);
            inte.putExtra("idAttivita",sf.get(position).getIdAttivita());
            inte.putExtra("flag",f.get(position));
        }
        else inte.putExtra("id",ids[position]);

        inte.putExtra("chiamante",chiamante);
        startActivity(inte);
        finish();
    }
}
