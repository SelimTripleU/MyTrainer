package org.example.entity;

import jakarta.persistence.*;
@Entity
public class Meal {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String name;
        private int calories;
        private double protein;
        private double carbohydrates;
        private double fat;

        @ManyToOne
        @JoinColumn(name = "meal_plan_id")
        private MealPlan mealPlan;


        public Meal(int id, String name, int calories, double protein, double carbohydrates, double fat, MealPlan mealPlan) {
                this.id = id;
                this.name = name;
                this.calories = calories;
                this.protein = protein;
                this.carbohydrates = carbohydrates;
                this.fat = fat;
                this.mealPlan = mealPlan;
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

        public int getCalories() {
                return calories;
        }

        public void setCalories(int calories) {
                this.calories = calories;
        }

        public double getProtein() {
                return protein;
        }

        public void setProtein(double protein) {
                this.protein = protein;
        }

        public double getCarbohydrates() {
                return carbohydrates;
        }

        public void setCarbohydrates(double carbohydrates) {
                this.carbohydrates = carbohydrates;
        }

        public double getFat() {
                return fat;
        }

        public void setFat(double fat) {
                this.fat = fat;
        }

        public MealPlan getMealPlan() {
                return mealPlan;
        }

        public void setMealPlan(MealPlan mealPlan) {
                this.mealPlan = mealPlan;
        }
}
