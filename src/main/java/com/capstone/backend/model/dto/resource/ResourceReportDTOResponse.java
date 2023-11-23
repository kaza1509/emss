package com.capstone.backend.model.dto.resource;

import com.capstone.backend.entity.type.ResourceType;
import com.capstone.backend.entity.type.VisualType;
import com.capstone.backend.model.dto.reportresource.ReportResourceDTOResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceReportDTOResponse {
    Long id;
    String name;
    String description;
    ResourceType resourceType;
    LocalDateTime createdAt;
    VisualType visualType;
    String resourceSrc;
    List<ReportResourceDTOResponse> reportResourceDTOResponse;
}
