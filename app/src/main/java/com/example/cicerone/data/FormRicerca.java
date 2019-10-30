package com.example.cicerone.data;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cicerone.R;

import java.sql.Time;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;

public class FormRicerca extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener setData ;
    private TimePickerDialog.OnTimeSetListener setOra;
    private int anno=0;
    private int mese=0;
    private int giorno=0;
    private int ora=0;
    private int minuto=0;
    private String linguaStr="";
    private String hour ="";
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_ricerca);

        Button cerca = findViewById(R.id.invia);
        final AutoCompleteTextView citta = findViewById(R.id.cittain);
        final TextView mostraData = findViewById(R.id.datain);
        final TextView mostraOra = findViewById(R.id.orain);
        final EditText partecipanti = findViewById(R.id.partecipantiin);
        final Spinner lingua = findViewById(R.id.linguain);

        String[] countries = getResources().getStringArray(R.array.citta);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        citta.setAdapter(adapter);

        ArrayList<Lingua> lingue = DBhelper.getAllLingue();
        String[] ls = new String[lingue.size()];
        int i=0;

        for(Lingua l:lingue) {
            ls[i]=l.getNome();
            i++;
        }

        cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cittaStr = citta.getText().toString().trim().toUpperCase();
                String dataStr = mostraData.getText().toString().trim();

                Integer partecipantiInt=0;
                String partecipantiStr = partecipanti.getText().toString().trim();

                if(partecipantiStr.equals("")||dataStr.equals("")||cittaStr.equals("")||hour.equals(""))
                    Toast.makeText(FormRicerca.this, "Tutti i campi sono obbligatori!", Toast.LENGTH_SHORT).show();
                else {
                    partecipantiInt = Integer.parseInt(partecipantiStr);

                if (!DBhelper.checkData(anno,mese,giorno,"cerca")) {
                    Toast.makeText(FormRicerca.this, "La data inserita non è corretta.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(partecipantiInt<=0)
                        Toast.makeText(FormRicerca.this, "Il numero dei partecipanti non è corretto.", Toast.LENGTH_SHORT).show();
                    else {
                        ArrayList<Lingua> l = DBhelper.getAllLingue();

                        int idLingua=0;
                        for(Lingua l2:l){
                            if(l2.getNome().equals(linguaStr))
                                idLingua=l2.getId();
                        }

                        cittaStr = DBhelper.rimuoviAccenti(cittaStr);
                        Attivita a = new Attivita(0, dataStr, "", idLingua, cittaStr, partecipantiInt, Time.valueOf(hour+":00"));
                        Integer id = getIntent().getExtras().getInt("idUtente");
                        ArrayList<Attivita> c = DBhelper.getInfoAttivita(a,id);
                        Intent res = new Intent(FormRicerca.this, Cerca.class);
                        res.putExtra("risultati", c);
                        res.putExtra("npartecipanti",partecipantiInt);
                        res.putExtra("idUtente",id);
                        res.putExtra("chiamante",getIntent().getExtras().getString("chiamante"));
                        startActivity(res);
                        finish();
                    }
                }
                }
            }
        });

        spinnerAdapter=new ArrayAdapter<>(this, R.layout.row);
        spinnerAdapter.addAll(ls);
        lingua.setAdapter(spinnerAdapter);

        lingua.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                TextView txt= arg1.findViewById(R.id.rowtext);
                updateLingua(txt.getText().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            { }
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

        mostraOra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(FormRicerca.this, setOra,
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE),
                        true);
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
                String date = dayOfMonth+"/"+month+"/"+year;
                mostraData.setText(date);
            }
        };

        setOra = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                ora = hourOfDay;
                minuto = minute;
                String orastr=""+hourOfDay,minutostr=""+minute;

                if(minute<10){
                    minutostr="0"+minute;
                }

                if(hourOfDay<10){
                    orastr="0"+hourOfDay;
                }

                hour = ""+orastr+":"+minutostr;
                mostraOra.setText(hour);
            }
        };
    }

    private void updateLingua(String lingua) {
        linguaStr=lingua;
    }
}
