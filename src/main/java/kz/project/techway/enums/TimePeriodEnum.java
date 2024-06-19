package kz.project.techway.enums;

import lombok.Getter;

@Getter
public enum TimePeriodEnum {
    WEEK("week"),
    MONTH("month"),
    YEAR("year");

    private final String period;

    TimePeriodEnum(String period) {
        this.period = period;
    }

}