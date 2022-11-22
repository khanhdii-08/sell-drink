package com.example.ProductService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ProductService.entity.Watter;

@Repository
public interface WatterRepository extends JpaRepository<Watter, Long>{

}
