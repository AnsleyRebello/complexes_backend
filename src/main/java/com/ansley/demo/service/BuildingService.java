package com.ansley.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ansley.demo.model.Building;
import com.ansley.demo.repository.BuildingRepository;

@Service
public class BuildingService {

    @Autowired 
    private BuildingRepository buildingRepo;

    public List<Building> getAllBuildings() { 
        return buildingRepo.findAll(); 
    }

    public List<Building> filterBuildings(double minCost, double maxCost, String type) {
        return buildingRepo.findByCostBetween(minCost, maxCost)
                .stream()
                .filter(b -> type == null || b.getType().equalsIgnoreCase(type))
                .toList();
    }

    public Optional<Building> getBuildingById(Long id) { 
        return buildingRepo.findById(id); 
    }

    public Building save(Building b) { 
        return buildingRepo.save(b); 
    }

    public void delete(Long id) { 
        buildingRepo.deleteById(id); 
    }
}
