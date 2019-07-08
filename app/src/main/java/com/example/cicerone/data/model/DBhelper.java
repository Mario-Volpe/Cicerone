package com.example.cicerone.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cicerone.Attivita;
import com.example.cicerone.Prenotazione;
import com.example.cicerone.Utente;

import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {
    public static final String DBNAME="Cicerone.db";
    public static final String UTENTE_TABLE="Utenti";
    public static final String U_COL_NOME="Nome";
    public static final String U_COL_COGNOME="Cognome";
    public static final String U_COL_EMAIL="Email";
    public static final String U_COL_DATA_NASCITA="Data_nascita";
    private static final String U_COL_SEC="Password";

    public static final String ATTIVITA_TABLE="Attivita";
    public static final String A_COL_ATTIVITA="ID_Attivita";
    public static final String A_COL_CICERONE="ID_Cicerone";
    public static final String A_COL_CITTA="Citta";
    public static final String A_COL_ITINERARIO="Descrizione_itinerario";
    public static final String A_COL_LINGUA="Lingua";
    public static final String A_COL_PARTECIPANTI="Max_partecipanti";
    public static final String A_COL_DATA="Data";

    public static final String PRENOTAZIONE_TABLE="Prenotazione";
    public static final String P_COL_ATTIVITA="ID_ATTIVITA";
    public static final String P_COL_GLOBETROTTER="Globetrotter";
    public static final String P_COL_PARTECIPANTI="Numero_partecipanti";
    public static final String P_COL_COMMENTI="Commenti";
    public static final String P_COL_CONFERMA="Conferma";


    public DBhelper(Context context ) {
        super(context, DBNAME, null, 11);

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
                "ID_ATTIVITA INTEGER PRIMARY KEY," +
                "DATA TEXT," +
                "CITTA TEXT," +
                "LINGUA TEXT," +
                "DESCRIZIONE_ITINERARIO TEXT, " +
                "MAX_PARTECIPANTI INTEGER," +
                "ID_CICERONE TEXT," +
                "FOREIGN KEY (ID_CICERONE) REFERENCES Utenti(EMAIL)" +
                "ON DELETE CASCADE " +
                "ON UPDATE CASCADE "+
                ")";
        db.execSQL(createTableAttivita);

        String createTablePrenotazione = "CREATE TABLE " +PRENOTAZIONE_TABLE+"(" +
                "GLOBETROTTER TEXT REFERENCES Utenti( EMAIL)," +
                "ID_ATTIVITA INTEGER REFERENCES Attivita(ID_ATTIVITA)," +
                "NUMERO_PARTECIPANTI INTEGER," +
                "COMMENTI TEXT, " +
                "CONFERMA INTEGER," + //0: in attesa, 1:confermata, 2:rifiutata
                "PRIMARY KEY ( GLOBETROTTER, ID_ATTIVITA) " +
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
        values.put(U_COL_SEC, u.getPassword());
        values.put( U_COL_DATA_NASCITA, u.getDatanascita());

        long res = db.insert( UTENTE_TABLE, null, values );
        db.close();

        return res;
    }

    public long inserisciAttivita( Attivita a )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put( A_COL_ATTIVITA, a.getIdAttivita() );
        values.put( A_COL_DATA, a.getData() );
        values.put( A_COL_CITTA, a.getCitta() );
        values.put( A_COL_LINGUA, a.getLingua() );
        values.put( A_COL_ITINERARIO, a.getDescrizioneItinerario() );
        values.put( A_COL_PARTECIPANTI, a.getMaxPartecipanti() );
        values.put( A_COL_CICERONE, a.getCicerone() );

        long res = db.insert( ATTIVITA_TABLE, null, values );
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
        String email=null,password=null,nome = null,cognome=null,data=null;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select EMAIL, NOME, COGNOME, PASSWORD, DATA_NASCITA" +
                    " FROM "+UTENTE_TABLE+" WHERE EMAIL = '"+utente.getEmail()+"'";

        Cursor cursor = db.rawQuery(query,null );

            if (cursor.moveToFirst()) {
                do {
                    email = cursor.getString(0);
                    nome = cursor.getString( 1 );
                    cognome = cursor.getString( 2 );
                    password = cursor.getString( 3 );
                    data = cursor.getString( 4 );

                    if ( email.equals(utente.getEmail()) &&
                            password.equals(utente.getPassword()) ) {
                        break;
                    }

                }while (cursor.moveToNext());

            db.close();
            cursor.close();
        }

        utente.setNome(nome);
        utente.setPassword(password);
        utente.setCognome(cognome);
        utente.setEmail(email);
        utente.setDatanascita(data);

        return utente;
    }

    public ArrayList<Attivita> getInfoAttivita(Attivita a,String email)
    {
        ArrayList<Attivita> s = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select ID_Attivita, Data, Citta, Lingua, Descrizione_itinerario, Max_partecipanti, Id_Cicerone" +
                " FROM "+ATTIVITA_TABLE+ " WHERE Citta = '"+a.getCitta()+
                "' AND DATA = '"+a.getData()+"' AND Max_partecipanti >= "+a.getMaxPartecipanti()+
                " AND Id_Cicerone != '"+email+"'";

        Cursor cursor = db.rawQuery(query,null );

        String Cicerone;
        String data,descrizioneItinerario,lingua,citta;
        Integer maxPartecipanti,idAttivita;

        if (cursor.moveToFirst()) {
            do {
                idAttivita = cursor.getInt( 0 );
                data = cursor.getString( 1 );
                citta = cursor.getString( 2 );
                lingua = cursor.getString( 3 );
                descrizioneItinerario = cursor.getString(4);
                maxPartecipanti = cursor.getInt( 5 );
                Cicerone = cursor.getString( 6 );

                Attivita c = new Attivita (idAttivita,Cicerone,data,descrizioneItinerario,lingua,citta,maxPartecipanti);
                s.add(c);

            }while (cursor.moveToNext());

            db.close();
            cursor.close();
        }
        return s;
    }

    public Attivita getAttivita(Integer id) {
        Attivita a=null;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select ID_Attivita, Data, Citta, Lingua, Descrizione_itinerario, Max_partecipanti, Id_Cicerone" +
                " FROM " + ATTIVITA_TABLE + " WHERE ID_Attivita ="+id;

        Cursor cursor = db.rawQuery(query, null);

        String Cicerone;
        String data, descrizioneItinerario, lingua, citta;
        Integer maxPartecipanti, idAttivita;

        if (cursor.moveToFirst()) {
            do {
                idAttivita = cursor.getInt(0);
                data = cursor.getString(1);
                citta = cursor.getString(2);
                lingua = cursor.getString(3);
                descrizioneItinerario = cursor.getString(4);
                maxPartecipanti = cursor.getInt(5);
                Cicerone = cursor.getString(6);

                a = new Attivita(idAttivita, Cicerone, data, descrizioneItinerario, lingua, citta, maxPartecipanti);

            } while (cursor.moveToNext());

            db.close();
            cursor.close();
        }
        return a;
    }

    public ArrayList<Attivita> getAllAttivita(String email) {
        ArrayList<Attivita> s = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select ID_Attivita, Data, Citta, Lingua, Descrizione_itinerario, Max_partecipanti, Id_Cicerone" +
                " FROM "+ATTIVITA_TABLE+ " WHERE Id_Cicerone = '"+email+"'";

        Cursor cursor = db.rawQuery(query,null );

        String Cicerone;
        String data,descrizioneItinerario,lingua,citta;
        Integer maxPartecipanti,idAttivita;

        if (cursor.moveToFirst()) {
            do {
                idAttivita = cursor.getInt( 0 );
                data = cursor.getString( 1 );
                citta = cursor.getString( 2 );
                lingua = cursor.getString( 3 );
                descrizioneItinerario = cursor.getString(4);
                maxPartecipanti = cursor.getInt( 5 );
                Cicerone = cursor.getString( 6 );

                Attivita c = new Attivita (idAttivita,Cicerone,data,descrizioneItinerario,lingua,citta,maxPartecipanti);
                s.add(c);

            }while (cursor.moveToNext());

            db.close();
            cursor.close();
        }

        return s;
    }

    public int rimuoviAttivita(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();

        int flag = db.delete(ATTIVITA_TABLE,"ID_Attivita=?",new String[]{""+id});

        db.close();
        return flag;
    }

    public long richiestaPartecipazione(int partecipanti,int id,String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(P_COL_GLOBETROTTER, email );
        values.put(P_COL_ATTIVITA, id );
        values.put(P_COL_PARTECIPANTI, partecipanti );
        values.put(P_COL_COMMENTI,"");
        values.put(P_COL_CONFERMA,0);

        long res = db.insert( PRENOTAZIONE_TABLE, null, values );
        db.close();

        return res;
    }

    public ArrayList<Prenotazione> getAllPrenotazioni(Integer id){
        ArrayList<Prenotazione> s = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select GLOBETROTTER, ID_ATTIVITA, NUMERO_PARTECIPANTI, COMMENTI, CONFERMA" +
                " FROM "+PRENOTAZIONE_TABLE+ " WHERE ID_ATTIVITA = "+id;

        Cursor cursor = db.rawQuery(query,null );

        String Globetrotter,commenti;
        Integer nPartecipanti,idAttivita,conferma;

        if (cursor.moveToFirst()) {
            do {
                Globetrotter = cursor.getString(0);
                idAttivita = cursor.getInt( 1 );;
                nPartecipanti = cursor.getInt( 2 );
                commenti = cursor.getString( 3 );
                conferma = cursor.getInt(4);

                Prenotazione c = new Prenotazione(Globetrotter,idAttivita,nPartecipanti);
                s.add(c);

            }while (cursor.moveToNext());

            db.close();
            cursor.close();
        }

        return s;
    }

}