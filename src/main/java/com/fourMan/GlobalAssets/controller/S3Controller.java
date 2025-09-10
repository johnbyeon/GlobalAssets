package com.fourMan.GlobalAssets.controller;

import com.fourMan.GlobalAssets.dto.AdvertisementDto;
import com.fourMan.GlobalAssets.dto.PresignedUrlResponse;
import com.fourMan.GlobalAssets.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.Duration;

@Controller
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {
    private final AdvertisementService advertisementService;
    private final S3Presigner presigner;

    @PreAuthorize("hasRole('ROLE_ADMIN')") //관리자 권한
    @GetMapping("/upload")
    public String showUploadPage() {
        return "upload"; // upload.html 렌더링
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')") //관리자 권한
    @PostMapping("/presigned-url")
    @ResponseBody
    public PresignedUrlResponse getPresignedUrl(@RequestParam String filename,@RequestParam String linkPath) {
        AdvertisementDto dto = AdvertisementDto.builder()
                .imagePath(filename)
                .linkPath(linkPath)
                .build();
        advertisementService.addAdvertisement(dto);
        // 확장자 추출
        String extension = "";
        if (filename.contains(".")) {
            extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        }

        // Content-Type 매핑
        String contentType = switch (extension) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            case "bmp" -> "image/bmp";
            default -> "application/octet-stream";
        };

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket("ap-global-assets-bucket") // 버킷 이름만!
                .key(filename)
                .contentType(contentType)
                .build();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10)) // Presigned URL 유효기간
                        .putObjectRequest(objectRequest)
                        .build()
        );

        return new PresignedUrlResponse(
                presignedRequest.url().toString(),
                filename,
                contentType
        );
    }


}