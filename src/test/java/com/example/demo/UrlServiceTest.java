package com.example.demo;

import com.example.demo.entity.Url;
import com.example.demo.repository.UrlRepository;
import com.example.demo.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateShortUrl_newUrl() {
        String originalUrl = "http://example.com";
        String normalizedUrl = "http://example.com";
        Url newUrl = new Url();
        newUrl.setOriginalUrl(normalizedUrl);

        when(urlRepository.findByOriginalUrl(normalizedUrl)).thenReturn(Optional.empty());
        when(urlRepository.save(newUrl)).thenReturn(newUrl);
        Url createdUrl = urlService.createShortUrl(originalUrl);
        assertThat(createdUrl).isNotNull();
        assertThat(createdUrl.getOriginalUrl()).isEqualTo(normalizedUrl);
        assertThat(createdUrl.getShortUrl()).isNotNull().hasSize(6);
    }


    @Test
    void testCreateShortUrl_existingUrl() {
        String originalUrl = "http://example.com";
        String normalizedUrl = "http://example.com";
        Url existingUrl = new Url();
        existingUrl.setOriginalUrl(normalizedUrl);

        when(urlRepository.findByOriginalUrl(normalizedUrl)).thenReturn(Optional.of(existingUrl));
        Url createdUrl = urlService.createShortUrl(originalUrl);
        assertThat(createdUrl).isNotNull();
        assertThat(createdUrl.getOriginalUrl()).isEqualTo(normalizedUrl);
    }

    @Test
    void testGetOriginalUrl_existingShortUrl() {
        String shortUrl = "abcdef";
        String originalUrl = "http://example.com";
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(shortUrl);

        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(url));

        String retrievedUrl = urlService.getOriginalUrl(shortUrl);

        assertThat(retrievedUrl).isNotNull();
        assertThat(retrievedUrl).isEqualTo(originalUrl);
    }

    @Test
    void testGetOriginalUrl_nonExistingShortUrl() {
        String shortUrl = "nonexistent";

        when(urlRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());
        String retrievedUrl = urlService.getOriginalUrl(shortUrl);
        assertThat(retrievedUrl).isNull();
    }
}