package com.duyplk.cart.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private Long productId;
    private int quantity;
}
