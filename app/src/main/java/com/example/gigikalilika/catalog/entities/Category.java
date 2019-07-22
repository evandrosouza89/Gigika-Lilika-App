package com.example.gigikalilika.catalog.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Category implements Serializable {

    public static final String TABLE_NAME = "CATEGORY";

    private Long id;

    private String name;

    private String backgroundImage;
}