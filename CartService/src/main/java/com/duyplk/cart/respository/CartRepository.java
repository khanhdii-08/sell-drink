package com.duyplk.cart.respository;

import com.duyplk.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
	public Cart findByUserId(Long userId);

}
