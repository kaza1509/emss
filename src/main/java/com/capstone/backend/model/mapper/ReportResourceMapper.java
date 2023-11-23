package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.ReportResource;
import com.capstone.backend.entity.Resource;
import com.capstone.backend.model.dto.reportresource.ReportResourceDTOResponse;
import com.capstone.backend.model.dto.resource.ResourceReportDTOResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ReportResourceMapper {
    public static List<ReportResourceDTOResponse> toReportResourceDTOResponseList(List<ReportResource> reportResources) {
        return reportResources.stream()
                .map(reportResource -> ReportResourceDTOResponse.builder()
                        .id(reportResource.getId())
                        .message(reportResource.getMessage())
                        .createdAt(reportResource.getCreatedAt())
                        .reporter(UserMapper.toUserDTOResponse(reportResource.getReporter()))
                        .build())
                .collect(Collectors.toList());
    }
    public static ResourceReportDTOResponse toResourceReportDTOResponse(Resource resource) {
        return ResourceReportDTOResponse.builder()
                .id(resource.getId())
                .name(resource.getName())
                .resourceType(resource.getResourceType())
                .createdAt(resource.getCreatedAt())
                .visualType(resource.getVisualType())
                .resourceSrc(resource.getResourceSrc())
                .reportResourceDTOResponse(ReportResourceMapper.toReportResourceDTOResponseList(resource.getReportResourceList()))
                .build();
    }
}
