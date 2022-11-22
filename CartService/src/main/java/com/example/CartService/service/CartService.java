package com.example.CartService.service;

import java.util.List;

import com.example.CartService.entity.Cart;
import com.example.CartService.entity.CartItem;
import com.example.CartService.entity.CartItemPK;



public interface CartService {
	public Cart addCart(Cart cart);
	public CartItem saveItem(CartItem item) throws Exception;
	public boolean removeItem(CartItemPK itemId);
	public boolean removeItems(Long cartId);
	public boolean deleteCart(Long cartId);
	public Cart getCart(Long cartId);
	public Cart getCartByUserId(Long userId);

	public Cart saveCart(Cart cart);
}
