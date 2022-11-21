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

import com.example.ProductService.Service.WatterService;
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


}
