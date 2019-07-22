package com.example.gigikalilika.catalog.entities.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order implements Serializable {

    private Long id;

    private Double value;

    private Date lastUpdated;

    private List<OrderItem> orderItemList;

}