package org.example;

import org.example.entity.BodyMeasurement;
import org.example.entity.User;
import org.example.service.BodyMeasurementService;
import org.example.service.UserService;

import java.time.LocalDate;

public class TestBackend {
    public static void main(String[] args) {
        UserService userService = new UserService();
        BodyMeasurementService bodyMeasurementService = new BodyMeasurementService();

        // User erstellen
        User user = userService.createUser(
                "Kevin",
                LocalDate.of(2002, 10, 22),
                1.90,
                "male"
        );

        // Prüfen, ob der User gespeichert wurde
        System.out.println("User ID: " + user.getId());

        // BodyMeasurement für den User erstellen
        BodyMeasurement measurement = bodyMeasurementService.createBodyMeasurement(
                LocalDate.now(),
                85,
                1.90,
                18,
                user
        );
        System.out.println("Body Measurement fat: " + measurement.getBodyfat());
        measurement.setBodyfat(99.9);
        bodyMeasurementService.modifyBodyMeasurement(measurement);
        BodyMeasurement modifiedBodyMeasurement = bodyMeasurementService.findBodyMeasurementById(measurement.getId());
        System.out.println("Neues Bodyfat " + modifiedBodyMeasurement.getBodyfat());
    }


}
