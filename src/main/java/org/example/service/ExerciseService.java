package org.example.service;

import org.example.dao.ExerciseRepository;
import org.example.entity.Exercise;

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

}