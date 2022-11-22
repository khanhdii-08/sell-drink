package com.example.CartService.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "order_details")
@IdClass(OrderDetailPK.class)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class OrderDetail {
    @Id
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "orderId")
    private Order order;
    @Id
    private Long productId;
    private int quantity;
    private double price;


    public OrderDetailPK getId() {
        return new OrderDetailPK(order.getId(), productId);
    }
    public void setId(OrderDetailPK id) {
        this.productId = id.getProductId();
        this.order.setId(id.getOrder());
    }
    public OrderDetail(Order order, Long productId, int quantity) {
        this.order = order;
        this.productId = productId;
        this.quantity = quantity;
    }
    public OrderDetail(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

}
