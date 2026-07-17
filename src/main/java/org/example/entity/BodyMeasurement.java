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



}
