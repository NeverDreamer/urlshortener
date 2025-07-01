package com.Meli4.urlshortener.urlEntity;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "urlEntites", path = "urlEntites")
public interface UrlEntityRepository extends JpaRepository<UrlEntity, Long>, CrudRepository<UrlEntity, Long> {

    List<UrlEntity> findByShortCode(@Param("shortCode") String shortCode);

    @Transactional
    <S extends UrlEntity> S save(S entity);
}
