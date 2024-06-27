package kz.project.techway.enums;

import lombok.Getter;

@Getter
public enum CurrencyEnum {
    USD("USD"),
    EUR("EUR"),
    RUB("RUB"),
    KZT("KZT");
    private final String code;

    CurrencyEnum(String code) {
        this.code = code;
    }
}
