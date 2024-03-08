package com.duyplk.product.controller;

import java.util.List;

import com.duyplk.product.entity.Water;
import com.duyplk.product.service.WaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/waters")
public class WatterController {
	
	@Autowired
	private WaterService waterService;
	
	@GetMapping()
	public List<Water> getAllWatter(){
		return waterService.getAllWatter();
	}

	@GetMapping("/{id}")
	public Water getOnWatter(@PathVariable Long id) {
		return waterService.getOneWatter(id);
	}

	@PostMapping
	public Water addOrUpdateWatter(@RequestBody Water water) {
		return waterService.addOrUppdateWatter(water);
		
	}
	@DeleteMapping("/{id}")
	public boolean deleteWatter(@PathVariable Long id) {
		boolean check = waterService.deleteWate(id);
		return check;
	}

	@PostMapping("/ids")
	public List<Water> getAllWaterByIds(@RequestBody List<Long> ids){
		return waterService.findByListId(ids);
	}

	@PutMapping
	public boolean addOrUpdatePizza(@RequestBody List<Water> listUpdate) {
		if(waterService.updateWaters(listUpdate).size() > 0)
			return true;
		return false;
	}

}
