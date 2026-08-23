package org.example.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class CalendarEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate datum;
    private boolean trainiert;
    private String muskelgruppe;
    private int kalorien;
    private double fett;
    private double kohlenhydrate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public CalendarEntry() {
    }

    public CalendarEntry(int id, LocalDate datum, boolean trainiert, String muskelgruppe, int kalorien,
                          double fett, double kohlenhydrate, User user) {
        this.id = id;
        this.datum = datum;
        this.trainiert = trainiert;
        this.muskelgruppe = muskelgruppe;
        this.kalorien = kalorien;
        this.fett = fett;
        this.kohlenhydrate = kohlenhydrate;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public boolean isTrainiert() {
        return trainiert;
    }

    public void setTrainiert(boolean trainiert) {
        this.trainiert = trainiert;
    }

    public String getMuskelgruppe() {
        return muskelgruppe;
    }

    public void setMuskelgruppe(String muskelgruppe) {
        this.muskelgruppe = muskelgruppe;
    }

    public int getKalorien() {
        return kalorien;
    }

    public void setKalorien(int kalorien) {
        this.kalorien = kalorien;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
