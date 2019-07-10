package com.example.cicerone;

import android.os.Parcel;
import android.os.Parcelable;


public class Attivita implements Parcelable {
    private String Cicerone;
    private String data, descrizione,lingua,citta;
    private Integer maxPartecipanti, idAttivita;

    public String toStringSearch(){
        return ""+getIdAttivita()+"\b\b"+getCitta()+"\b\b"+getData()+"\b\b"+getMaxPartecipanti();
    }

    public Attivita(String Cicerone, String data, String descrizione, String lingua,
                    String citta, Integer maxPartecipanti){

        setCicerone(Cicerone);
        setData(data);
        setDescrizione(descrizione);
        setLingua(lingua);
        setCitta(citta);
        setMaxPartecipanti(maxPartecipanti);

    }

    public Attivita(Integer id, String Cicerone, String data, String descrizione, String lingua,
                    String citta, Integer maxPartecipanti){

        setIdAttivita(id);
        setCicerone(Cicerone);
        setData(data);
        setDescrizione(descrizione);
        setLingua(lingua);
        setCitta(citta);
        setMaxPartecipanti(maxPartecipanti);

    }

    protected Attivita(Parcel in) {
        Cicerone = in.readString();
        data = in.readString();
        descrizione = in.readString();
        lingua = in.readString();
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

    public String getCicerone() {
        return Cicerone;
    }

    public void setCicerone(String cicerone) {
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

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
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
        dest.writeString(Cicerone);
        dest.writeString(data);
        dest.writeString(descrizione);
        dest.writeString(lingua);
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
}
