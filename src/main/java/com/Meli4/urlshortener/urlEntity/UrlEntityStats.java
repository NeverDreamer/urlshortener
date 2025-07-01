package com.Meli4.urlshortener.urlEntity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class UrlEntityStats {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "url_entity_id")
    private UrlEntity urlEntity;

    private int accessCount;

    public int getAccessCount(){
        return accessCount;
    }

    public void setAccessCount(int accessCount){
        this.accessCount = accessCount;
    }

    public void incrementAccessCount(){
        this.accessCount++;
    }
}
