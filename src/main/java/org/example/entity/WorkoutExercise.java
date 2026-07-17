package org.example.entity;

import jakarta.persistence.*;
@Entity
public class WorkoutExercise {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private int sets;
        private int repetitions;
        private double weight;
        private int resttime;

        @ManyToOne
        @JoinColumn(name = "workout_id")
        private Workout workout;

        @ManyToOne
        @JoinColumn(name = "exercise_id")
        private Exercise exercise;

}
