package com.example.cicerone.data;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import java.util.ArrayList;
import java.util.Calendar;

public class Creazione extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener setData ;
    private TimePickerDialog.OnTimeSetListener setOra;

    private int anno=0;
    private int mese=0;
    private int giorno=0;
    private int ora=0;
    private int minuto=0;
    private String linguaStr="";
    private String hour = "";
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creazione);

        Button crea = findViewById(R.id.crea);
        final TextView mostraData = findViewById(R.id.datains);
        final TextView mostraOra = findViewById(R.id.orains);
        final EditText partecipanti = findViewById(R.id.partecipantiins);
        final Spinner lingua = findViewById(R.id.linguains);
        final EditText itinerario = findViewById(R.id.itinerarioins);

        // Get a reference to the AutoCompleteTextView in the layout
        final AutoCompleteTextView citta = findViewById(R.id.cittains);
        // Get the string array
        String[] countries = getResources().getStringArray(R.array.citta);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> cAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        citta.setAdapter(cAdapter);

        ArrayList<Lingua> lingue = new DBhelper(this).getAllLingue();
        String[] ls = new String[lingue.size()];
        int i=0;

        for(Lingua l:lingue) {
            ls[i]=l.getNome();
            i++;
        }

        crea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer partecipantiInt = 0;
                String partecipantiStr = partecipanti.getText().toString().trim();
                String dataStr = mostraData.getText().toString().trim();
                String itinerarioStr = itinerario.getText().toString().trim();
                String cittaStr = citta.getText().toString().trim().toUpperCase();

                if (partecipantiStr.equals("") || dataStr.equals("") || itinerarioStr.equals("") || linguaStr.equals("") || cittaStr.equals("") || hour.equals(""))
                    Toast.makeText(Creazione.this, "Tutti i campi sono obbligatori!", Toast.LENGTH_SHORT).show();
                else {
                    partecipantiInt = Integer.parseInt(partecipantiStr);

                    ArrayList<Lingua> l = new DBhelper(Creazione.super.getBaseContext()).getAllLingue();

                    int idLingua = 0;
                    for (Lingua l2 : l) {
                        if (l2.getNome().equals(linguaStr))
                            idLingua = l2.getId();
                    }

                    if (checkInfo(partecipantiInt)) {
                        final Attivita a = new Attivita(getIntent().getExtras().getInt("idUtente"), dataStr, itinerarioStr, idLingua, cittaStr, partecipantiInt, Time.valueOf(hour + ":00"));

                        DBhelper db = new DBhelper(Creazione.super.getBaseContext());
                        if (db.inserisciAttivita(a) != -1)
                            Toast.makeText(Creazione.this, "Creazione riuscita.", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Creazione.this, "Errore nella creazione.", Toast.LENGTH_SHORT).show();
                        finish();
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
                TextView txt=(TextView) arg1.findViewById(R.id.rowtext);
                updateLingua(txt.getText().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            { }
        });

        mostraData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(Creazione.this, setData,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        mostraOra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(Creazione.this, setOra,
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

    private boolean checkInfo(Integer partecipantiInt){

        if (!new DBhelper(this).checkData(anno,mese,giorno,"creazione")) {
            Toast.makeText(Creazione.this, "La data inserita non è corretta.", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            if(partecipantiInt<=0) {
                Toast.makeText(Creazione.this, "Il numero dei partecipanti non è corretto.", Toast.LENGTH_SHORT).show();
                return false;
            }

            else return true;
        }
    }
}
