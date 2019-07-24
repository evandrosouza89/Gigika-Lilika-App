package com.example.gigikalilika.catalog.entities.product;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Product implements Serializable {

    public static final String TABLE_NAME = "PRODUCT";
    private Long id;

    private String referenceCode;

    private String name;

    private String description;

    private List<ProductImage> productImageList;

    private List<ProductPrice> productPriceList;

    private List<ProductCategory> productCategoryList;

    public String toText() {
        String toString = "";

        toString += "#" + referenceCode + " - " + name;

        return toString;
    }
}