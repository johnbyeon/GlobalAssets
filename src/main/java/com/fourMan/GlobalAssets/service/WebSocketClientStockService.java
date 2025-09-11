package com.fourMan.GlobalAssets.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourMan.GlobalAssets.config.ADMIN;
import com.fourMan.GlobalAssets.config.ApiConfig;
import com.fourMan.GlobalAssets.dto.*;
import jakarta.websocket.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.net.URI;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Service
@ClientEndpoint
public class WebSocketClientStockService {
    @Autowired
    private ApiConfig apiConfig;
    public static final String KRX_INDEX_TR_KEY = "373220"; //005930 : 삼성전자 000660 : sk하이닉스 373220: LG에너지 솔루션

    private Session session;
    private static Map<String, WebSocketStockResponseDto> maps = new HashMap<String,WebSocketStockResponseDto>();
    @Autowired
    AssetService assetService;

    @Autowired
    PricesService pricesService;

    @Autowired
    private ObjectMapper objectMapper;


    public void connect(String socketServerDomain, String connectUrl) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String uri = socketServerDomain + connectUrl + "?approval_key=" + apiConfig.getApprovalKey();
            log.info("WebSocket 연결 시도 중: {}", uri);

            container.connectToServer(this, new URI(uri));

            log.info("WebSocket 서버에 연결됨: {}", uri);
        } catch (Exception e) {
            log.error("WebSocket 연결 실패: {}", e.getMessage());
            throw new RuntimeException("WebSocket 연결 실패", e);
        }


    }

    @OnOpen                   //trId: H0STCNT0, trKey : 005930(삼성전자)
    public void onOpen(Session session) {
        this.session = session;
        log.info("WebSocket 연결 성공: {}", session.getId());
//        sendSubscribeRequest(KRX_INDEX_TR_ID, KRX_INDEX_TR_KEY);
        //            // 국내 종목 여러 개 구독 요청
        for (int i = 0; i < ADMIN.INIT_STOCK.CODES.length; i++) {

            sendSubscribeRequest(ADMIN.INIT_STOCK.KR_TRID, ADMIN.INIT_STOCK.CODES[i]);
            maps.put(ADMIN.INIT_STOCK.CODES[i],new WebSocketStockResponseDto());
            log.info("{} 구독시작 : ", ADMIN.INIT_STOCK.CODES[i]);
        }
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("실시간 데이터 받음 (원문): {}", message);

        if (message.contains("SUBSCRIBE SUCCESS")) {
            log.info("구독 성공: {}", message);
        } else {
            List<WebSocketStockResponseDto> dtos = parseMessage(message);
                for (WebSocketStockResponseDto dto : dtos) {
                    log.info("파싱된 데이터: 종목 코드 = {}, 현재가 = {}, 시간 = {}, 변동폭 = {}, 변동률 = {}, 거래량 = {}, ... (전체 DTO: {})",
                            dto.getMkscShrnIscd(), dto.getStckPrpr(), dto.getStckCntgHour(), dto.getPrdyVrss(), dto.getPrdyCtrt(), dto.getAcmlVol(), dto);
                    //min dto 객체를 하나 만들어낸다 비어있다면 초기 데이터를 넣는다.
                    WebSocketStockResponseDto minDto = maps.get(dto.getMkscShrnIscd());
                    Long nowPrices = Long.valueOf(dto.getStckPrpr());
                    String now = normalizeToMinute(dto.getStckCntgHour());
                    if(ObjectUtils.isEmpty(minDto)){
                        minDto.setMkscShrnIscd(dto.getMkscShrnIscd());
                        //시간 분단위 자르기 위해 NOW변수사용
                        minDto.setStckCntgHour(now);
                        //날짜
                        minDto.setBsopDate(dto.getBsopDate());
                        //최고가
                        minDto.setStckHgpr(dto.getStckPrpr());
                        //최저가
                        minDto.setStckLwpr(dto.getStckPrpr());
                        //시작가
                        minDto.setStckOprc(dto.getStckPrpr());
                        //현재가
                        minDto.setStckPrpr(dto.getStckPrpr());
                    }else{
                        //날짜가 같은가?
                        if(minDto.getBsopDate().equals(dto.getBsopDate())){
                            //시간이 같은가?
                            if(minDto.getStckCntgHour().equals(now)){

                                if(Long.parseLong(minDto.getStckHgpr()) < nowPrices){
                                    //최고 값이보다 현재값이 크면 넣기
                                    minDto.setStckHgpr(dto.getStckPrpr());
                                } else if(Long.parseLong(minDto.getStckLwpr()) > nowPrices){
                                    //최저 값보다 현재값이 작으면 넣기
                                    minDto.setStckLwpr(dto.getStckPrpr());
                                }
                                //현재가
                                minDto.setStckPrpr(dto.getStckPrpr());
                            }else{
                                //날짜가 다르면 데이터가 변한것이니까 저장 후 초기화
                                SavePriceAndInitNow(minDto,dto);
                            }
                        }
                        //날짜가 다르면 데이터가 변한것이니까 저장 후 초기화
                        else {
                            SavePriceAndInitNow(minDto,dto);
                        }

                    }
                }
                if (dtos.isEmpty()) {
                    log.warn("데이터 파싱 실패 또는 유효하지 않은 형식: {}", message);
                }
        }
    }
    public void SavePriceAndInitNow(WebSocketStockResponseDto minDto,WebSocketStockResponseDto nowDto){
        List<AssetsDto> dtoList = assetService.findAllByCode(nowDto.getStckPrpr());
        if(ObjectUtils.isEmpty(dtoList))
        {
            log.error("분봉데이터 저장실패 assets값 없음 ");
            return;
        }
        PricesDto price = PricesDto.builder()
                .assetId(dtoList.get(0).getId())
                .timestamp(toTimestamp(nowDto.getBsopDate(),nowDto.getStckCntgHour()))
                .high(BigDecimal.valueOf(Long.parseLong(nowDto.getStckHgpr())))
                .low(BigDecimal.valueOf(Long.parseLong(nowDto.getStckLwpr())))
                .open(BigDecimal.valueOf(Long.parseLong(nowDto.getStckOprc())))
                .close(BigDecimal.valueOf(Long.parseLong(nowDto.getStckPrpr())))
                .build();
        //dto저장
        String now = normalizeToMinute(nowDto.getStckCntgHour());
        pricesService.insertPrices(price);
        //새로운 분봉 데이터 저장
        minDto.setMkscShrnIscd(nowDto.getMkscShrnIscd());
        //시간
        minDto.setStckCntgHour(now);
        //날짜
        minDto.setBsopDate(nowDto.getBsopDate());
        //최고가
        minDto.setStckHgpr(nowDto.getStckPrpr());
        //최저가
        minDto.setStckLwpr(nowDto.getStckPrpr());
        //시작가
        minDto.setStckOprc(nowDto.getStckPrpr());
        //현재가
        minDto.setStckPrpr(nowDto.getStckPrpr());
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        log.info("WebSocket 연결 종료: {}", reason);
        session = null;
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("WebSocket 에러: {}", throwable.getMessage());
    }
    public static String normalizeToMinute(String time) {
        if (time == null || time.length() != 6) {
            throw new IllegalArgumentException("시간 형식이 잘못되었습니다: " + time);
        }
        // 앞 4자리(HHmm) + "00"
        return time.substring(0, 4) + "00";
    }
    public static Timestamp toTimestamp(String bsopDate, String time) {
        // 초는 잘라내고 분까지만 사용 ("151905" -> "1519")
        String hhmm = time.substring(0, 4);  // "1519"

        // 날짜 + 시분 문자열 ("202509111519")
        String dateTimeStr = bsopDate + hhmm;

        // yyyyMMddHHmm 패턴 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        // LocalDateTime 변환 (초는 자동으로 00초 처리됨)
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, formatter);

        // DB 저장용 Timestamp
        return Timestamp.valueOf(localDateTime);
    }

    public void sendSubscribeRequest(String trId, String trKey) {
        WebSocketSubscribeRequestDto requestDto = new WebSocketSubscribeRequestDto();
        WebSocketSubscribeRequestDto.Header header = new WebSocketSubscribeRequestDto.Header();
        header.setApprovalKey(apiConfig.getApprovalKey());
        requestDto.setHeader(header);

        WebSocketSubscribeRequestDto.Body body = new WebSocketSubscribeRequestDto.Body();
        WebSocketSubscribeRequestDto.Body.Input input = new WebSocketSubscribeRequestDto.Body.Input();
        input.setTrId(trId);
        input.setTrKey(trKey);
        body.setInput(input);
        requestDto.setBody(body);

        try {
            String jsonMessage = objectMapper.writeValueAsString(requestDto);
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(jsonMessage);
                log.info("구독 요청 보냄: {}", jsonMessage);
            }
        } catch (Exception e) {
            log.error("구독 요청 실패: {}", e.getMessage());
        }
    }
    private List<OverseasStockRealtimeDTO> EN_parseMessage(String message) {
        List<OverseasStockRealtimeDTO> dtos = new ArrayList<>();
        try {
            String[] parts = message.split("\\|", 4);
            if (parts.length < 4 || !"0".equals(parts[0])) {
                return dtos; // 암호화 0이 아니거나 형식 오류
            }

            String trId = parts[1];
            int count = Integer.parseInt(parts[2]);
            String dataBlock = parts[3];

            String[] fields = dataBlock.split("\\^");
            int expectedLength = count * 25;
            if (fields.length < expectedLength) {
                log.warn("필드 부족: 실제 {}개, 기대 {}개", fields.length, expectedLength);
                return dtos;
            }

            for (int i = 0; i < count; i++) {
                int start = i * 26;
                if (start + 25 >= fields.length) {
                    log.warn("인덱스 초과 방지: start = {}, fields.length = {}", start, fields.length);
                    break;
                }

                OverseasStockRealtimeDTO dto = new OverseasStockRealtimeDTO();
                dto.setRsym(getField(fields, start + 0));
                dto.setSymb(getField(fields, start + 1));
                dto.setZdiv(getField(fields, start + 2));
                dto.setTymd(getField(fields, start + 3));
                dto.setXhms(getField(fields, start + 4));
                dto.setXymd(getField(fields, start + 5));
                dto.setKymd(getField(fields, start + 6));
                dto.setKhms(getField(fields, start + 7));
                dto.setOpen(getField(fields, start + 8));
                dto.setHigh(getField(fields, start + 9));
                dto.setLow(getField(fields, start + 10));
                dto.setLast(getField(fields, start + 11));
                dto.setSign(getField(fields, start + 12));
                dto.setDiff(getField(fields, start + 13));
                dto.setRate(getField(fields, start + 14));
                dto.setPbid(getField(fields, start + 15));
                dto.setPask(getField(fields, start + 16));
                dto.setVbid(getField(fields, start + 17));
                dto.setVask(getField(fields, start + 18));
                dto.setEvol(getField(fields, start + 19));
                dto.setTvol(getField(fields, start + 20));
                dto.setTamt(getField(fields, start + 21));
                dto.setBivl(getField(fields, start + 22));
                dto.setAsvl(getField(fields, start + 23));
                dto.setStrn(getField(fields, start + 24));
                dto.setMtyp(getField(fields, start + 25));
                dtos.add(dto);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            log.error("파싱 중 에러: {}", e.getMessage());
        }
        return dtos;
    }
    private List<WebSocketStockResponseDto> parseMessage(String message) {
        List<WebSocketStockResponseDto> dtos = new ArrayList<>();
        try {
            String[] parts = message.split("\\|", 4);
            if (parts.length < 4 || !"0".equals(parts[0])) {
                return dtos; // 암호화 0이 아니거나 형식 오류
            }

            String trId = parts[1];
            int count = Integer.parseInt(parts[2]);
            String dataBlock = parts[3];

            String[] fields = dataBlock.split("\\^");
            int expectedLength = count * 46;
            if (fields.length < expectedLength) {
                log.warn("필드 부족: 실제 {}개, 기대 {}개", fields.length, expectedLength);
                return dtos;
            }

            for (int i = 0; i < count; i++) {
                int start = i * 46;
                if (start + 45 >= fields.length) {
                    log.warn("인덱스 초과 방지: start = {}, fields.length = {}", start, fields.length);
                    break;
                }

                WebSocketStockResponseDto dto = new WebSocketStockResponseDto();
                dto.setMkscShrnIscd(getField(fields, start + 0));
                dto.setStckCntgHour(getField(fields, start + 1));
                dto.setStckPrpr(getField(fields, start + 2));
                dto.setPrdyVrssSign(getField(fields, start + 3));
                dto.setPrdyVrss(getField(fields, start + 4));
                dto.setPrdyCtrt(getField(fields, start + 5));
                dto.setWghnAvrgStckPrc(getField(fields, start + 6));
                dto.setStckOprc(getField(fields, start + 7));
                dto.setStckHgpr(getField(fields, start + 8));
                dto.setStckLwpr(getField(fields, start + 9));
                dto.setAskp1(getField(fields, start + 10));
                dto.setBidp1(getField(fields, start + 11));
                dto.setCntgVol(getField(fields, start + 12));
                dto.setAcmlVol(getField(fields, start + 13));
                dto.setAcmlTrPbmn(getField(fields, start + 14));
                dto.setSelnCntgCsnu(getField(fields, start + 15));
                dto.setShnuCntgCsnu(getField(fields, start + 16));
                dto.setNtbyCntgCsnu(getField(fields, start + 17));
                dto.setCttr(getField(fields, start + 18));
                dto.setSelnCntgSmtn(getField(fields, start + 19));
                dto.setShnuCntgSmtn(getField(fields, start + 20));
                dto.setCcldDvsn(getField(fields, start + 21));
                dto.setShnuRate(getField(fields, start + 22));
                dto.setPrdyVolVrssAcmlVolRate(getField(fields, start + 23));
                dto.setOprcHour(getField(fields, start + 24));
                dto.setOprcVrssPrprSign(getField(fields, start + 25));
                dto.setOprcVrssPrpr(getField(fields, start + 26));
                dto.setHgprHour(getField(fields, start + 27));
                dto.setHgprVrssPrprSign(getField(fields, start + 28));
                dto.setHgprVrssPrpr(getField(fields, start + 29));
                dto.setLwprHour(getField(fields, start + 30));
                dto.setLwprVrssPrprSign(getField(fields, start + 31));
                dto.setLwprVrssPrpr(getField(fields, start + 32));
                dto.setBsopDate(getField(fields, start + 33));
                dto.setNewMkopClsCode(getField(fields, start + 34));
                dto.setTrhtYn(getField(fields, start + 35));
                dto.setAskpRsqn1(getField(fields, start + 36));
                dto.setBidpRsqn1(getField(fields, start + 37));
                dto.setTotalAskpRsqn(getField(fields, start + 38));
                dto.setTotalBidpRsqn(getField(fields, start + 39));
                dto.setVolTnrt(getField(fields, start + 40));
                dto.setPrdySmnsHourAcmlVol(getField(fields, start + 41));
                dto.setPrdySmnsHourAcmlVolRate(getField(fields, start + 42));
                dto.setHourClsCode(getField(fields, start + 43));
                dto.setMrktTrtmClsCode(getField(fields, start + 44));
                dto.setViStndPrc(getField(fields, start + 45));

                dtos.add(dto);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            log.error("파싱 중 에러: {}", e.getMessage());
        }
        return dtos;
    }

    private String getField(String[] fields, int index) {
        if (index < fields.length) {
            return fields[index].trim();
        }
        return null;
    }

    public void disconnect() {


           if (session != null && session.isOpen()) {
               try {
                   session.close();
                   log.info("WebSocket 연결 끊음");
               } catch (Exception e) {
                   log.error("연결 종료 실패: {}", e.getMessage());
               }
           }


    }
}