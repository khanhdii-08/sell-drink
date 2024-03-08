package com.duypkl.cart.service;

import com.duypkl.cart.entity.Cart;
import com.duypkl.cart.entity.CartItem;
import com.duypkl.cart.entity.CartItemPK;


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
