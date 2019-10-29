package com.example.cicerone.data;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public abstract class DBhelper {
    private static final String FROM=" FROM ";

    private static final String UTENTE_TABLE="Utenti";
    private static final String U_COL_ID="IdUtente";
    private static final String U_COL_NOME="NomeUtente";
    private static final String U_COL_COGNOME="CognomeUtente";
    private static final String U_COL_EMAIL="EmailUtente";
    private static final String U_COL_DATA_NASCITA="DataNascitaUtente";
    private static final String U_COL_SEC="PasswordUtente";
    private static final String U_COL_CF="CFUtente";
    private static final String U_COL_TELEFONO="TelefonoUtente";

    private static final String ATTIVITA_TABLE="Attivita";
    private static final String A_COL_CICERONE="FkUtentiAttivita";
    private static final String A_COL_ID="IdAttivita";
    private static final String A_COL_CITTA="CittaAttivita";
    private static final String A_COL_ITINERARIO="DescrizioneAttivita";
    private static final String A_COL_ORA="OraAttivita";
    private static final String A_COL_LINGUA="FkLingueAttivita";
    private static final String A_COL_PARTECIPANTI="MaxPartecipantiAttivita";
    private static final String A_COL_DATA="DataAttivita";

    private static final String PRENOTAZIONE_TABLE="Prenotazioni";
    private static final String P_COL_ATTIVITA="FkAttivitaPrenotazione";
    private static final String P_COL_GLOBETROTTER="FkUtentiPrenotazione";
    private static final String P_COL_PARTECIPANTI="PartecipantiPrenotazione";
    private static final String P_COL_COMMENTI="CommentiPrenotazione";
    private static final String P_COL_CONFERMA="ConfermaPrenotazione";

    private static final String FEEDBACK_TABLE="Feedback";
    private static final String F_COL_ATTIVITA="FkAttivitaFeedback";
    private static final String F_COL_GLOBETROTTER="FkUtentiFeedback";
    private static final String F_COL_VOTO="VotoFeedback";
    private static final String F_COL_COMMENTO="CommentoFeedback";

    private static final String LINGUA_TABLE="Lingue";
    private static final String L_COL_ID="IdLingua";
    private static final String L_COL_NOME="NomeLingua";

    private static void conn(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://18.232.247.191/conn.php");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            Log.e("log_tag", "Success in http connection ");
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());

        }
    }


    private static JSONArray doQuery(String query){
        conn();
        String result="";
        JSONArray jArray = new JSONArray();
        StringBuilder sb;
        InputStream is = null;
        ArrayList<NameValuePair> querySend = new ArrayList<>();
        querySend.add(new BasicNameValuePair("querySend",query));

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://18.232.247.191/query.php");
            httppost.setEntity(new UrlEncodedFormEntity(querySend));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("log_tag", "Success in http connection ");
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());

        }

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "latin1"), 8);

            sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            Log.e("log_tag", "No converting errors");
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        if(result.isEmpty())
            Log.e("log_tag","è vuoto.");

        try{
            jArray = new JSONArray(result);
        } catch(JSONException e1){
            Log.e("Errore:", "Conversione fallita");
        }
        return jArray;
    }

    public static boolean checkData(int anno, int mese, int giorno, String chiamante){
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
            if(chiamante.equals("cerca")) {
                if (mese == m && giorno < g)
                    res = false;
            } else if (mese == m && giorno <= g)
                res = false;

        }
        return res;
    }

    private final static int C = 48;

    public static Integer[] parseData(String data){
        char[] c = data.toCharArray();
        int giorno=c[0]-C;
        int mese=0;
        int anno=0;
        int i=1;

        if(c[i]!='/') {
            giorno = giorno * 10 + (c[i] - C);
            i++;
        }

        i++;
        mese=c[i]-C;
        i++;

        if(c[i]!='/'){
            mese = mese * 10 + (c[i] - C);
            i++;
        }
        i++;

        for(int j=3;j>=0;j--){
            anno+=(c[i]-C)*Math.pow(10,j);
            i++;
        }


        Integer[] res = new Integer[3];
        res[0]=giorno;
        res[1]=mese;
        res[2]=anno;

        return res;
    }

    public static long inserisciUtente(Utente u)
    {
        long res=0;
        String query="INSERT INTO "+UTENTE_TABLE+" ("+U_COL_EMAIL+","+U_COL_NOME+","+U_COL_COGNOME+","+U_COL_SEC+","+U_COL_DATA_NASCITA+","+U_COL_TELEFONO+","+U_COL_CF+")"+
                " VALUES ('"+u.getEmail()+"','"+u.getNome()+"','"+u.getCognome()+"','"+u.getPassword()+"','"+u.getDatanascita()+"',+'"+u.getTelefono()+"','"+u.getCF()+"')";

        doQuery(query);
        return res;
    }

    public static void upgradeUtente(Integer id, String CF, String telefono){
        String query = "UPDATE "+UTENTE_TABLE+" SET "+U_COL_CF+" = '"+CF+"', "+ U_COL_TELEFONO+" = '"+telefono+"'"+
                " WHERE "+U_COL_ID+" = '"+id+"'";

        doQuery(query);
    }

    public static long inserisciAttivita(Attivita a)
    {
        long res=0;
        String query="INSERT INTO "+ATTIVITA_TABLE+" ("+A_COL_CICERONE+","+A_COL_CITTA+","+A_COL_DATA+","+A_COL_ITINERARIO+","+A_COL_LINGUA+","+A_COL_ORA+","+A_COL_PARTECIPANTI+")"+
                " VALUES ('"+a.getCicerone()+"','"+a.getCitta()+"','"+a.getData()+"','"+a.getDescrizione()+"','"+a.getLingua()+"','"+a.getOra()+"','"+a.getMaxPartecipanti()+"')";

        doQuery(query);
        return res;
    }

    /**
     * Metodo che dato un utente cerca la corrispettiva password
     * @param utente dati del registrante
     * @return "" se la password non viene trovata, altrimenti password
     */
    public static String searchPassword(Utente utente)
    {
        String stringaFinale="";
        String query = "select "+U_COL_SEC+FROM+UTENTE_TABLE+" WHERE "+U_COL_EMAIL+"='"+utente.getEmail()+"'";

        JSONArray jArray = doQuery(query);
        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                stringaFinale = stringaFinale + json_data.getString( U_COL_SEC);
            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca psw:","Non trovata");
        }

        return stringaFinale;
    }

    /**
     * Metodo che dato un utente lo cerca nel db
     * @param utente da cercare
     * @return True se utente esiste, False altrimenti
     */
    public static boolean isSignedUp(Utente utente)
    {
        String stringaFinale="";
        String query = "select "+ U_COL_EMAIL+FROM+UTENTE_TABLE+" WHERE "+U_COL_EMAIL+"='"+utente.getEmail()+"'";
        boolean isIn = false;

        JSONArray jArray = doQuery(query);
        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                stringaFinale = "" + stringaFinale + "" + json_data.getString( U_COL_EMAIL);
            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca utente:","Non trovato");
        }

        if (!stringaFinale.equals(""))
            isIn=true;

        return isIn;
    }

    public static Utente getInfoUtentebyID(Context context, Integer id)
    {
        String query = "select "+ U_COL_NOME+","+ U_COL_COGNOME+","+U_COL_EMAIL+","+ U_COL_SEC+","+ U_COL_DATA_NASCITA+","+U_COL_CF+","+U_COL_TELEFONO+
                FROM +UTENTE_TABLE+" WHERE "+U_COL_ID+" = '"+id+"'";

        JSONArray jArray = doQuery(query);
        Utente utente = null;
        String nome,cognome,email,CF,password,datanascita,telefono;

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                nome = json_data.getString( U_COL_NOME);
                cognome = json_data.getString( U_COL_COGNOME);
                email = json_data.getString(U_COL_EMAIL);
                CF = json_data.getString( U_COL_CF);
                password = json_data.getString( U_COL_SEC);
                datanascita = json_data.getString( U_COL_DATA_NASCITA);
                telefono = json_data.getString( U_COL_TELEFONO);

                utente = new Utente(context,password,cognome,nome,email,datanascita,id);

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca utente:","Non trovato");
        }

        return utente;
    }

    public static Utente getInfoUtente(Utente utente) {

        String query = "select "+U_COL_ID+","+ U_COL_NOME+","+ U_COL_COGNOME+","+ U_COL_SEC+","+ U_COL_DATA_NASCITA+","+U_COL_CF+","+U_COL_TELEFONO+
                    FROM +UTENTE_TABLE+" WHERE "+U_COL_EMAIL+" = '"+utente.getEmail()+"'";

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                utente.setId(json_data.getInt( U_COL_ID));
                utente.setNome(json_data.getString( U_COL_NOME));
                utente.setCognome(json_data.getString( U_COL_COGNOME));
                utente.setCF(json_data.getString( U_COL_CF));
                utente.setPassword(json_data.getString( U_COL_SEC));
                utente.setDatanascita(json_data.getString( U_COL_DATA_NASCITA));
                utente.setTelefono(json_data.getString( U_COL_TELEFONO));

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca utente:","Non trovato");
        }

        return utente;
    }

    public static ArrayList<Attivita> getInfoAttivita(Attivita a, Integer id) {

        String query = "select "+A_COL_ID+","+A_COL_DATA+","+ A_COL_CITTA+","+A_COL_LINGUA+","+ A_COL_ITINERARIO+","+A_COL_PARTECIPANTI+","+A_COL_CICERONE+","+A_COL_ORA+
                 FROM +ATTIVITA_TABLE+" WHERE "+A_COL_CITTA+" = '"+a.getCitta()+
                "' AND "+A_COL_PARTECIPANTI+" >= '"+a.getMaxPartecipanti()+
                "' AND "+A_COL_CICERONE+" != '"+id+"' AND "+A_COL_LINGUA+" = '"+a.getLingua()+"'";

        ArrayList<Attivita> s = new ArrayList<>();

        String data, descrizioneItinerario,citta,ora;
        Integer maxPartecipanti, idAttivita,lingua,cicerone;

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);
                boolean flag = true;

                cicerone = json_data.getInt(A_COL_CICERONE);
                idAttivita = json_data.getInt(A_COL_ID);
                data = json_data.getString(A_COL_DATA);
                descrizioneItinerario = json_data.getString(A_COL_ITINERARIO);
                lingua = json_data.getInt(A_COL_LINGUA);
                citta = json_data.getString(A_COL_CITTA);
                maxPartecipanti = json_data.getInt(A_COL_PARTECIPANTI);
                ora = json_data.getString(A_COL_ORA);

                ArrayList<Prenotazione> p = getAllPrenotazioni(idAttivita, "cerca");

                if (p == null || p.size() == 0) {
                    Integer gma[] = parseData(data);

                    if (checkData(gma[2], gma[1], gma[0], "cerca")) {
                        Attivita c = new Attivita(idAttivita, cicerone, data, descrizioneItinerario, lingua, citta, maxPartecipanti, Time.valueOf(ora));
                        s.add(c);
                        Log.e("pnull", "aggiunta attività con id:" + idAttivita);
                    }
                } else {
                    for (Prenotazione p2 : p)
                        if (p2.getId() == id)
                            flag = false;

                    if(flag) {
                        Integer gma[] = parseData(data);
                        if (checkData(gma[2], gma[1], gma[0], "cerca")) {
                            Attivita c = new Attivita(idAttivita, cicerone, data, descrizioneItinerario, lingua, citta, maxPartecipanti, Time.valueOf(ora));
                            s.add(c);
                            Log.e("pnotnull", "aggiunta attività con id:" + idAttivita);
                        }
                    }

                }
            }


        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca prenotazioni:","Non trovate");
        }
        return  s;
    }

    public static Attivita getAttivita(Integer id) {
        String query = "select "+A_COL_ID+","+A_COL_DATA+","+ A_COL_CITTA+","+A_COL_LINGUA+","+ A_COL_ITINERARIO+","+A_COL_PARTECIPANTI+","+A_COL_CICERONE+","+A_COL_ORA+
                  FROM  + ATTIVITA_TABLE + " WHERE "+A_COL_ID+" = '"+id+"'";

        ArrayList<Attivita> s = attivitaSearcher(query);

        return s.get(0);
    }

    public static ArrayList<Attivita> getAllAttivita(Integer id) {
        String query = "select "+A_COL_ID+","+A_COL_DATA+","+ A_COL_CITTA+","+A_COL_LINGUA+","+ A_COL_ITINERARIO+","+A_COL_PARTECIPANTI+","+A_COL_CICERONE+","+A_COL_ORA+
                 FROM +ATTIVITA_TABLE+ " WHERE "+A_COL_CICERONE+" = '"+id+"'";
        return attivitaSearcher(query);
    }

    public static ArrayList<Lingua> getAllLingue() {
        String query = "select "+L_COL_ID+","+L_COL_NOME+FROM +LINGUA_TABLE;

        ArrayList<Lingua> s = new ArrayList<>();

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                int idLingua = json_data.getInt(L_COL_ID);
                String nomeLingua = json_data.getString(L_COL_NOME);

                Lingua c = new Lingua(idLingua, nomeLingua);
                s.add(c);

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca prenotazioni:","Non trovate");
        }

        return s;
    }

    private static ArrayList<Attivita> attivitaSearcher(String query) {
        ArrayList<Attivita> s = new ArrayList<>();

        String data, descrizioneItinerario, citta, ora;
        Integer maxPartecipanti, idAttivita, cicerone, lingua;

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                cicerone = json_data.getInt(A_COL_CICERONE);
                idAttivita = json_data.getInt(A_COL_ID);
                data = json_data.getString(A_COL_DATA);
                descrizioneItinerario = json_data.getString(A_COL_ITINERARIO);
                lingua = json_data.getInt(A_COL_LINGUA);
                citta = json_data.getString(A_COL_CITTA);
                maxPartecipanti = json_data.getInt(A_COL_PARTECIPANTI);
                ora = json_data.getString(A_COL_ORA);

                Attivita c = new Attivita(idAttivita, cicerone, data, descrizioneItinerario, lingua, citta, maxPartecipanti, Time.valueOf(ora));
                s.add(c);

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca prenotazioni:","Non trovate");
        }

        return  s;
    }

    public static int rimuoviAttivita(Integer id) {

        int flag=0;

        String query = "DELETE"+FROM+ATTIVITA_TABLE+" WHERE "+A_COL_ID+" = '"+id+"'";

        JSONArray jArray = doQuery(query);

        try {
            if(jArray.getBoolean(0))
                flag=1;
        } catch (JSONException|NullPointerException e){
            e.printStackTrace();
        }

        return flag;
    }

    public static long richiestaPartecipazione(int partecipanti, int id, Integer idUtente){
        long res=0;
        String query="INSERT INTO "+PRENOTAZIONE_TABLE+" ("+P_COL_GLOBETROTTER+","+P_COL_ATTIVITA+","+P_COL_PARTECIPANTI+","+P_COL_COMMENTI+","+P_COL_CONFERMA+")"+
                " VALUES ('"+idUtente+"','"+id+"','"+partecipanti+"','',0)";
        doQuery(query);
        return res;
    }

    public static ArrayList<Prenotazione> getAllPrenotazioni(Integer id, String chiamante){
        ArrayList<Prenotazione> p = new ArrayList<>();

        String query;
        String sel="select "+P_COL_GLOBETROTTER+","+ P_COL_ATTIVITA+","+ P_COL_PARTECIPANTI+","+ P_COL_COMMENTI+","+ P_COL_CONFERMA;
        String where=" WHERE "+P_COL_ATTIVITA+" = ";
        if(chiamante.equals("modifica"))
            query = sel + FROM +PRENOTAZIONE_TABLE+ where+"'"+id+"'";
        else
            query = sel + FROM +PRENOTAZIONE_TABLE+ where+"'"+id+"' AND "+P_COL_CONFERMA+" = '1'";

        if(chiamante.equals("richieste"))
            query = sel + FROM +PRENOTAZIONE_TABLE+ where+"'"+id+"' AND "+P_COL_CONFERMA+" = '0'";

        Integer nPartecipanti,idAttivita,Globetrotter;

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                Globetrotter = json_data.getInt(P_COL_GLOBETROTTER);
                idAttivita = json_data.getInt(P_COL_ATTIVITA);
                nPartecipanti = json_data.getInt(P_COL_PARTECIPANTI);

                Log.e("getprenotaz:","idAtt:"+idAttivita+" nPartec:"+nPartecipanti);

                Prenotazione c = new Prenotazione(Globetrotter,idAttivita,nPartecipanti);
                p.add(c);

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca prenotazioni:","Non trovate");
        }

        return p;
    }

    public static ArrayList<Prenotazione> getAllPrenotazioniUtente(Integer id){
        ArrayList<Prenotazione> p = new ArrayList<>();

        String query = "select *"+FROM +PRENOTAZIONE_TABLE+ " WHERE "+P_COL_GLOBETROTTER +" = '"+id+"'";

        String commenti;
        Integer nPartecipanti,Globetrotter,idAttivita,conferma;
        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                Globetrotter = json_data.getInt(P_COL_GLOBETROTTER);
                idAttivita = json_data.getInt(P_COL_ATTIVITA);
                nPartecipanti = json_data.getInt(P_COL_PARTECIPANTI);
                commenti = json_data.getString(P_COL_COMMENTI);
                conferma =json_data.getInt(P_COL_CONFERMA);

                Log.e("prenotazione"," n"+i+" idA:"+idAttivita+" np:"+nPartecipanti+" comm:"+commenti+" flagc:"+conferma);
                Prenotazione c = new Prenotazione(Globetrotter,idAttivita,nPartecipanti,commenti,conferma);
                p.add(c);

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca prenotazioni:","Non trovate");
        }

        return p;
    }

    public static int updatePrenotazione(Prenotazione p){
        int flag=0;

        String query = "UPDATE "+PRENOTAZIONE_TABLE+" SET "+P_COL_COMMENTI+"='"+p.getCommenti()+"',"+P_COL_CONFERMA+"='"+p.getFlagConferma()+
                "' WHERE "+P_COL_ATTIVITA+" = '"+p.getIdAttivita()+"' AND "+P_COL_GLOBETROTTER+" = '"+p.getId()+"'";

        doQuery(query);

        return flag;
    }

    public static int rimuoviPrenotazione(Integer id) {
        int flag=0;

        String query = "DELETE"+FROM+PRENOTAZIONE_TABLE+" WHERE "+P_COL_ATTIVITA+" = '"+id+"'";

        doQuery(query);

        return flag;
    }

    public static long inserisciFeedback(Feedback f){
        long res=0;
        String query="INSERT INTO "+FEEDBACK_TABLE+" ("+F_COL_GLOBETROTTER+","+F_COL_ATTIVITA+","+F_COL_VOTO+","+F_COL_COMMENTO+")"+
                " VALUES ('"+f.getGlobetrotter()+"','"+f.getIdAttivita()+"','"+f.getVoto()+"','"+f.getCommento()+"')";
        doQuery(query);
        return res;
    }

    public static Feedback getFeedback(Integer idAttivita, Integer id) {
        String query = "select "+ F_COL_GLOBETROTTER+","+ F_COL_ATTIVITA+","+ F_COL_VOTO+","+ F_COL_COMMENTO +
                FROM +FEEDBACK_TABLE+ " WHERE "+F_COL_GLOBETROTTER +"= '"+id+"' AND "+F_COL_ATTIVITA+" = '"+idAttivita+"'";

        ArrayList<Feedback> f = feedbackSearcher(query);

        if(f==null||f.size()==0)
            return null;
        else return f.get(0);
    }

    public static ArrayList<Feedback> getAllFeedback(Integer idAttivita){
        String query = "select "+F_COL_GLOBETROTTER+","+ F_COL_ATTIVITA+", "+F_COL_VOTO+","+F_COL_COMMENTO +
                FROM +FEEDBACK_TABLE+ " WHERE "+F_COL_ATTIVITA+" = '"+idAttivita+"'";

        return feedbackSearcher(query);
    }

    private static ArrayList<Feedback> feedbackSearcher(String query){
        Feedback f;
        ArrayList<Feedback> a = new ArrayList<>();
        String commento;
        Integer voto,idAttivita,id;

        JSONArray jArray = doQuery(query);

        try {
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = (JSONObject) jArray.get(i);

                id = json_data.getInt(F_COL_GLOBETROTTER);
                idAttivita = json_data.getInt(F_COL_ATTIVITA);
                voto = json_data.getInt(F_COL_VOTO);
                commento = json_data.getString(F_COL_COMMENTO);

                f = new Feedback(id,idAttivita,voto,commento);
                a.add(f);

            }
        } catch (JSONException|NullPointerException e){
            Log.e("Ricerca Feedback:","Non trovato");
        }

        return a;
    }
}