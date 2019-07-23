package com.example.gigikalilika.catalog.entities.offer;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OfferText implements Serializable {

    public static final String TABLE_NAME = "OFFER_TEXT";

    private Long id;

    private Long offerId;

    private String offerText;

}