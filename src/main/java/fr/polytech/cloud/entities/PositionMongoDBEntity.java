package fr.polytech.cloud.entities;

import lombok.Data;

import java.util.List;

public @Data class PositionMongoDBEntity {

    private String type;

    private List<Double> coordinates;
}