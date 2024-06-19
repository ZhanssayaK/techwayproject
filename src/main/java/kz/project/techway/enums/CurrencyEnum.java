package kz.project.techway.enums;

import lombok.Getter;

@Getter
public enum CurrencyEnum {
    USD("usd"),
    EUR("eur"),
    RUB("rub");
    private final String code;

    CurrencyEnum(String code) {
        this.code = code;
    }
}
