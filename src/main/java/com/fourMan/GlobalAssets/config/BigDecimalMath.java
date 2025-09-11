package com.fourMan.GlobalAssets.config;

import java.math.BigDecimal;

public class BigDecimalMath {

    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        if (a == null) return b;
        if (b == null) return a;
        return (a.compareTo(b) >= 0) ? a : b;
    }

    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        if (a == null) return b;
        if (b == null) return a;
        return (a.compareTo(b) <= 0) ? a : b;
    }
}
