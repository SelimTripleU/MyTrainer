package org.example.service;

import org.example.dao.WorkoutExerciseRepository;
import org.example.entity.Exercise;
import org.example.entity.Workout;
import org.example.entity.WorkoutExercise;

public class WorkoutExerciseService {

    private final WorkoutExerciseRepository workoutExerciseRepository = new WorkoutExerciseRepository();

    public WorkoutExercise createWorkoutExercise(int sets, int repetitions, double weight, int resttime, Workout workout, Exercise exercise) {

        WorkoutExercise workoutExercise = new WorkoutExercise();

        workoutExercise.setSets(sets);
        workoutExercise.setRepetitions(repetitions);
        workoutExercise.setWeight(weight);
        workoutExercise.setResttime(resttime);
        workoutExercise.setWorkout(workout);
        workoutExercise.setExercise(exercise);

        workoutExerciseRepository.save(workoutExercise);

        return workoutExercise;
    }

    public void deleteWorkoutExercise(int i) {
        WorkoutExercise workoutExerciseToBeDeleted = workoutExerciseRepository.findById(Long.valueOf(i));

        workoutExerciseRepository.delete(workoutExerciseToBeDeleted);

    }

    public void modifyWorkoutExercise(WorkoutExercise modifiedWorkoutExercise) {
        workoutExerciseRepository.update(modifiedWorkoutExercise);
    }

    public WorkoutExercise findWorkoutExerciseById(int id) {
        return workoutExerciseRepository.findById(Long.valueOf(id));
    }

}