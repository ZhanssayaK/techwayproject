package kz.project.techway.enums;

import lombok.Getter;

@Getter
public enum TimePeriodEnum {
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    YEAR("year");

    public final String period;

    TimePeriodEnum(String period) {
        this.period = period;
    }
}