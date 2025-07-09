package com.Meli4.urlshortener;

import com.Meli4.urlshortener.urlEntity.UrlEntity;
import com.Meli4.urlshortener.urlEntity.UrlEntityRepository;
import com.Meli4.urlshortener.urlEntity.UrlEntityService;
import com.Meli4.urlshortener.urlEntity.UrlEntityStats;
import com.Meli4.urlshortener.util.ShortenerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ShortenerController {

    @Autowired
    private UrlEntityRepository urlEntityRepository;
    @Autowired
    private UrlEntityService urlEntityService;

    @PostMapping("/shorten")
    public ResponseEntity<UrlEntity> createShortUrl(@RequestParam(value = "url") String url){
        ResponseEntity<UrlEntity> response = ShortenerUtil.checkUrl(url);
        if (response != null) return response;

        return ResponseEntity.ok().body(urlEntityService.createShortUrl(url));
    }

    @GetMapping("/shorten/{code}")
    public ResponseEntity<UrlEntityStats> getShortUrl(@PathVariable String code){
        UrlEntity urlEntity = urlEntityService.findByShortCode(code);
        if(urlEntity == null){
            return ResponseEntity.notFound().build();
        }

        UrlEntityStats stats = urlEntityService.getUrlWithAnalytics(code);
        if(stats == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(stats);
    }

    @PostMapping("/shorten/{code}")
    public ResponseEntity<UrlEntity> updateShortUrl(@PathVariable String code, @RequestParam(value = "url") String url){
        ResponseEntity<UrlEntity> response = ShortenerUtil.checkUrl(url);
        if (response != null) return response;

        return ResponseEntity.ok().body(urlEntityService.updateShortUrl(url, code));
    }

    @GetMapping("/shortify/{code}")
    public ModelAndView redirectWithUsingRedirectPrefix(@PathVariable String code) {
        UrlEntity urlEntity = urlEntityService.findByShortCode(code);
        if(urlEntity == null){
            return new ModelAndView();
        }

        urlEntityService.recordAccess(code);

        return new ModelAndView("redirect:" + urlEntity.getUrl());
    }
}
