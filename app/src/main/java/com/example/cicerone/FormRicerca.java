package com.example.cicerone;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cicerone.data.model.DBhelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FormRicerca extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener setData ;
    private static final String TAG = "FormRicerca";

    private int anno=0;
    private int mese=0;
    private int giorno=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_ricerca);

        Button cerca = findViewById(R.id.invia);
        final EditText citta = findViewById(R.id.cittain);
        final TextView mostraData = findViewById(R.id.datain);
        final EditText partecipanti = findViewById(R.id.partecipantiin);

        cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cittaStr = citta.getText().toString().trim();
                String dataStr = mostraData.getText().toString().trim();

                Integer partecipantiInt=0;
                String partecipantiStr = partecipanti.getText().toString().trim();

                if(partecipantiStr.equals(""))
                    Toast.makeText(FormRicerca.this, "Tutti i campi sono obbligatori!.", Toast.LENGTH_SHORT).show();
                else {
                    partecipantiInt = Integer.parseInt(partecipantiStr);
                }

                if(dataStr.equals("")||cittaStr.equals(""))
                    Toast.makeText(FormRicerca.this, "Tutti i campi sono obbligatori!.", Toast.LENGTH_SHORT).show();

                DBhelper db = new DBhelper(FormRicerca.super.getBaseContext());

                if (!checkData()) {
                    Toast.makeText(FormRicerca.this, "La data inserita non è corretta.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(partecipantiInt<=0)
                        Toast.makeText(FormRicerca.this, "Il numero dei partecipanti non è corretto.", Toast.LENGTH_SHORT).show();
                    else {
                        Attivita a = new Attivita("", dataStr, "", "", cittaStr, partecipantiInt);
                        String id = getIntent().getExtras().getString("id");
                        ArrayList<Attivita> c = db.getInfoAttivita(a,id);
                        Intent res = new Intent(FormRicerca.this, Cerca.class);
                        res.putExtra("risultati", c);
                        res.putExtra("npartecipanti",partecipantiInt);
                        startActivity(res);
                        finish();
                    }
                }
            }
        });

        mostraData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(FormRicerca.this, setData,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        setData = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                anno=year;
                mese=month;
                giorno=dayOfMonth;

                Log.d(TAG,"onDateSet: dd/mm/yyyy: "+ dayOfMonth+"/"+month+"/"+year);

                String date = dayOfMonth+"/"+month+"/"+year;
                mostraData.setText(date);
            }
        };
    }

    private boolean checkData(){
        boolean res = true;
        Date date = new Date();
        int g=date.getDate();
        int m=date.getMonth()+1;
        int a=date.getYear()+1900;

        if(anno<a)
            res=false;
        else if(anno==a){
            if(mese<m)
                res=false;
            else
            if(mese==m&&giorno<g)
                res=false;


        }
        return res;
    }
}