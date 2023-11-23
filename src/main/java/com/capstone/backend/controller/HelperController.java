package com.capstone.backend.controller;

import com.capstone.backend.entity.type.VisualType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/helper")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Helper", description = "API for Helper")
@CrossOrigin
public class HelperController {

    @GetMapping("/list-visual-type")
    public ResponseEntity<?> getListVisualType() {
        return ResponseEntity.ok(VisualType.values());
    }

}
