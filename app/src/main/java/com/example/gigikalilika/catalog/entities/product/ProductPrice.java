package com.example.gigikalilika.catalog.entities.product;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductPrice implements Serializable {

    public static final String TABLE_NAME = "PRODUCT_PRICE";

    private Long productId;

    private String productSize;

    private Double productPrice;

}