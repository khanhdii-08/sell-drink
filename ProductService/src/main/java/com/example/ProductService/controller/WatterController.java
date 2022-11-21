package com.example.ProductService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProductService.Service.WaterService;
import com.example.ProductService.entity.Water;

@RestController
@RequestMapping("api/waters")
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
	public Water addOrUpdateWatter(@RequestBody Water watter) {
		return waterService.addOrUppdateWatter(watter);
		
	}
	@DeleteMapping("/{id}")
	public boolean deleteWatter(@PathVariable Long id) {
		boolean check = waterService.deleteWate(id);
		return check;
	}


}
