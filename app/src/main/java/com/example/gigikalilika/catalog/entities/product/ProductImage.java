package com.example.gigikalilika.catalog.entities.product;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductImage implements Serializable {

    public static final String TABLE_NAME = "PRODUCT_IMAGE";

    private Long productId;

    private String sourcePath;

}
