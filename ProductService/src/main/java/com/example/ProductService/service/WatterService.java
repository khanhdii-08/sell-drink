package com.example.ProductService.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ProductService.entity.Watter;
@Service
public interface WatterService {
	Watter getOneWatter(Long id);
	List<Watter> getAllWatter();
	Watter addOrUppdateWatter(Watter watter);
	boolean deleteWate(Long id);

	List<Watter> findByListId(List<Long> ids);

	List<Watter> updateWaters(List<Watter> watters);

}
