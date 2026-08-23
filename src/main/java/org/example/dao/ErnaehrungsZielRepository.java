package org.example.dao;

import org.example.entity.ErnaehrungsZiel;

public class ErnaehrungsZielRepository extends GenericDao<ErnaehrungsZiel> {

    public ErnaehrungsZielRepository() {
        super(ErnaehrungsZiel.class);
    }
}
