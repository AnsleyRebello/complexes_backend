package com.ansley.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ansley.demo.model.Building;
import com.ansley.demo.service.BuildingService;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    @Autowired 
    private BuildingService buildingService;

    // Get all buildings
    @GetMapping
    public List<Building> getAllBuildings() {
        return buildingService.getAllBuildings();
    }

    // Filter buildings
    @GetMapping("/filter")
    public List<Building> filterBuildings(@RequestParam double minCost,
                                          @RequestParam double maxCost,
                                          @RequestParam(required=false) String type) {
        return buildingService.filterBuildings(minCost, maxCost, type);
    }

    // Get building by ID
    @GetMapping("/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable Long id) {
        return buildingService.getBuildingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add new building
    @PostMapping
    public Building addBuilding(@RequestBody Building b) { 
        return buildingService.save(b); 
    }

    // Update existing building
    @PutMapping("/{id}")
    public ResponseEntity<Building> updateBuilding(
            @PathVariable Long id, 
            @RequestBody Building updatedBuilding) {

        Optional<Building> existingBuilding = buildingService.getBuildingById(id);

        if (existingBuilding.isPresent()) {
            Building b = existingBuilding.get();
            // Update fields (you can add more as needed)
            b.setName(updatedBuilding.getName());
            b.setLocation(updatedBuilding.getLocation());
            b.setCost(updatedBuilding.getCost());
            b.setType(updatedBuilding.getType());
            b.setDescription(updatedBuilding.getDescription());
            b.setAvailable(updatedBuilding.isAvailable());
            b.setImages(updatedBuilding.getImages());
            b.setPriceHistory(updatedBuilding.getPriceHistory());
            
            buildingService.save(b);
            return ResponseEntity.ok(b);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete building
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBuilding(@PathVariable Long id) {
        buildingService.delete(id);
        return ResponseEntity.ok("Building deleted successfully");
    }
}
