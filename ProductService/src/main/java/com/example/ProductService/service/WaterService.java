package com.example.ProductService.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ProductService.entity.Water;
@Service
public interface WaterService {
	Water getOneWatter(Long id);
	List<Water> getAllWatter();
	Water addOrUppdateWatter(Water water);
	boolean deleteWate(Long id);

	List<Water> findByListId(List<Long> ids);

	List<Water> updateWaters(List<Water> waters);

}
