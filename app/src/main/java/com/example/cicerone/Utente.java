package com.example.cicerone;

import android.provider.ContactsContract;

public class Utente {
    private static final Utente ourInstance = new Utente();

    public Utente() {
    }

    public Utente(String password, String cognome, String nome, ContactsContract.CommonDataKinds.Email email) {
        this.password = password;
        this.cognome = cognome;
        this.nome = nome;
        this.email = email;
    }


    public static Utente getInstance() {
        return ourInstance;
    }

    private String password;
    private String cognome;
    private String nome;
    private ContactsContract.CommonDataKinds.Email email;

    public ContactsContract.CommonDataKinds.Email getEmail() {
        return email;
    }

    public void setEmail(ContactsContract.CommonDataKinds.Email email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}