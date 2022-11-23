package com.example.ProductService.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ProductService.entity.Water;
import com.example.ProductService.repository.WaterRepository;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class WaterServiceImp implements WaterService {
	
	@Autowired
	private WaterRepository waterRepository;

	@Override
	public Water getOneWatter(Long id) {
		// TODO Auto-generated method stub
		return waterRepository.findById(id).get();
	}

	@Override
	public List<Water> getAllWatter() {
		// TODO Auto-generated method stub
		return waterRepository.findAll();
	}

	@Override
	public Water addOrUppdateWatter(Water water) {
		// TODO Auto-generated method stub
		return waterRepository.save(water);
	}

	@Override
	public boolean deleteWate(Long id) {
		// TODO Auto-generated method stub
		Water wt = waterRepository.findById(id).get();
		if(wt != null) {
			waterRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public List<Water> findByListId(List<Long> ids) {
		try {
			return waterRepository.findAllById(ids);
		} catch (Exception e){
			System.out.println(e);
		}
		return null;
	}

	@Override
	public List<Water> updateWaters(List<Water> watters) {

		List<Water> list = waterRepository.saveAll(watters);
		return list;

	}

}
