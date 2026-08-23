package org.example.entity;

import jakarta.persistence.*;

@Entity
public class ErnaehrungsZiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int kalorien;
    private double protein;
    private double fett;
    private double kohlenhydrate;
    private String ziel;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ErnaehrungsZiel() {
    }

    public ErnaehrungsZiel(int id, int kalorien, double protein, double fett, double kohlenhydrate,
                            String ziel, User user) {
        this.id = id;
        this.kalorien = kalorien;
        this.protein = protein;
        this.fett = fett;
        this.kohlenhydrate = kohlenhydrate;
        this.ziel = ziel;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
