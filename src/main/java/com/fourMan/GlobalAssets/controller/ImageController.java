package com.fourMan.GlobalAssets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Slf4j
@Controller
public class ImageController {
    @GetMapping("/image/{filename}")
    public ResponseEntity<byte[]> proxyImage(@PathVariable String filename) {
        String imageUrl = "https://ap-global-assets-bucket.s3.ap-northeast-2.amazonaws.com/" + filename;
        try {
            UrlResource resource = new UrlResource(imageUrl);

            // 실제 이미지 바이트 읽기
            byte[] bytes = resource.getInputStream().readAllBytes();

            // Content-Type 추정 (단순 예시: 확장자로 판별)
            MediaType mediaType = MediaType.IMAGE_PNG;
            if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else if (filename.toLowerCase().endsWith(".gif")) {
                mediaType = MediaType.IMAGE_GIF;
            }

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(bytes);

        } catch (Exception e) {
            // 에러 로그 찍기
            log.error("Image proxy error for {}: {}", filename, e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
