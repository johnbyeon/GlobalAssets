package com.fourMan.GlobalAssets.service;

import com.fourMan.GlobalAssets.config.ApiConfig;
import com.fourMan.GlobalAssets.dto.ApprovalKeyRequestDto;
import com.fourMan.GlobalAssets.dto.ApprovalKeyResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// 이 클래스는 WebSocket 연결에 필요한 approval_key를 API 서버에서 받아와.
// 중학생 설명: 이 코드는 서버에 전화해서 "실시간 데이터 볼 수 있는 키 줘!"라고 부탁하는 거야.

@Slf4j // 로그를 쉽게 남기기 위한 도구
@Service
public class ApprovalKeyService {

    @Autowired
    private ApiConfig apiConfig; // 키를 읽어오는 클래스

    public String getApprovalKey() {
        RestTemplate restTemplate = new RestTemplate(); // HTTP 요청 보내는 도구
        String url = "https://openapi.koreainvestment.com:9443/oauth2/Approval"; // approval_key 요청 URL

        // 헤더 설정: JSON 형식으로 보낼 거야
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", java.nio.charset.StandardCharsets.UTF_8));
        log.info("Request headers: {}", headers);

        // 요청 데이터: appKey와 secretKey를 넣어
        ApprovalKeyRequestDto requestDto = new ApprovalKeyRequestDto();
        requestDto.setGrantType(apiConfig.getGrantType());
        requestDto.setAppKey(apiConfig.getAppKey());
        requestDto.setSecretKey(apiConfig.getSecretKey());

        // 디버깅: 어떤 데이터를 보내는지 로그로 남김
        log.info("Requesting approval key with body: {}", requestDto);

        HttpEntity<ApprovalKeyRequestDto> request = new HttpEntity<>(requestDto, headers);


        try {
            // 서버에 요청 보내고 응답 받음
            ResponseEntity<ApprovalKeyResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, request, ApprovalKeyResponseDto.class);
            log.info("Approval key response: {}", response.getBody());
            return response.getBody().getApprovalKey(); // 받은 키 반환
        } catch (Exception e) {
            // 에러 나면 로그 남기고 예외 던짐
            log.error("Failed to get approval key: {}", e.getMessage());
            throw new RuntimeException("Failed to get approval key", e);
        }
    }
}