package com.fourMan.GlobalAssets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data  // Lombok으로 getter/setter 자동 생성
public class ApprovalKeyRequestDto {
    @JsonProperty("grant_type")
    private String grantType;
    @JsonProperty("appkey")// "client_credentials"
    private String appKey;
    @JsonProperty("secretkey")
    private String secretKey;
}
