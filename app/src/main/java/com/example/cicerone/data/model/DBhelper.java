package com.example.cicerone.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;

import com.example.cicerone.Utente;

public class DBhelper extends SQLiteOpenHelper {
    public static final String DBNAME="Cicerone.db";
    public static final String UTENTE_TABLE="Utenti";
    public static final String U_COL_NOME="Nome";
    public static final String U_COL_COGNOME="Cognome";
    public static final String U_COL_EMAIL="Email";
    public static final String U_COL_DATA_NASCITA="Data_nascita";
    public static final String U_COL_PASSWORD="Password";

    public static final String ATTIVITA_TABLE="Attivita";
    public static final String A_COL_ATTIVITA="ID_Attivita";
    public static final String A_COL_CICERONE="ID_Cicerone";
    public static final String A_COL_ITINERARIO="Descrizione_itenerario";
    public static final String A_COL_LINGUA="Lingua";
    public static final String A_COL_PARTECIPANTI="Max_partecipanti";
    public static final String A_COL_DATA="Data";

    public static final String PRENOTAZIONE_TABLE="Prenotazione";
    public static final String P_COL_ATTIVITA="ID_ACTIVITY";
    public static final String P_COL_GLOBETROTTER="ID_Globetrotter";
    public static final String P_COL_PARTECIPANTI="Numero_partecipanti";



    public DBhelper(Context context ) {
        super(context, DBNAME, null, 2);

        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTableUtente = "CREATE TABLE " +UTENTE_TABLE+"(" +
                "EMAIL TEXT PRIMARY KEY," +
                "NOME TEXT," +
                "COGNOME TEXT," +
                "PASSWORD TEXT," +
                "DATA_NASCITA TEXT)";
        db.execSQL(createTableUtente);

        String createTableAttivita = "CREATE TABLE " +ATTIVITA_TABLE+"(" +
                "ID_ATTIVITA INTEGER PRIMARY KEY AUTOINCREMENT," +
                "DATA DATE," +
                "LINGUA TEXT," +
                "MAX_PARTECIPANTI INTEGER," +
                "ID_CICERONE INTEGER," +
                "FOREIGN KEY (ID_CICERONE) REFERENCES Utenti(EMAIL)" +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE "+
                ")";
        db.execSQL(createTableAttivita);

        String createTablePrenotazione = "CREATE TABLE " +PRENOTAZIONE_TABLE+"(" +
                "GLOBETROTTER INTEGER REFERENCES Utenti( EMAIL)," +
                "ID_ACTIVITY INTEGER REFERENCES Attivita(ID_ATTIVTA)," +
                "NUMERO_PARTECIPANTI INTEGER," +
                "PRIMARY KEY ( GLOBETROTTER, ID_ACTIVITY) " +
                ")";
        db.execSQL(createTablePrenotazione);

    }

    public long inserisciUtente( Utente u )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put( U_COL_EMAIL, u.getEmail() );
        values.put( U_COL_NOME, u.getNome() );
        values.put( U_COL_COGNOME, u.getCognome());
        values.put( U_COL_PASSWORD, u.getPassword());
        values.put( U_COL_DATA_NASCITA, u.getDatanascita());

        long res = db.insert( UTENTE_TABLE, null, values );
        db.close();

        return res;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+ UTENTE_TABLE );
        db.execSQL("DROP TABLE IF EXISTS "+ ATTIVITA_TABLE );
        db.execSQL("DROP TABLE IF EXISTS "+ PRENOTAZIONE_TABLE );

        onCreate( db );
    }

    /**
     * Metodo che dato un utente cerca la corrispettiva password
     * @param utente dati del registrante
     * @return "" se la password non viene trovata, altrimenti password
     */
    public String searchPassword (Utente utente)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select EMAIL, PASSWORD FROM "+UTENTE_TABLE;

        Cursor cursor = db.rawQuery(query, null);
        String a,b;
        b = ""; //stringa vuota indica password non trovata

        if (cursor.moveToFirst())
        {
            do {

                a = cursor.getString(0); //prende l'utente

                if (a.equals(utente.getEmail())) //email corrispondente trovata, assegno la password corrispondente
                {
                    b = cursor.getString(1);
                    break;
                }

            }while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return b;
    }



    /**
     * Metodo che dato un utente lo cerca nel db
     * @param utente da cercare
     * @return True se utente esiste, False altrimenti
     */
    public boolean isSignedUp (Utente utente)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select EMAIL FROM "+UTENTE_TABLE;

        Cursor cursor = db.rawQuery(query, null);
        String email;
        boolean isIn = false;

        if (cursor.moveToFirst())
        {
            do {
                email = cursor.getString(0);

                if (email.equals(utente.getEmail()))
                {
                    isIn = true;
                    break;
                }
            }while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return isIn;
    }

    public Utente getInfoUtente( Utente utente )
    {
        Utente infoUtente = new Utente();

        String email = null;
        String password = null;
        String nome = null;
        String cognome = null;
        String data = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select EMAIL, NOME, COGNOME, PASSWORD, DATA_NASCITA" +
                    " FROM "+UTENTE_TABLE;


            Cursor cursor = db.rawQuery(query, null);

            boolean isIn = false;

            if (cursor.moveToFirst())
            {
                do {
                    email = cursor.getString(0);
                    nome = cursor.getString( 1 );
                    cognome = cursor.getString( 2 );
                    password = cursor.getString( 3 );
                    data = cursor.getString( 4 );

                    if ( email.equals(utente.getEmail()) &&
                            nome.equals(utente.getNome()) &&
                            cognome.equals(utente.getCognome()) &&
                            password.equals(utente.getPassword()) &&
                            data.equals(utente.getDatanascita()) )
                    {
                        isIn = true;
                        break;
                    }

                }while (cursor.moveToNext());


            db.close();
            cursor.close();
        }

        infoUtente.setNome(nome);
        infoUtente.setPassword(password);
        infoUtente.setCognome(cognome);
        infoUtente.setEmail(email);
        infoUtente.setDatanascita(data);

        return infoUtente;
    }
}