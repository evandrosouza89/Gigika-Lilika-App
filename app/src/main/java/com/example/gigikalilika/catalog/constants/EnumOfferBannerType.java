package com.example.gigikalilika.catalog.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumOfferBannerType {

    NO_LABEL,
    COLOR_LABEL,
    FADING;

    public static EnumOfferBannerType getByValue(int value) {
        for (EnumOfferBannerType enumOfferBannerType : values()) {
            if (enumOfferBannerType.ordinal() == value) {
                return enumOfferBannerType;
            }
        }
        return null;
    }
}