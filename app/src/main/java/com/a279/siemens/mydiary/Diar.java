package com.a279.siemens.mydiary;

import java.sql.Date;

public class Diar {

    public Integer id;
    public String tema;
    public String text;
    public String date;

    public Diar() {}

    public Diar(Integer id, String tema, String text, String date) {
        this.id = id;
        this.tema = tema;
        this.text = text;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
