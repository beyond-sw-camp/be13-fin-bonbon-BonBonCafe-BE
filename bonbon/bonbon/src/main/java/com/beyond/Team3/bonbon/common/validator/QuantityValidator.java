package com.beyond.Team3.bonbon.common.validator;

import java.math.BigDecimal;

public class QuantityValidator {
    private static final BigDecimal MAX_QUANTITY = new BigDecimal("999999");

    public static void validate(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
        }
        if (quantity > 9999999) {
            throw new IllegalArgumentException("가격은 9999999 이하로 입력해야 합니다.");
        }
    }

    public static void validate(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }
        if (quantity.compareTo(MAX_QUANTITY) > 0) {
            throw new IllegalArgumentException("수량은 " + MAX_QUANTITY + " 이하로 입력해야 합니다.");
        }
    }

    public static void validateNonNegative(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("수량은 음수일 수 없습니다.");
        }
        if (quantity.compareTo(MAX_QUANTITY) > 0) {
            throw new IllegalArgumentException("수량은 " + MAX_QUANTITY + " 이하로 입력해야 합니다.");
        }
    }
}
