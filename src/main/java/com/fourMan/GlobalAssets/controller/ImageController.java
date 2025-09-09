package com.fourMan.GlobalAssets.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
@Controller
public class ImageController {
    @GetMapping("/images/{filename}")
    public String getImage(@PathVariable String filename){
        String s3Url = "https://ap-global-assets-bucket.s3.ap-northeast-2.amazonaws.com/" + filename;
        return s3Url;
    }
}
