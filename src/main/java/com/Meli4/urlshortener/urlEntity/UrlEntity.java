package com.Meli4.urlshortener.urlEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class UrlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String url;
    private String shortCode;
    private Date createdAt;
    private Date updatedAt;

    @OneToOne(mappedBy = "urlEntity", cascade = CascadeType.ALL)
    @JsonIgnore
    private UrlEntityStats stats;

    public long getId(){
        return id;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getShortCode(){
        return shortCode;
    }

    public void setShortCode(String shortCode){
        this.shortCode = shortCode;
    }

    public Date getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt(){
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt){
        this.updatedAt = updatedAt;
    }

    public UrlEntityStats getStats(){
        return stats;
    }
}
