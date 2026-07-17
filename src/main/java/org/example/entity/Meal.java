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

}
