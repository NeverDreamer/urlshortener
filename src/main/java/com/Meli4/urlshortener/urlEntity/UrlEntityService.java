package com.Meli4.urlshortener.urlEntity;

import com.Meli4.urlshortener.util.ShortenerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

@Service
public class UrlEntityService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random rand = new Random();

    @Autowired
    private UrlEntityRepository urlEntityRepository;
    @Autowired
    private UrlEntityStatsRepository urlEntityStatsRepository;

    @Transactional
    public UrlEntity createShortUrl(String url) {
        Date time = ShortenerUtil.getTimeNow();

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setUrl(url);
        urlEntity.setShortCode(generateUniqueHash());
        urlEntity.setCreatedAt(time);
        urlEntity.setUpdatedAt(time);

        urlEntityRepository.save(urlEntity);

        UrlEntityStats stats = new UrlEntityStats();
        stats.setUrlEntity(urlEntity);
        stats.setAccessCount(0);
        urlEntityStatsRepository.save(stats);

        return urlEntity;
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

        return urlEntityRepository.save(urlEntity);
    }

    @Transactional(readOnly = true)
    public UrlEntity findByShortCode(String shortCode) {
        return urlEntityRepository.findByShortCode(shortCode).orElse(null);
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

    private String generateHash(){
        StringBuilder hashCode = new StringBuilder();
        char[] chars = CHARACTERS.toCharArray();
        for(int i = 0; i < 7; i++){
            hashCode.append(chars[rand.nextInt(chars.length)]);
        }

        String hash = hashCode.toString();
        if(urlEntityRepository.findByShortCode(hash).isPresent()){
            return generateHash();
        }


        return hashCode.toString();
    }

    public String generateUniqueHash(){
        String hash;
        do{
            hash = generateHash();
        }
        while (urlEntityRepository.findByShortCode(hash).isPresent());

        return hash;
    }
}
