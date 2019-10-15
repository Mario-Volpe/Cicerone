package com.example.cicerone.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Prenotazione implements Parcelable {
    private String commenti;
    private Integer idAttivita,partecipanti,flagConferma,id;

    public Prenotazione(Integer id,Integer idAttivita,Integer partecipanti){
        setIdAttivita(idAttivita);
        setId(id);
        setPartecipanti(partecipanti);
        setCommenti("");
        setFlagConferma(0);
    }

    public Prenotazione(Integer id,Integer idAttivita,Integer partecipanti,String commenti,Integer flagConferma){
        setIdAttivita(idAttivita);
        setId(id);
        setPartecipanti(partecipanti);
        setCommenti(commenti);
        setFlagConferma(flagConferma);
    }

    protected Prenotazione(Parcel in) {
        if(in.readByte()==0)
            id = null;
        else id = in.readInt();
        commenti = in.readString();
        if (in.readByte() == 0) {
            idAttivita = null;
        } else {
            idAttivita = in.readInt();
        }
        if (in.readByte() == 0) {
            partecipanti = null;
        } else {
            partecipanti = in.readInt();
        }
        if (in.readByte() == 0) {
            flagConferma = null;
        } else {
            flagConferma = in.readInt();
        }
    }

    public static final Creator<Prenotazione> CREATOR = new Creator<Prenotazione>() {
        @Override
        public Prenotazione createFromParcel(Parcel in) {
            return new Prenotazione(in);
        }

        @Override
        public Prenotazione[] newArray(int size) {
            return new Prenotazione[size];
        }
    };

    public String getCommenti() {
        return commenti;
    }

    public void setCommenti(String commenti) {
        this.commenti = commenti;
    }

    public Integer getIdAttivita() {
        return idAttivita;
    }

    public void setIdAttivita(Integer idAttivita) {
        this.idAttivita = idAttivita;
    }

    public Integer getPartecipanti() {
        return partecipanti;
    }

    public void setPartecipanti(Integer partecipanti) {
        this.partecipanti = partecipanti;
    }

    public Integer getFlagConferma() {
        return flagConferma;
    }

    public void setFlagConferma(Integer flagConferma) {
        this.flagConferma = flagConferma;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(commenti);
        if (idAttivita == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idAttivita);
        }
        if (partecipanti == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(partecipanti);
        }
        if (flagConferma == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(flagConferma);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
