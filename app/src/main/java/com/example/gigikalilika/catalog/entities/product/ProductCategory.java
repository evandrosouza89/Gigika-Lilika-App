package com.example.gigikalilika.catalog.entities.product;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductCategory implements Serializable {

    public static final String TABLE_NAME = "PRODUCT_CATEGORY";

    private Long productId;

    private Long categoryId;

}