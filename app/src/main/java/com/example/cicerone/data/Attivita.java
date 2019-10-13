package com.example.cicerone.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Time;


public class Attivita implements Parcelable {
    private String data, descrizione,citta;
    private Integer maxPartecipanti, idAttivita, Cicerone,lingua;
    private Time ora;

    public String toStringSearch(){
        return ""+getIdAttivita()+"\b\b"+getCitta()+"\b\b"+getData()+"\b\b"+getMaxPartecipanti();
    }

    public Attivita(Integer Cicerone, String data, String descrizione, Integer lingua,
                    String citta, Integer maxPartecipanti,Time ora){

        setOra(ora);
        setCicerone(Cicerone);
        setData(data);
        setDescrizione(descrizione);
        setLingua(lingua);
        setCitta(citta);
        setMaxPartecipanti(maxPartecipanti);

    }

    public Attivita(Integer id, Integer Cicerone, String data, String descrizione, Integer lingua,
                    String citta, Integer maxPartecipanti, Time ora){
        this.ora = ora;

        setIdAttivita(id);
        setCicerone(Cicerone);
        setData(data);
        setDescrizione(descrizione);
        setLingua(lingua);
        setCitta(citta);
        setMaxPartecipanti(maxPartecipanti);

    }

    protected Attivita(Parcel in) {
        Cicerone = in.readInt();
        data = in.readString();
        descrizione = in.readString();
        lingua = in.readInt();
        citta = in.readString();
        if (in.readByte() == 0) {
            maxPartecipanti = null;
        } else {
            maxPartecipanti = in.readInt();
        }
        if (in.readByte() == 0) {
            idAttivita = null;
        } else {
            idAttivita = in.readInt();
        }
    }

    public static final Creator<Attivita> CREATOR = new Creator<Attivita>() {
        @Override
        public Attivita createFromParcel(Parcel in) {
            return new Attivita(in);
        }

        @Override
        public Attivita[] newArray(int size) {
            return new Attivita[size];
        }
    };

    public Integer getCicerone() {
        return Cicerone;
    }

    public void setCicerone(Integer cicerone) {
        Cicerone = cicerone;
    }

    public Integer getMaxPartecipanti() {
        return maxPartecipanti;
    }

    public void setMaxPartecipanti(Integer maxPartecipanti) {
        this.maxPartecipanti = maxPartecipanti;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getLingua() {
        return lingua;
    }

    public void setLingua(Integer lingua) {
        this.lingua = lingua;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public Integer getIdAttivita() {
        return idAttivita;
    }

    public void setIdAttivita(Integer idAttivita) {
        this.idAttivita = idAttivita;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Cicerone);
        dest.writeString(data);
        dest.writeString(descrizione);
        dest.writeInt(lingua);
        dest.writeString(citta);
        if (maxPartecipanti == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maxPartecipanti);
        }
        if (idAttivita == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idAttivita);
        }
    }

    public Time getOra() {
        return ora;
    }

    public void setOra(Time ora) {
        this.ora = ora;
    }
}
