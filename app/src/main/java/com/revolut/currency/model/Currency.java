package com.revolut.currency.model;

public class Currency {

    private int id;
    private String name;
    private String surname;

    public Currency(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getId() {
        return id;
    }
}
