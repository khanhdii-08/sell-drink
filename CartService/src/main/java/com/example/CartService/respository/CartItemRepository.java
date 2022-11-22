package com.example.CartService.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.CartService.entity.CartItem;
import com.example.CartService.entity.CartItemPK;



@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemPK>{
    @Modifying
    @Query(nativeQuery = true, value = "delete from cart_items where cart_id = ?1 ")
    public void deleteAllByCartId(Long cartId);
}
