package org.example.entity;

import jakarta.persistence.*;

@Entity
public class Rezept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int kalorien;
    private double protein;
    private double fett;
    private double kohlenhydrate;
    private String ziel;

    @Column(length = 2000)
    private String rezeptText;

    public Rezept() {
    }

    public Rezept(int id, String name, int kalorien, double protein, double fett, double kohlenhydrate,
                  String ziel, String rezeptText) {
        this.id = id;
        this.name = name;
        this.kalorien = kalorien;
        this.protein = protein;
        this.fett = fett;
        this.kohlenhydrate = kohlenhydrate;
        this.ziel = ziel;
        this.rezeptText = rezeptText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKalorien() {
        return kalorien;
    }

    public void setKalorien(int kalorien) {
        this.kalorien = kalorien;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFett() {
        return fett;
    }

    public void setFett(double fett) {
        this.fett = fett;
    }

    public double getKohlenhydrate() {
        return kohlenhydrate;
    }

    public void setKohlenhydrate(double kohlenhydrate) {
        this.kohlenhydrate = kohlenhydrate;
    }

    public String getZiel() {
        return ziel;
    }

    public void setZiel(String ziel) {
        this.ziel = ziel;
    }

    public String getRezeptText() {
        return rezeptText;
    }

    public void setRezeptText(String rezeptText) {
        this.rezeptText = rezeptText;
    }
}
