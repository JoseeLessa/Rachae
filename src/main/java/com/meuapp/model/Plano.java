package com.meuapp.model;

import java.time.LocalDate;

public class Plano {

    private int id;
    private String name;
    private int screens;
    private double value;
    private LocalDate payDue;
    private LocalDate creationDate;
    private LocalDate updatedAt;

    /** Construtor completo (usado quando o registro já existe no banco, com ID). */
    public Plano(int id, String name, int screens, double value, LocalDate payDue) {
        this.id = id;
        this.name = name;
        this.screens = screens;
        this.value = value;
        this.payDue = payDue;
        this.creationDate = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    // Construtor usado no SELECT
    public Plano(int id, String name, int screens, double value, LocalDate payDue, LocalDate creationDate, LocalDate updatedAt) {
        this.id = id;
        this.name = name;
        this.screens = screens;
        this.value = value;
        this.payDue = payDue;
        this.creationDate = creationDate;
        this.updatedAt = updatedAt;
    }

    /** Construtor sem ID (usado para criar um novo registro, antes de inserir). */
    public Plano(String name, int screens, double value, LocalDate payDue) {
        this.name = name;
        this.screens = screens;
        this.value = value;
        this.payDue = payDue;
        this.creationDate = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScreens() {
        return this.screens;
    }

    public void getScreens(int screens) {
        this.screens = screens;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LocalDate getPayDue(){
        return this.payDue;
    }

    public void setPayDue(LocalDate payDue) {
        this.payDue = payDue;
    }

    public LocalDate getCreationDate(){
        return this.creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getUpdateDate(){
        return this.updatedAt;
    }

    public void setUpdateDate(LocalDate updatedAt){
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return name;
    }
}
