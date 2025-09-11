package com.fourMan.GlobalAssets.config;

public interface ADMIN {
    String ADMIN_ID = "admin@fm.fm";

    interface INIT_CRYPTO {
        String CATEGORY = "CRYPTO";
        String[] SYMBOLS = new String[]{"BTC/KRW", "SOL/KRW", "DOGE/KRW", "XRP/KRW", "ETC/KRW"};
        String[] NAMES = new String[]{"비트코인", "솔라나", "도지코인", "엑스알피(리플)", "이더리움클래식"};
        String[] CODES = new String[]{"KRW-BTC", "KRW-SOL", "KRW-DOGE", "KRW-XRP", "KRW-ETC"};
        String[] EN_SYMBOLS = new String[]{"BTC", "SOL", "DOGE", "XRP", "ETC"};
    }

    interface INIT_STOCK {

        String KR_TRID = "H0STCNT0";
        String CATEGORY = "STOCK";
        String[] TR_ID = new String[]{"H0STCNT0", "H0STCNT0", "H0STCNT0"};
        String[] SYMBOLS = new String[]{"SAMSUNG", "SK", "LG"};
        String[] NAMES = new String[]{"삼성전자", "SK하이닉스", "LG에너지솔루션"};
        String[] CODES = new String[]{"005930", "000660", "373220"};
        String[] EN_SYMBOLS = new String[]{"SAMSUNG", "SK", "LG"};
    }
    interface INIT_EN_STOCK {
        //    <미국 야간거래/아시아 주간거래 - 무료시세>
        //    D+시장구분(3자리)+종목코드
        //    예) DNASAAPL : D+NAS(나스닥)+AAPL(애플)
        //[시장구분]
        //    NYS : 뉴욕, NAS : 나스닥, AMS : 아멕스 ,
        //    TSE : 도쿄, HKS : 홍콩,
        //    SHS : 상해, SZS : 심천
        //    HSX : 호치민, HNX : 하노이
        //
        //<미국 야간거래/아시아 주간거래 - 유료시세>
        //            ※ 유료시세 신청시에만 유료시세 수신가능
        //"포럼 > FAQ > 해외주식 유료시세 신청방법" 참고
        //    R+시장구분(3자리)+종목코드
        //    예) RNASAAPL : R+NAS(나스닥)+AAPL(애플)
        //[시장구분]
        //    NYS : 뉴욕, NAS : 나스닥, AMS : 아멕스 ,
        //    TSE : 도쿄, HKS : 홍콩,
        //    SHS : 상해, SZS : 심천
        //    HSX : 호치민, HNX : 하노이
        //
        //<미국 주간거래>
        //    R+시장구분(3자리)+종목코드
        //    예) RBAQAAPL : R+BAQ(나스닥)+AAPL(애플) NVDA : 엔비디아, TSLA: 테슬라
        //[시장구분]
        //    BAY : 뉴욕(주간), BAQ : 나스닥(주간). BAA : 아멕스(주간)
        String EN_TRID = "HDFSCNT0";
        String CATEGORY = "STOCK";
        String[] TR_ID = new String[]{"HDFSCNT0", "HDFSCNT0", "HDFSCNT0"};
        String[] SYMBOLS = new String[]{"AAPL", "NVDA", "TSLA"};
        String[] NAMES = new String[]{"애플","엔비디아","테슬라"};
        String[] CODES = new String[]{"RBAQAAPL", "RBAQNVDA", "RBAQTSLA"};
        String[] EN_SYMBOLS = new String[]{"AAPL", "NVDA", "TSLA"};
    }


}
