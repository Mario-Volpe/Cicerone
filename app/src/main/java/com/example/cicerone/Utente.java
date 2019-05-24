package com.example.cicerone;

import android.provider.ContactsContract;

import java.util.UUID;

public class Utente {
    private static final Utente ourInstance = new Utente();

    public static Utente getInstance() {
        return ourInstance;
    }

    private UUID id = UUID.randomUUID();

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    private Utente() {

    }
}