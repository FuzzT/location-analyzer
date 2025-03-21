package com.location.repository;

import com.location.model.MergedLocation;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LocationRepository {
    private final List<MergedLocation> locations = new ArrayList<>();

    public List<MergedLocation> getAllLocations() {
        return locations;
    }

    public void saveAll(List<MergedLocation> mergedLocations) {
        locations.clear();
        locations.addAll(mergedLocations);
    }
}
