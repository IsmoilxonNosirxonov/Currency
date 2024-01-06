package uz.in.currency.enumeration;

import lombok.Getter;

@Getter
public enum StrategyName {
    CBU("cbu"),
    NBU("nbu"),
    UNKNOWN("unknown");

    private final String name;
    StrategyName(String name) {
        this.name = name;
    }
}
