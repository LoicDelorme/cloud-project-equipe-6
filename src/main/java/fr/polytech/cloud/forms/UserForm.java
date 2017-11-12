package fr.polytech.cloud.forms;

import lombok.Data;

import java.util.Date;

public @Data class UserForm {

    private String id;

    private String lastName;

    private String firstName;

    private Date birthDay;

    private Double lat;

    private Double lon;
}