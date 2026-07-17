package org.example.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
@Entity
public class MealPlan {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private LocalDate datum;
        private int targetCalories;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @OneToMany(mappedBy = "mealPlan")
        private List<Meal> meals;

        public MealPlan(int id, LocalDate datum, int targetCalories, User user, List<Meal> meals) {
                this.id = id;
                this.datum = datum;
                this.targetCalories = targetCalories;
                this.user = user;
                this.meals = meals;
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

        public int getTargetCalories() {
                return targetCalories;
        }

        public void setTargetCalories(int targetCalories) {
                this.targetCalories = targetCalories;
        }

        public User getUser() {
                return user;
        }

        public void setUser(User user) {
                this.user = user;
        }

        public List<Meal> getMeals() {
                return meals;
        }

        public void setMeals(List<Meal> meals) {
                this.meals = meals;
        }
}
