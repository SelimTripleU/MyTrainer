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

    //kommentar

    public User(int id, String name, LocalDate dateOfBirth, double height, String gender, List<Workout> workouts,
                List<MealPlan> mealPlans, List<BodyMeasurement> bodyMeasurements) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.gender = gender;
        this.workouts = workouts;
        this.mealPlans = mealPlans;
        this.bodyMeasurements = bodyMeasurements;
    }

    public User() {
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    public List<MealPlan> getMealPlans() {
        return mealPlans;
    }

    public void setMealPlans(List<MealPlan> mealPlans) {
        this.mealPlans = mealPlans;
    }

    public List<BodyMeasurement> getBodyMeasurements() {
        return bodyMeasurements;
    }

    public void setBodyMeasurements(List<BodyMeasurement> bodyMeasurements) {
        this.bodyMeasurements = bodyMeasurements;
    }
}
