package com.duyplk.cart.respository;

import com.duyplk.cart.entity.CartItem;
import com.duyplk.cart.entity.CartItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemPK>{
    @Modifying
    @Query(nativeQuery = true, value = "delete from cart_items where cart_id = ?1 ")
    public void deleteAllByCartId(Long cartId);
}
