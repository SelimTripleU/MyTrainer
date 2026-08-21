package org.example.dao;

import org.example.entity.Exercise;

public class ExerciseRepository extends GenericDao<Exercise> {

    public ExerciseRepository() {
        super(Exercise.class);
    }
}
