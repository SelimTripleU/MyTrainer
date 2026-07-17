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

        public WorkoutExercise(int id, int sets, int repetitions, double weight, int resttime, Workout workout, Exercise exercise) {
                this.id = id;
                this.sets = sets;
                this.repetitions = repetitions;
                this.weight = weight;
                this.resttime = resttime;
                this.workout = workout;
                this.exercise = exercise;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public int getSets() {
                return sets;
        }

        public void setSets(int sets) {
                this.sets = sets;
        }

        public int getRepetitions() {
                return repetitions;
        }

        public void setRepetitions(int repetitions) {
                this.repetitions = repetitions;
        }

        public double getWeight() {
                return weight;
        }

        public void setWeight(double weight) {
                this.weight = weight;
        }

        public int getResttime() {
                return resttime;
        }

        public void setResttime(int resttime) {
                this.resttime = resttime;
        }

        public Workout getWorkout() {
                return workout;
        }

        public void setWorkout(Workout workout) {
                this.workout = workout;
        }

        public Exercise getExercise() {
                return exercise;
        }

        public void setExercise(Exercise exercise) {
                this.exercise = exercise;
        }
}
