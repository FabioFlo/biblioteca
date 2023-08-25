package com.example.biblioteca;

import java.sql.Date;
import java.time.LocalDate;

public class BookSearchModel {

    int id;
    String titolo;
    String autore;
    String disponibile;
    String prezzo;
    String utente;
    Date dataPrestito;
    Date dataFine;




    public BookSearchModel(int id, String titolo, String autore) {
        this.id = id;
        this.titolo = titolo;
        this.autore = autore;
    }

    public BookSearchModel(int id, String disponibile, String titolo, String autore, String prezzo, String utente,Date dataPrestito, Date dataFine) {
        this.id = id;
        this.disponibile = disponibile;
        this.titolo = titolo;
        this.autore = autore;
        this.prezzo  = prezzo;
        this.utente = utente;
        this.dataPrestito = dataPrestito;
        this.dataFine = dataFine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getDisponibile() {
        return disponibile;
    }

    public void setDisponibile(String disponibile) {
        this.disponibile = disponibile;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public Date getDataPrestito() {
        return dataPrestito;
    }

    public void setDataPrestito(Date dataPrestito) {
        this.dataPrestito = dataPrestito;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }
}
