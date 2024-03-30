package com.duyplk.cart.service;

import com.duyplk.cart.entity.Cart;
import com.duyplk.cart.entity.Order;
import com.duyplk.cart.entity.OrderDetail;
import com.duyplk.cart.entity.OrderDetailPK;

import java.util.List;

public interface OrderService {
    public Order addOrder(Cart cart) throws Exception;
    public OrderDetail saveOrderDetail(OrderDetail orderDetail);
    public List<OrderDetail> saveOrderDetails(List<OrderDetail> orders);
    public boolean removeOrderDetail(OrderDetailPK detailPK);
    public boolean deleteOrder(Long orderId);
    public Order getOrder(Long orderId);
    public List<Order> findAllByUserId(Long userId);

}
