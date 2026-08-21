package org.example.dao;

import org.example.entity.WorkoutExercise;

public class WorkoutExerciseRepository extends GenericDao<WorkoutExercise> {

    public WorkoutExerciseRepository() {
        super(WorkoutExercise.class);
    }
}
