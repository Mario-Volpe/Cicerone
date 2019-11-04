package com.example.cicerone.data;

import android.content.Context;
import android.content.SharedPreferences;

public class Utente {
    private static final Utente ourInstance = new Utente();
    Context context;
    SharedPreferences sharedPreferences;

    public Utente() {

    }

    public Utente(Context context, String password, String cognome, String nome, String email, String datanascita, Integer id,String CF,String telefono) {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("userinfo",context.MODE_PRIVATE);
        this.id = id;

        setCF(CF);
        setTelefono(telefono);
        setPassword(password);
        setNome(nome);
        setCognome(cognome);
        setEmail(email);
        setDatanascita(datanascita);
    }

    public Utente(Context context, String password, String cognome, String nome, String email, String datanascita, Integer id) {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("userinfo",context.MODE_PRIVATE);
        this.id = id;

        setPassword(password);
        setNome(nome);
        setCognome(cognome);
        setEmail(email);
        setDatanascita(datanascita);
    }

    public void logout(){
        sharedPreferences.edit().clear().commit();
    }


    public static Utente getInstance() {
        return ourInstance;
    }

    private String password;
    private String cognome;
    private String nome;
    private String email;
    private String datanascita;
    private String telefono;
    private String CF;
    private Integer id;

    public String getEmail() {
        email=sharedPreferences.getString("email","");
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("email",email).commit();
    }

    public String getPassword() {
        password=sharedPreferences.getString("password","");
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        sharedPreferences.edit().putString("password",password).commit();
    }

    public String getCognome() {
        cognome=sharedPreferences.getString("cognome","");
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
        sharedPreferences.edit().putString("cognome",cognome).commit();
    }

    public String getNome() {
        nome=sharedPreferences.getString("nome","");
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        sharedPreferences.edit().putString("nome",nome).commit();
    }

    public String getDatanascita() {
        datanascita=sharedPreferences.getString("datanascita","");
        return datanascita;
    }

    public void setDatanascita(String datanascita) {
        this.datanascita = datanascita;
        sharedPreferences.edit().putString("datanascita",datanascita).commit();
    }

    public String getTelefono() {
        telefono=sharedPreferences.getString("telefono","");
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
        sharedPreferences.edit().putString("telefono",telefono).commit();
    }

    public String getCF() {
        CF=sharedPreferences.getString("CF","");
        return CF;
    }

    public void setCF(String CF) {
        this.CF = CF;
        sharedPreferences.edit().putString("CF",CF).commit();
    }

    public Integer getId() {
        id=sharedPreferences.getInt("id",0);
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
        sharedPreferences.edit().putInt("id",id).commit();

    }
}