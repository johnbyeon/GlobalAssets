package com.fourMan.GlobalAssets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApprovalKeyResponseDto {
    @JsonProperty("approval_key")
    private String approvalKey;  // 이게 핵심 키!
    // 다른 필드 있으면 추가 (API 문서 참고)
}
