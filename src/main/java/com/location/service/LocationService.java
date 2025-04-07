package com.location.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.location.model.Location;
import com.location.model.MergedLocation;
import com.location.model.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final ObjectMapper objectMapper;
    private List<MergedLocation> mergedData = new ArrayList<>();

    @Autowired
    public LocationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        loadAndProcessData();
    }

    private void loadAndProcessData() {
        try {
            InputStream locationsStream = getClass().getResourceAsStream("/locations.json");
            InputStream metadataStream = getClass().getResourceAsStream("/metadata.json");

            if (locationsStream == null || metadataStream == null) {
                throw new RuntimeException("JSON data files are missing!");
            }

            List<Location> locations = objectMapper.readValue(locationsStream, new TypeReference<>() {});
            List<Metadata> metadataList = objectMapper.readValue(metadataStream, new TypeReference<>() {});

            Map<String, Metadata> metadataMap = metadataList.stream()
                    .collect(Collectors.toMap(Metadata::getId, m -> m));

            for (Location loc : locations) {
                Metadata meta = metadataMap.get(loc.getId());
                MergedLocation merged = new MergedLocation();
                merged.setId(loc.getId());
                merged.setLatitude(loc.getLatitude());
                merged.setLongitude(loc.getLongitude());
                merged.setType(meta != null ? meta.getType() : "Unknown");
                merged.setRating(meta != null && meta.getRating() != null ? meta.getRating() : 0.0);
                merged.setReviews(meta != null && meta.getReviews() != null ? meta.getReviews() : 0);
                mergedData.add(merged);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Long> countLocationsPerType() {
        return mergedData.stream()
                .filter(loc -> loc.getType() != null)
                .collect(Collectors.groupingBy(MergedLocation::getType, Collectors.counting()));
    }

    public Map<String, Double> averageRatingsPerType() {
        return mergedData.stream()
                .filter(loc -> loc.getType() != null && loc.getRating() != null)
                .collect(Collectors.groupingBy(
                        MergedLocation::getType,
                        Collectors.averagingDouble(MergedLocation::getRating)
                ));
    }

    public MergedLocation mostReviewedLocation() {
        return mergedData.stream()
                .filter(loc -> loc.getReviews() != null)
                .max(Comparator.comparingInt(MergedLocation::getReviews))
                .orElse(new MergedLocation()); // no null is returned
    }

    private static boolean hasIncompleteData(MergedLocation loc) {
        return loc.getType() == null || loc.getRating() == null || loc.getReviews() == null;
    }

    public List<MergedLocation> getIncompleteData() {
        return mergedData.stream()
                .filter(LocationService::hasIncompleteData)
                .collect(Collectors.toList());
    }

    public double getCombinedAverage(String type1, String type2) {
        List<MergedLocation> filtered = mergedData.stream()
                .filter(loc -> loc.getType() != null && loc.getRating() != null)
                .filter(loc -> loc.getType().equalsIgnoreCase(type1) || loc.getType().equalsIgnoreCase(type2))
                .toList();

        if (filtered.isEmpty()) return 0.0;

        double total = filtered.stream().mapToDouble(MergedLocation::getRating).sum();
        return total / filtered.size();
    }

}
