package com.fourMan.GlobalAssets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresignedUrlResponse {
    private String url;
    private String filename;
    private String contentType;
}
