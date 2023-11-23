package com.capstone.backend.service;

import com.capstone.backend.model.dto.resource.PagingResourceDTOResponse;
import com.capstone.backend.model.dto.resource.ResourceDTOResponse;
import com.capstone.backend.model.dto.resource.ResourceReportDTOResponse;
import com.capstone.backend.model.dto.reviewResource.MaterialReviewDTOFilter;
import com.capstone.backend.model.dto.reviewResource.MediaReviewDTOFilter;
import com.capstone.backend.model.dto.reviewResource.ReviewResourceDTORequest;

public interface ReviewResourceService {
    ResourceDTOResponse reviewResource(ReviewResourceDTORequest request, boolean isAccepted);
    PagingResourceDTOResponse searchMaterial(MaterialReviewDTOFilter materialReviewDTOFilter, boolean isReport);
    Object searchMedia(MediaReviewDTOFilter mediaReviewDTOFilter, boolean isReport);
    ResourceReportDTOResponse getResourceReportById(Long id);
    ResourceReportDTOResponse reportResource(ReviewResourceDTORequest request);
}
