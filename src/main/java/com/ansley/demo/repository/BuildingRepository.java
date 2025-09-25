package com.ansley.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ansley.demo.model.Building;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    List<Building> findByCostBetween(double min, double max);
    List<Building> findByType(String type);
}