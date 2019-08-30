package com.example.cicerone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cicerone.data.model.DBhelper;

import java.util.ArrayList;

public class Cerca extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca);
        ListView lista = findViewById(R.id.risultati);

        ArrayList<String> array = new ArrayList<>();
        final ArrayList<Attivita> s = getIntent().getExtras().getParcelableArrayList("risultati");
        final ArrayList<Integer> r= new ArrayList<>(); //n partecipanti per attività
        final String chiamante = getIntent().getExtras().getString("chiamante");

        final Integer[] ids;
        String avv="";
        int flag=0;
        int j=0;

        if(s.size()==0){
            avv = "Nessuna attività trovata";
            Toast.makeText(Cerca.this, "Nessuna attività trovata", Toast.LENGTH_SHORT).show();
            lista.setClickable(false);
            flag=1;
        }

        if (flag==0) {
            ids = new Integer[s.size()];
            for (Attivita b : s) {
                array.add(b.toStringSearch());
                ids[j] = b.getIdAttivita();
                ArrayList<Prenotazione> p = new DBhelper(Cerca.this).getAllPrenotazioni(ids[j], chiamante);
                int h=0;
                for(Prenotazione p2:p)
                    h+=p2.getPartecipanti();
                r.add(h);
                j++;
            }
        }
        else{
            ids = new Integer[1];
            array.add(avv);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                Cerca.this, android.R.layout.simple_list_item_1, array
        );

        EAdapter fadapter = new EAdapter(this, s, r, chiamante);

        if (flag==1)
            lista.setAdapter(adapter);

        else{
            lista.setAdapter(fadapter);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent inte = new Intent(Cerca.this, DettagliAttivita.class);
                    inte.putExtra("id",ids[position]);
                    inte.putExtra("chiamante",chiamante);
                    inte.putExtra("npartecipanti",getIntent().getExtras().getInt("npartecipanti"));
                    inte.putExtra("email",getIntent().getExtras().getString("email"));
                    inte.putExtra("postidisponibili",s.get(position).getMaxPartecipanti()-r.get(position));
                    startActivity(inte);
                    finishFromChild(Cerca.this);
                }
            });
        }

    }
}
