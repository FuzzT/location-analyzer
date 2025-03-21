package com.location.controller;

import com.location.model.MergedLocation;
import com.location.service.LocationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/count-per-type")
    public Map<String, Long> countPerType() {
        return locationService.countLocationsPerType();
    }

    @GetMapping("/average-ratings")
    public Map<String, Double> averageRatings() {
        return locationService.averageRatingsPerType();
    }

    @GetMapping("/most-reviewed")
    public MergedLocation mostReviewed() {
        return locationService.mostReviewedLocation();
    }

    @GetMapping("/incomplete-data")
    public List<MergedLocation> incompleteData() {
        return locationService.getIncompleteData();
    }
}
