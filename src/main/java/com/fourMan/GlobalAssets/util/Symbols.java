package com.fourMan.GlobalAssets.util;

public final class Symbols {
    private Symbols(){}

    // KRW-BTC -> BTCKRW
    public static String toInternal(String upbitMarket) {
        if (upbitMarket == null || !upbitMarket.contains("-")) return upbitMarket;
        String[] p = upbitMarket.split("-");
        return p[1] + p[0];
    }

    // BTCKRW -> KRW-BTC
    public static String toUpbit(String internal) {
        if (internal == null || internal.length() < 6) return internal;
        String base = internal.substring(internal.length()-3);
        String coin = internal.substring(0, internal.length()-3);
        return base + "-" + coin;
    }
}
