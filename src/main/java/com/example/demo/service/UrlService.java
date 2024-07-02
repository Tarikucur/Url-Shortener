package com.example.demo.service;

import com.example.demo.entity.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.example.demo.repository.UrlRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    private static final String REDIS_PREFIX = "url:";

    public Url createShortUrl(String originalUrl) {
        String normalizedUrl = normalizeUrl(originalUrl);
        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(normalizedUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get();
        }

        String shortUrl = generateUniqueShortUrl();
        Url newUrl = new Url();
        newUrl.setOriginalUrl(normalizedUrl);
        newUrl.setShortUrl(shortUrl);
        urlRepository.save(newUrl);
        return newUrl;
    }

    @Cacheable(value = "urls", key = "#shortUrl", unless = "#result == null")
    public String getOriginalUrl(String shortUrl) {
        Optional<Url> optionalUrl = urlRepository.findByShortUrl(shortUrl);
        System.out.println(shortUrl);
        System.out.println(optionalUrl.map(Url::getOriginalUrl));
        return optionalUrl.map(Url::getOriginalUrl).orElse(null);
    }

    public String generateUniqueShortUrl() {
        String shortUrl;
        Optional<Url> existingUrl;
        do {
            shortUrl = UUID.randomUUID().toString().substring(0, 6);
            existingUrl = urlRepository.findByShortUrl(shortUrl);
        } while (existingUrl.isPresent());
        return shortUrl;
    }

    public String normalizeUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            if (host != null && (host.startsWith("www."))) {
                host = host.substring(4);
            }
            URI normalizedUri = new URI(uri.getScheme(), host, uri.getPath(), uri.getQuery(), uri.getFragment());
            return normalizedUri.toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URL format");
        }
    }
}