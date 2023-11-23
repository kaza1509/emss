package com.capstone.backend.repository;

import com.capstone.backend.entity.ReportResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportResourceRepository extends JpaRepository<ReportResource, Long> {
    @Query("select rr from ReportResource  rr where rr.reporter.id = ?1 and rr.active = true")
    public Optional<ReportResource> findByReporterIdActive(Long reporterId);
    @Query("select rr from ReportResource  rr where rr.active=true and rr.resource.id = ?1")
    public Optional<List<ReportResource>> findByResourceId(Long resourceId);
}
