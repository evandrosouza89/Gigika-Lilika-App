package com.example.gigikalilika.catalog.entities.offer;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Offer implements Serializable {

    public static final String TABLE_NAME = "OFFER";

    private Long id;

    private Integer bannerType;

    private String labelText;

    private String backgroundSourcePath;

    private String searchQuery;

    private List<OfferText> offerTextList;
}