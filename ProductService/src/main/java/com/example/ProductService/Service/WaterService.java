package com.example.ProductService.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ProductService.entity.Water;
@Service
public interface WaterService {
	Water getOneWatter(Long id);
	List<Water> getAllWatter();
	Water addOrUppdateWatter(Water watter);
	boolean deleteWate(Long id);

}
