package com.duyplk.cart.respository;

import com.duyplk.cart.entity.OrderDetail;
import com.duyplk.cart.entity.OrderDetailPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailPK> {
}
