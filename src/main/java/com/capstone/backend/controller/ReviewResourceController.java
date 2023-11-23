package com.capstone.backend.controller;

import com.capstone.backend.model.dto.reviewResource.MaterialReviewDTOFilter;
import com.capstone.backend.model.dto.reviewResource.MediaReviewDTOFilter;
import com.capstone.backend.model.dto.reviewResource.ReviewResourceDTORequest;
import com.capstone.backend.service.ReviewResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.capstone.backend.utils.Constants.API_VERSION;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/review")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Review resource", description = "API for Review resource")

public class ReviewResourceController {
    ReviewResourceService reviewResourceService;

    @GetMapping("/materials")
    @Operation(summary = "Get list of materials to be reviewed")
    public ResponseEntity<?> searchReviewMaterial(@Valid @ModelAttribute MaterialReviewDTOFilter materialReviewDTOFilter) {
        return ResponseEntity.ok(reviewResourceService.searchMaterial(materialReviewDTOFilter, false));
    }

    @GetMapping("/medias")
    @Operation(summary = "Get list of medias to be reviewed")
    public ResponseEntity<?> searchReviewMedia(@Valid @ModelAttribute MediaReviewDTOFilter mediaReviewDTOFilter) {
        return ResponseEntity.ok(reviewResourceService.searchMedia(mediaReviewDTOFilter, false));
    }

    @GetMapping("/report-materials")
    @Operation(summary = "Get list of reported materials")
    public ResponseEntity<?> searchReportedMaterial(@Valid @ModelAttribute MaterialReviewDTOFilter materialReviewDTOFilter) {
        return ResponseEntity.ok(reviewResourceService.searchMaterial(materialReviewDTOFilter, true));
    }

    @GetMapping("/report-medias")
    @Operation(summary = "Get list of reported medias")
    public ResponseEntity<?> searchReportedMaterial(@Valid @ModelAttribute MediaReviewDTOFilter mediaReviewDTOFilter) {
        return ResponseEntity.ok(reviewResourceService.searchMedia(mediaReviewDTOFilter, true));
    }

    @GetMapping("/report-detail/{id}")
    @Operation(summary = "Get detail of a reported media/material")
    public ResponseEntity<?> getResourceReportById(@Valid @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(reviewResourceService.getResourceReportById(id));
    }

    @PostMapping("/report")
    @Operation(summary = "Send report of a resource to user's email")
    public ResponseEntity<?> reportResource(@Valid @RequestBody ReviewResourceDTORequest request) {
        return ResponseEntity.ok(reviewResourceService.reportResource(request));
    }

    @PostMapping("/approve")
    @Operation(summary = "Approve a resource")
    public ResponseEntity<?> approveResource(@Valid @RequestBody ReviewResourceDTORequest request) {
        return ResponseEntity.ok(reviewResourceService.reviewResource(request, true));
    }

    @PostMapping("/reject")
    @Operation(summary = "Reject a resource")
    public ResponseEntity<?> rejectResource(@Valid @RequestBody ReviewResourceDTORequest request) {
        return ResponseEntity.ok(reviewResourceService.reviewResource(request, false));
    }
}
