package com.duyplk.cart.service;

import com.duyplk.cart.entity.Cart;
import com.duyplk.cart.entity.CartItem;
import com.duyplk.cart.entity.CartItemPK;


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
