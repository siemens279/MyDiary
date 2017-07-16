package com.a279.siemens.mydiary;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Diar implements Serializable {

    public Integer id;
    public String tema;
    public String text;
    public String date;
    public ArrayList<String> imgArray = new ArrayList<>();

    public Diar() {}

    public Diar(Integer id, String tema, String text, String date, ArrayList<String> array) {
        this.id = id;
        this.tema = tema;
        this.text = text;
        this.date = date;
        this.imgArray = array;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("tema", tema);
        result.put("text", text);
        result.put("date", date);
        result.put("img", imgArray);
        return result;
    }

    public ArrayList<String> getImgArray() {
        return imgArray;
    }

    public void setImgArray(ArrayList<String> imgArray) {
        this.imgArray = imgArray;
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
