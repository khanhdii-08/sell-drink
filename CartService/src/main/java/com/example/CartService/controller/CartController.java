package com.example.CartService.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.CartService.entity.Cart;
import com.example.CartService.entity.CartItem;
import com.example.CartService.entity.CartItemPK;
import com.example.CartService.entity.CartItemRequest;
import com.example.CartService.service.CartService;






@RestController
@RequestMapping("/api/carts")
public class CartController {
	@Autowired
	CartService cartService;
	
	@GetMapping("/{userId}")
	public ResponseEntity<Cart> getCartByUserId(@PathVariable Long userId) {
		Cart cart = cartService.getCartByUserId(userId);
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/items")
	public ResponseEntity<CartItem> saveOrUpdate(@RequestBody CartItemRequest item) {
		Cart cart = cartService.getCartByUserId(1L);
		
		CartItem i = null;
		if(cart != null) {
			CartItem item2 = new CartItem(cart, item.getProductId(), item.getQuantity(), item.getPrice());
			i = cartService.saveItem(item2);
			
		}
		return ResponseEntity.ok(i);
	}
	
	@DeleteMapping("/items/{productId}")
	public void removeItem(@PathVariable Long productId) {
		cartService.removeItem(new CartItemPK(1l, productId));
	}

}
