package org.example.dao;

import org.example.entity.Rezept;

public class RezeptRepository extends GenericDao<Rezept> {

    public RezeptRepository() {
        super(Rezept.class);
    }
}
