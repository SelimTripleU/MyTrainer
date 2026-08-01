package org.example.dao;

import org.example.entity.BodyMeasurement;

public class BodyMeasurementRepository extends GenericDao<BodyMeasurement> {
    public BodyMeasurementRepository() {
        super(BodyMeasurement.class);
    }
}
