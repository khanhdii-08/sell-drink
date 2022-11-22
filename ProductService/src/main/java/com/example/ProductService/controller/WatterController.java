package com.example.ProductService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ProductService.service.WatterService;
import com.example.ProductService.entity.Watter;

@RestController
@RequestMapping("api/watters")
public class WatterController {
	
	@Autowired
	private WatterService watterService;
	
	@GetMapping()
	public List<Watter> getAllWatter(){
		return watterService.getAllWatter();
	}

	@GetMapping("/{id}")
	public Watter getOnWatter(@PathVariable Long id) {
		return watterService.getOneWatter(id);
	}

	@PostMapping
	public Watter addOrUpdateWatter(@RequestBody Watter watter) {
		return watterService.addOrUppdateWatter(watter);
		
	}
	@DeleteMapping("/{id}")
	public boolean deleteWatter(@PathVariable Long id) {
		boolean check = watterService.deleteWate(id);
		return check;
	}

	@PostMapping("/ids")
	public List<Watter> getAllWaterByIds(@RequestBody List<Long> ids){
		return watterService.findByListId(ids);
	}

	@PutMapping
	public boolean addOrUpdatePizza(@RequestBody List<Watter> listUpdate) {
		if(watterService.updateWaters(listUpdate).size() > 0)
			return true;
		return false;
	}

}
