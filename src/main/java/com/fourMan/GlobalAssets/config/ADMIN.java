package com.fourMan.GlobalAssets.config;

public interface ADMIN {
    String ADMIN_ID = "admin@fm.fm";

    public interface INIT_CRYPTO{
        String CRYPTO = "CRYPTO";
        String[] SYMBOLS = new String[]{"BTC/KRW","SOL/KRW","DOGE/KRW","XRP/KRW","ETC/KRW"};
        String[] NAMES = new String[]{"비트코인","솔라나","도지코인","엑스알피(리플)","이더리움클래식"};
        String[] CODES = new String[]{"KRW-BTC","KRW-SOL","KRW-DOGE","KRW-XRP","KRW-ETC"};
    }

}
