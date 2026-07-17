package org.example.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private LocalDate dateOfBirth;
    private double height;
    private String gender;

    @OneToMany(mappedBy = "user")
    private List<Workout> workouts;

    @OneToMany(mappedBy = "user")
    private List<MealPlan> mealPlans;

    @OneToMany(mappedBy = "user")
    private List<BodyMeasurement> bodyMeasurements;
}
