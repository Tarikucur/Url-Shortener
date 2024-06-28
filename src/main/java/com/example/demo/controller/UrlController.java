package com.example.demo.controller;

import com.example.demo.entity.Url;
import com.example.demo.exception.UrlNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.UrlService;

import java.io.IOException;

@RestController
@RequestMapping("/url")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<Url> shortenUrl(@RequestParam("originalUrl") String originalUrl) {
        Url url = urlService.createShortUrl(originalUrl);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/{shortUrl}")
    public void redirect(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String originalUrl = urlService.getOriginalUrl(shortUrl);
        if (originalUrl != null) {
            response.sendRedirect(originalUrl);
        } else {
            throw new UrlNotFoundException("The URL does not exist: " + shortUrl);
        }
    }
}