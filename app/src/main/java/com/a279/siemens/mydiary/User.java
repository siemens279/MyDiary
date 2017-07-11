package com.a279.siemens.mydiary;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String id;
    public String name;
    public String fam;
    public String age;

    public User() {}

    public User(String id, String name, String fam, String age) {
        this.id = id;
        this.name = name;
        this.fam = fam;
        this.age = age;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("fam", fam);
        result.put("age", age);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFam() {
        return fam;
    }

    public void setFam(String fam) {
        this.fam = fam;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
