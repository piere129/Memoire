package com.example.pieter.memoire.Models;

public class Theme {

    private String name;

    public Theme(String name)
    {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
