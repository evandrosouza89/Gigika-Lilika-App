package com.example.gigikalilika.catalog.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumProductSize {

    SIZE_01("01"),
    SIZE_02("02"),
    SIZE_03("03"),
    SIZE_04("04"),
    SIZE_05("05"),
    SIZE_OTHER("Outro");

    private String text;

}
