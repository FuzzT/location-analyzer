package com.location.model;

import lombok.Data;

@Data
public class MergedLocation {
    private String id;
    private double latitude;
    private double longitude;
    private String type;
    private Double rating;
    private Integer reviews;
}
