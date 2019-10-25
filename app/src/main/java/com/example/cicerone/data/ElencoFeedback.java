package com.example.cicerone.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cicerone.R;

import java.util.ArrayList;

public class ElencoFeedback extends AppCompatActivity {
    private ArrayList<Feedback> f;
    private ArrayList<Attivita> a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenco_feedback);

        ListView lista;
        lista = findViewById(R.id.listaFeedback);
        Integer cicerone = getIntent().getExtras().getInt("id");
        TextView media;
        media = findViewById(R.id.media);
        double avg;
        f = new ArrayList<>();
        ArrayAdapter<String> adapter;
        EAdapter fadapter;
        String avv;
        avv = "Nessun feedback presente";
        ArrayList<String> array;
        array = new ArrayList<>();
        ArrayList<Integer> voti;
        voti = new ArrayList<>();

        Integer[] ids;

        a = DBhelper.getAllAttivita(cicerone);
        for(Attivita a2:a)
            f.addAll(DBhelper.getAllFeedback(a2.getIdAttivita()));

        if(f.size()==0||f==null){
            media.setVisibility(View.INVISIBLE);
            ids = new Integer[1];
            array.add(avv);
            adapter = new ArrayAdapter<>(
                    ElencoFeedback.this, android.R.layout.simple_list_item_1, array
            );
            lista.setAdapter(adapter);
        }
        else {
            a = new ArrayList<>();
            double somma;
            somma = 0;

            for (Feedback f2 : f){
                somma += f2.getVoto();
                voti.add(f2.getVoto());
                Attivita a2 = DBhelper.getAttivita(f2.getIdAttivita());
                a.add(a2);
            }

            avg = (somma / f.size());
            media.append(" " + avg + "/5");

            fadapter = new EAdapter(this,a,voti,"elencoF");

            lista.setAdapter(fadapter);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent inte = new Intent(ElencoFeedback.this,DettagliFeedback.class);
                    inte.putExtra("id",f.get(position).getGlobetrotter());
                    inte.putExtra("idAttivita",a.get(position).getIdAttivita());
                    inte.putExtra("flag",true);
                    startActivity(inte);
                }
            });
        }



    }
}
