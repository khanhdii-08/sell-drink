package com.duypkl.cart.respository;

import com.duypkl.cart.entity.OrderDetail;
import com.duypkl.cart.entity.OrderDetailPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailPK> {
}
