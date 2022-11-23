package com.example.ProductService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ProductService.entity.Water;

@Repository
public interface WaterRepository extends JpaRepository<Water, Long>{

}
