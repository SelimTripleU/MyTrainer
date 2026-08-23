package org.example.service;

import org.example.dao.ExerciseRepository;
import org.example.entity.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseService {

    private final ExerciseRepository exerciseRepository = new ExerciseRepository();

    public Exercise createExercise(String name, String muscleGroup, String description) {

        Exercise exercise = new Exercise();

        exercise.setName(name);
        exercise.setMuscleGroup(muscleGroup);
        exercise.setDescription(description);

        exerciseRepository.save(exercise);

        return exercise;
    }

    public void deleteExercise(int i) {
        Exercise exerciseToBeDeleted = exerciseRepository.findById(Long.valueOf(i));

        exerciseRepository.delete(exerciseToBeDeleted);

    }

    public void modifyExercise(Exercise modifiedExercise) {
        exerciseRepository.update(modifiedExercise);
    }

    public Exercise findExerciseById(int id) {
        return exerciseRepository.findById(Long.valueOf(id));
    }

    public List<Exercise> findAllExercises() {
        return exerciseRepository.findAll();
    }

    public List<Exercise> findExercisesByMuscleGroup(String muscleGroup) {
        List<Exercise> result = new ArrayList<>();

        for (Exercise exercise : findAllExercises()) {
            if (exercise.getMuscleGroup().equals(muscleGroup)) {
                result.add(exercise);
            }
        }

        return result;
    }

    // creates a handful of default exercises per muscle group on first start, so the selection isn't empty
    public void seedStandardExercisesIfEmpty() {
        if (!findAllExercises().isEmpty()) {
            return;
        }

        createExercise("Bankdrücken", "Brust", null);
        createExercise("Schrägbankdrücken", "Brust", null);
        createExercise("Liegestütze", "Brust", null);
        createExercise("Butterfly", "Brust", null);
        createExercise("Kabelzug Brust (Gym)", "Brust", null);
        createExercise("Bankdrücken Maschine (Gym)", "Brust", null);

        createExercise("Klimmzüge", "Rücken", null);
        createExercise("Rudern", "Rücken", null);
        createExercise("Latzug", "Rücken", null);
        createExercise("Kreuzheben", "Rücken", null);
        createExercise("Rudermaschine (Gym)", "Rücken", null);
        createExercise("T-Bar Rudern (Gym)", "Rücken", null);

        createExercise("Kniebeugen", "Beine", null);
        createExercise("Beinpresse", "Beine", null);
        createExercise("Ausfallschritte", "Beine", null);
        createExercise("Beinstrecker", "Beine", null);
        createExercise("Beinbeuger Maschine (Gym)", "Beine", null);
        createExercise("Adduktorenmaschine (Gym)", "Beine", null);

        createExercise("Schulterdrücken", "Schultern", null);
        createExercise("Seitheben", "Schultern", null);
        createExercise("Frontheben", "Schultern", null);
        createExercise("Schulterpresse Maschine (Gym)", "Schultern", null);
        createExercise("Kabelzug Seitheben (Gym)", "Schultern", null);

        createExercise("Bizepscurls", "Arme", null);
        createExercise("Trizepsdrücken", "Arme", null);
        createExercise("Hammercurls", "Arme", null);
        createExercise("Bizeps Kabelzug (Gym)", "Arme", null);
        createExercise("Trizeps Kabelzug (Gym)", "Arme", null);
        createExercise("Dip-Maschine (Gym)", "Arme", null);

        createExercise("Crunches", "Bauch", null);
        createExercise("Plank", "Bauch", null);
        createExercise("Beinheben", "Bauch", null);
        createExercise("Bauchmaschine (Gym)", "Bauch", null);
        createExercise("Kabelzug Crunches (Gym)", "Bauch", null);

        createExercise("Burpees", "Ganzkörper", null);
        createExercise("Kettlebell Swings", "Ganzkörper", null);
        createExercise("Rudergerät (Gym)", "Ganzkörper", null);
        createExercise("Cross-Trainer (Gym)", "Ganzkörper", null);
        createExercise("Multipresse (Gym)", "Ganzkörper", null);
    }

}