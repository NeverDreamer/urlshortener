package com.Meli4.urlshortener.urlEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "urlEntitiesStats", path = "urlEntitiesStats")
public interface UrlEntityStatsRepository extends JpaRepository<UrlEntityStats, Long> {

    Optional<UrlEntityStats> findByUrlEntityId(Long urlEntityId);

    @Modifying
    @Query("UPDATE UrlEntityStats s SET s.accessCount = s.accessCount + 1 WHERE s.urlEntity.id = :urlId")
    void incrementAccessCount(@Param("urlId") Long urlId);
}
