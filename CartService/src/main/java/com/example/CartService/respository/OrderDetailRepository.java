package com.example.CartService.respository;

import com.example.CartService.entity.OrderDetail;
import com.example.CartService.entity.OrderDetailPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailPK> {
}
