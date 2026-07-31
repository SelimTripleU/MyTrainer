# MyTrainer

Fitness Tracker 
Dieses Projekjt ist eine Java-Anwendung zur Verwaltung von Trainingsplänen, Mahlzeiten und Fitnessfortschritten. Die Anwendung ermöglischt es Benutzern, Übungen zu erstellen, Gerichte zu entdecken und seinen Fortschritt zu Dokumentieren.

## Funktion 

-Benutzerverwaltung 

-Erstellen und Verwalten von Trainingsplänen

-Bmi Rechner

-Speichern von Trainingseinheiten

-Entdeckung allerlei Gerichten

-Datenbankbindung mit Hibernate

## Verwendete Technologien 

-Java

-Hibernate

-JPA

-MySQL

-Git und GitHub

-DAO-Pattern

## Projektstruktur 

-entity - Enthält die Entity-Klassen

-dao- Enthält die DAO Klassen für den Datenbankzugriff

-util- Enthält die HibernateUtil

-service - Enthält die Geschäftslogik

-main - Startpunkt der Anwendung


## Skizzenleiter
User

│

├── BodyMeasurement

├── Workout

│   └── WorkoutExercise

│       └── Exercise

├── MealPlan

│   └── Meal


```mermaid
erDiagram
    User ||--o{ Workout : creates
    Workout ||--o{ WorkoutExercise : contains
    Exercise ||--o{ WorkoutExercise : used_in
    User ||--o{ Mealplan : owns
    Mealplan ||--o{ Meal : contains
    User ||--o{ BodyMeasurement : records

    User {
        int user_id PK
        string name
        int age
        float height
        datetime created_at
    }

    Workout {
        int workout_id PK
        int user_id FK
        string title
        date date
        int duration
    }

    WorkoutExercise {
        int workout_exercise_id PK
        int workout_id FK
        int exercise_id FK
        int sets
        int reps
        float weight
        int duration
    }

    Exercise {
        int exercise_id PK
        string name
        string muscle_group
        string description
    }

    Mealplan {
        int mealplan_id PK
        int user_id FK
        string title
        date start_date
        date end_date
    }

    Meal {
        int meal_id PK
        int mealplan_id FK
        string name
        int calories
        float protein
        float carbs
        float fat
    }

    BodyMeasurement {
        int measurement_id PK
        int user_id FK
        date measurement_date
        float weight
        float body_fat_percentage
        float chest
        float waist
        float hips
    }
```
