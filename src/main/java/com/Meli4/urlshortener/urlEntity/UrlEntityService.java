package com.Meli4.urlshortener.urlEntity;

import com.Meli4.urlshortener.ShortenerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class UrlEntityService {

    @Autowired
    private UrlEntityRepository urlEntityRepository;
    @Autowired
    private UrlEntityStatsRepository urlEntityStatsRepository;

    @Transactional
    public UrlEntity createShortUrl(String url) {
        Date time = ShortenerUtil.getTimeNow();

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setUrl(url);
        urlEntity.setShortCode(ShortenerUtil.generateHash());
        urlEntity.setCreatedAt(time);
        urlEntity.setUpdatedAt(time);

        return urlEntityRepository.save(urlEntity);
    }

    @Transactional
    public UrlEntity updateShortUrl(String url, String code) {
        UrlEntity urlEntity = this.findByShortCode(code);
        if(urlEntity == null){
            return createShortUrl(url);
        }
        Date time = ShortenerUtil.getTimeNow();

        urlEntity.setUrl(url);
        urlEntity.setShortCode(code);
        urlEntity.setCreatedAt(time);
        urlEntity.setUpdatedAt(time);

        urlEntityRepository.save(urlEntity);

        return urlEntityRepository.save(urlEntity);
    }

    @Transactional(readOnly = true)
    public UrlEntity findByShortCode(String shortCode) {
        return urlEntityRepository.findByShortCode(shortCode).get(0);
    }

    @Transactional(readOnly = true)
    public UrlEntityStats getUrlWithAnalytics(String shortCode) {
        return findByShortCode(shortCode).getStats();
    }

    @Transactional
    public void recordAccess(String shortCode) {
        UrlEntity url = findByShortCode(shortCode);
        urlEntityStatsRepository.incrementAccessCount(url.getId());
    }
}
