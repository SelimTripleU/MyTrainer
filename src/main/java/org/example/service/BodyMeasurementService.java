package org.example.service;

import org.example.dao.BodyMeasurementRepository;
import org.example.entity.BodyMeasurement;
import org.example.entity.User;

import java.time.LocalDate;

public class BodyMeasurementService {

    private final BodyMeasurementRepository bodyMeasurementRepository = new BodyMeasurementRepository();

   public BodyMeasurement createBodyMeasurement(LocalDate date, double weight, double height, double bodyfat, User user) {

       BodyMeasurement bodyMeasurement = new BodyMeasurement();

       bodyMeasurement.setDatum(date);
       bodyMeasurement.setWeight(weight);
       bodyMeasurement.setHeight(height);
       bodyMeasurement.setBodyfat(bodyfat);
       bodyMeasurement.setUser(user);

       bodyMeasurementRepository.save(bodyMeasurement);

       return bodyMeasurement;
   }
}
