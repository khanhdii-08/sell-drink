package com.example.CartService.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.CartService.entity.Cart;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
	public Cart findByUserId(Long userId);

}
