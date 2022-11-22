package com.example.CartService.service;

import com.example.CartService.entity.Cart;
import com.example.CartService.entity.Order;
import com.example.CartService.entity.OrderDetail;
import com.example.CartService.entity.OrderDetailPK;

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
