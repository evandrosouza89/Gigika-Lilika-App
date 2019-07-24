package com.example.gigikalilika.catalog.entities.order;

import com.example.gigikalilika.catalog.entities.product.Product;
import com.example.gigikalilika.catalog.utils.Utils;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem implements Serializable {

    private Long id;

    private Product product;

    private String size;

    private Integer quantity;

    private Double value;

    public String toText() {
        String toString = "";

        if (product != null) {
            toString += product.toText();
        }

        toString += ".";

        toString += " Tam: " + size + ".";

        toString += " Qtd: " + quantity + ".";

        if (value != null) {
            toString += " Valor: " + Utils.formatPrice(value) + ".";
        } else {
            toString += " Valor: sob consulta.";
        }

        return toString;
    }

}