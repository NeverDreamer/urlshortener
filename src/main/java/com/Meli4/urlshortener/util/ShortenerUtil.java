package com.Meli4.urlshortener.util;

import com.Meli4.urlshortener.urlEntity.UrlEntity;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public class ShortenerUtil {

    public static ResponseEntity<UrlEntity> checkUrl(String url) {
       /* try {
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
        }*/

        try {
            new URL(url).toURI();
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
        return null;
    }

    public static Date getTimeNow(){
        return Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.of("Z")));
    }
}
