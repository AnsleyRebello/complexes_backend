package com.ansley.demo.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double cost;
    private String type; // apartment/office/land
    private String location;
    private boolean available;

    @ElementCollection
    private List<String> images;

    @ElementCollection
    private List<Double> priceHistory;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public List<Double> getPriceHistory() {
		return priceHistory;
	}

	public void setPriceHistory(List<Double> priceHistory) {
		this.priceHistory = priceHistory;
	}

	@Override
	public String toString() {
		return "Building [id=" + id + ", name=" + name + ", description=" + description + ", cost=" + cost + ", type="
				+ type + ", location=" + location + ", available=" + available + ", images=" + images
				+ ", priceHistory=" + priceHistory + "]";
	}

	public Building() {
		super();
	}

    
}
