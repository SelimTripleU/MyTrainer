package org.example.dao;

import org.example.entity.Workout;

public class WorkoutRepository extends GenericDao<Workout> {

    public WorkoutRepository() {
        super(Workout.class);
    }
}
