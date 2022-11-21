package com.example.CartService.service.imp;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CartService.entity.Cart;
import com.example.CartService.entity.CartItem;
import com.example.CartService.entity.CartItemPK;
import com.example.CartService.respository.CartItemRepository;
import com.example.CartService.respository.CartRepository;
import com.example.CartService.service.CartService;




@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartItemRepository itemRepository;
	@Override
	public Cart addCart(Cart cart) {
		// TODO Auto-generated method stub
		return cartRepository.saveAndFlush(cart);
	}
	@Override
	public CartItem saveItem(CartItem item) {
		// TODO Auto-generated method stub
		return itemRepository.save(item);
	}
	@Override
	public boolean removeItem(CartItemPK itemId) {
		try {
			itemRepository.deleteById(itemId);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public boolean deleteCart(Long cartId) {
		try {
			cartRepository.deleteById(cartId);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public Cart getCart(Long cartId) {
		Cart cart = cartRepository.findById(cartId).get();
		return cart;
	}

	@Override
	public Cart getCartByUserId(Long userId) {

		Cart cart = cartRepository.findByUserId(userId);
		return cart;
	}



}