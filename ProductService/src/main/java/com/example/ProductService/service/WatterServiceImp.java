package com.example.ProductService.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ProductService.entity.Watter;
import com.example.ProductService.repository.WatterRepository;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class WatterServiceImp implements WatterService {
	
	@Autowired
	private WatterRepository watterRepository;

	@Override
	public Watter getOneWatter(Long id) {
		// TODO Auto-generated method stub
		return watterRepository.findById(id).get();
	}

	@Override
	public List<Watter> getAllWatter() {
		// TODO Auto-generated method stub
		return watterRepository.findAll();
	}

	@Override
	public Watter addOrUppdateWatter(Watter watter) {
		// TODO Auto-generated method stub
		return watterRepository.save(watter);
	}

	@Override
	public boolean deleteWate(Long id) {
		// TODO Auto-generated method stub
		Watter wt = watterRepository.findById(id).get();
		if(wt != null) {
			watterRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public List<Watter> findByListId(List<Long> ids) {
		try {
			return watterRepository.findAllById(ids);
		} catch (Exception e){
			System.out.println(e);
		}
		return null;
	}

	@Override
	public List<Watter> updateWaters(List<Watter> watters) {

		List<Watter> list = watterRepository.saveAll(watters);
		return list;

	}

}
