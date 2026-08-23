package org.example.service;

import org.example.dao.BodyMeasurementRepository;
import org.example.entity.BodyMeasurement;
import org.example.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BodyMeasurementService {

    private final BodyMeasurementRepository bodyMeasurementRepository = new BodyMeasurementRepository();

   public BodyMeasurement createBodyMeasurement(LocalDate date, double weight, double height, double bodyfat, User user) {

       BodyMeasurement bodyMeasurement = new BodyMeasurement();

       bodyMeasurement.setDate(date);
       bodyMeasurement.setWeight(weight);
       bodyMeasurement.setHeight(height);
       bodyMeasurement.setBodyfat(bodyfat);
       bodyMeasurement.setUser(user);

       bodyMeasurementRepository.save(bodyMeasurement);

       return bodyMeasurement;
   }

   public void deleteBodyMeasurement(int i) {
       BodyMeasurement bodyMeasToBeDeleted = bodyMeasurementRepository.findById(Long.valueOf(i));

       bodyMeasurementRepository.delete(bodyMeasToBeDeleted);

   }

   public void modifyBodyMeasurement(BodyMeasurement modifiedBodyMeasurement) {
       bodyMeasurementRepository.update(modifiedBodyMeasurement);
   }

   public BodyMeasurement findBodyMeasurementById(int id) {
       return bodyMeasurementRepository.findById(Long.valueOf(id));
   }

   public List<BodyMeasurement> findAllByUser(User user) {
       List<BodyMeasurement> result = new ArrayList<>();

       for (BodyMeasurement bodyMeasurement : bodyMeasurementRepository.findAll()) {
           if (bodyMeasurement.getUser().getId() == user.getId()) {
               result.add(bodyMeasurement);
           }
       }

       result.sort(Comparator.comparing(BodyMeasurement::getDate));

       return result;
   }

}
