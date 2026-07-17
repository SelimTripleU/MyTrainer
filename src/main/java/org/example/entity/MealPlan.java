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

}
