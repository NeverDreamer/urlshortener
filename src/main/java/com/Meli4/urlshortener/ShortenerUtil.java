package com.Meli4.urlshortener;

import com.Meli4.urlshortener.urlEntity.UrlEntity;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Random;

public class ShortenerUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random rand = new Random();

    public static String generateHash(){
        StringBuilder hashCode = new StringBuilder();
        char[] chars = CHARACTERS.toCharArray();
        for(int i = 0; i < 7; i++){
            hashCode.append(chars[rand.nextInt(chars.length)]);
        }
        return hashCode.toString();
    }

    public static ResponseEntity<UrlEntity> checkUrl(String url) {
        try {
            URL httpurl = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) httpurl.openConnection();

            int responseCode = huc.getResponseCode();

            if(HttpURLConnection.HTTP_OK != responseCode){
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return null;
    }

    public static Date getTimeNow(){
        return Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.of("Z")));
    }
}
