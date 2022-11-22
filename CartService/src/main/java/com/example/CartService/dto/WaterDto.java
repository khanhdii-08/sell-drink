package com.example.CartService.dto;

import lombok.Data;


@Data
public class WaterDto {
    private Long id;
    private String name;
    private int quantity;
    private double price;
}
