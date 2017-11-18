package fr.polytech.cloud.entities.dao;

import lombok.Data;

import java.util.List;

public @Data class PositionDao {

    private String type;

    private List<Double> coordinates;
}