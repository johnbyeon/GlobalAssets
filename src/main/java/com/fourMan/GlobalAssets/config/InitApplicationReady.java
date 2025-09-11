package com.fourMan.GlobalAssets.config;
import com.fourMan.GlobalAssets.dto.AssetsDto;
import com.fourMan.GlobalAssets.service.AssetService;
import com.fourMan.GlobalAssets.service.WebSocketClientStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fourMan.GlobalAssets.scheduler.UpbitCandleScheduler.cryptoAssetsId;

@Slf4j
@Component
@RequiredArgsConstructor
// 앱시작시 로딩이 다끝나면 initialize
public class InitApplicationReady implements ApplicationListener<ApplicationReadyEvent> {
    public static final String KOREA_STOCK_WS_DOMAIN = "ws://ops.koreainvestment.com:21000";
    public static final String KOREA_STOCK_WS_URL = "/tryitout/";
    private final AssetService assetService;
    private final WebSocketClientStockService stockService;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {


        for(int i=0;i<ADMIN.INIT_CRYPTO.SYMBOLS.length;i++){
            AssetsDto dto = assetService.findOneSymbol(ADMIN.INIT_CRYPTO.SYMBOLS[i]);
          if(ObjectUtils.isEmpty(dto)){
              AssetsDto assetsdto =new AssetsDto();
                assetsdto.setCode(ADMIN.INIT_CRYPTO.CODES[i]);
                assetsdto.setSymbol(ADMIN.INIT_CRYPTO.SYMBOLS[i]);
                assetsdto.setName(ADMIN.INIT_CRYPTO.NAMES[i]);
              assetsdto.setCategory(ADMIN.INIT_CRYPTO.CATEGORY);
              assetService.insertAssets(assetsdto);
          }
        }
        for(int i=0;i<ADMIN.INIT_STOCK.SYMBOLS.length;i++){
            AssetsDto dto = assetService.findOneSymbol(ADMIN.INIT_STOCK.SYMBOLS[i]);
            if(ObjectUtils.isEmpty(dto)){
                AssetsDto assetsdto =new AssetsDto();
                assetsdto.setCode(ADMIN.INIT_STOCK.CODES[i]);
                assetsdto.setSymbol(ADMIN.INIT_STOCK.SYMBOLS[i]);
                assetsdto.setName(ADMIN.INIT_STOCK.NAMES[i]);
                assetsdto.setCategory(ADMIN.INIT_STOCK.CATEGORY);
                assetService.insertAssets(assetsdto);
            }
        }

        if (ObjectUtils.isEmpty(cryptoAssetsId)){
            log.info("cryptoAssetsId: 비었음");
            List<AssetsDto> assetsDtos =  assetService.findAllByCategory(ADMIN.INIT_CRYPTO.CATEGORY);
            if(!ObjectUtils.isEmpty(assetsDtos)) {
                assetsDtos.forEach(x ->
                {
                    switch (x.getName()) {
                        case "비트코인":
                            cryptoAssetsId.put("KRW-BTC", x.getId());
                            break;
                        case "솔라나":
                            cryptoAssetsId.put("KRW-SOL", x.getId());
                            break;
                        case "도지코인":
                            cryptoAssetsId.put("KRW-DOGE", x.getId());
                            break;
                        case "엑스알피(리플)":
                            cryptoAssetsId.put("KRW-XRP", x.getId());
                            break;
                        case "이더리움클래식":
                            cryptoAssetsId.put("KRW-ETC", x.getId());
                            break;
                    }
                });
                log.info("cryptoAssetsId: {}", cryptoAssetsId);
            }
        }


    }
    public void OpenWebSocketStock(){
        try {
            // 세션 1개만 연결 (국내 전용)
            stockService.connect(KOREA_STOCK_WS_DOMAIN, KOREA_STOCK_WS_URL+ADMIN.INIT_STOCK.KR_TRID);

            log.info( "국내주식 WebSocket 연결 및 구독 시작!");
        } catch (Exception e) {
            log.error("국내주식 WebSocket 연결 실패: {}", e.getMessage());

        }
    }
}