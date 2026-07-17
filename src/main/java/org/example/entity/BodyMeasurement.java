package org.example.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
public class BodyMeasurement {



        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private LocalDate datum;
        private double weight;
        private double height;
        private double bodyfat;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        public double getBmi() {
            return weight / (height * height);
        }

        public BodyMeasurement(int id, LocalDate datum, double weight, double height, double bodyfat, User user) {
                this.id = id;
                this.datum = datum;
                this.weight = weight;
                this.height = height;
                this.bodyfat = bodyfat;
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

        public double getWeight() {
                return weight;
        }

        public void setWeight(double weight) {
                this.weight = weight;
        }

        public double getHeight() {
                return height;
        }

        public void setHeight(double height) {
                this.height = height;
        }

        public double getBodyfat() {
                return bodyfat;
        }

        public void setBodyfat(double bodyfat) {
                this.bodyfat = bodyfat;
        }

        public User getUser() {
                return user;
        }

        public void setUser(User user) {
                this.user = user;
        }
}
