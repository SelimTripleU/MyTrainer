package org.example.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String muscleGroup;
    private String description;

    @OneToMany(mappedBy = "exercise")
    private List<WorkoutExercise> workoutExercises;
}
