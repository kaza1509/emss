package com.capstone.backend.controller;

import com.capstone.backend.model.dto.resourcetag.ResourceTagDTOResponse;
import com.capstone.backend.model.dto.resourcetag.ResourceTagDTOUpdate;
import com.capstone.backend.model.dto.resourcetag.ResourceTagViewDTORequest;
import com.capstone.backend.service.ResourceTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(API_VERSION + "/resource-tags")
@Tag(name = "Resource-tag", description = "API for Resource's tags")
@CrossOrigin
public class ResourceTagController {
    ResourceTagService resourceTagService;

    @Operation(summary = "Display all tags of a resource-object table by table name and detail id")
    @PostMapping("")
    public List<ResourceTagDTOResponse> getAllResourceTagByTableTypeAndID(@RequestBody ResourceTagViewDTORequest request) {
        return resourceTagService.getAllResourceTagByTableTypeAndID(request.getTableType(), request.getDetailId());
    }

    @Operation(summary = "Update a resource tag (Add, delele)")
    @PutMapping("")
    public ResponseEntity<?> updateResourceTag(@RequestBody ResourceTagDTOUpdate request) {
        return ResponseEntity.ok(resourceTagService.updateResourceTag(request));
    }
}
