package org.example.service;

import org.example.dao.WorkoutRepository;
import org.example.entity.User;
import org.example.entity.Workout;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class WorkoutService {

    private final WorkoutRepository workoutRepository = new WorkoutRepository();

    public Workout createWorkout(LocalDate date, LocalTime startTime, LocalTime endTime, int duration, String notes, User user) {

        Workout workout = new Workout();

        workout.setDatum(date);
        workout.setStartTime(startTime);
        workout.setEndTime(endTime);
        workout.setDuration(duration);
        workout.setNotes(notes);
        workout.setUser(user);

        workoutRepository.save(workout);

        return workout;
    }

    public void deleteWorkout(int i) {
        Workout workoutToBeDeleted = workoutRepository.findById(Long.valueOf(i));

        workoutRepository.delete(workoutToBeDeleted);

    }

    public void modifyWorkout(Workout modifiedWorkout) {
        workoutRepository.update(modifiedWorkout);
    }

    public Workout findWorkoutById(int id) {
        return workoutRepository.findById(Long.valueOf(id));
    }

    public List<Workout> findAllWorkouts() {
        return workoutRepository.findAll();
    }

    public Workout findWorkoutByUserAndDatum(User user, LocalDate datum) {
        for (Workout workout : findAllWorkouts()) {
            if (workout.getUser().getId() == user.getId() && workout.getDatum().equals(datum)) {
                return workout;
            }
        }

        return null;
    }

}