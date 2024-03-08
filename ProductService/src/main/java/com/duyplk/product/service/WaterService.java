package com.duyplk.product.service;

import java.util.List;

import com.duyplk.product.entity.Water;
import org.springframework.stereotype.Service;

@Service
public interface WaterService {
	Water getOneWatter(Long id);
	List<Water> getAllWatter();
	Water addOrUppdateWatter(Water water);
	boolean deleteWate(Long id);

	List<Water> findByListId(List<Long> ids);

	List<Water> updateWaters(List<Water> waters);

}
