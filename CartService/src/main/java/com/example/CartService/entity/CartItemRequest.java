package com.example.CartService.entity;


import lombok.Data;

@Data
public class CartItemRequest {
	private Long productId;
	private int quantity;
	private double price;
}
