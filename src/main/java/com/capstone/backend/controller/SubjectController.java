package com.capstone.backend.controller;

import com.capstone.backend.model.dto.subject.SubjectDTOFilter;
import com.capstone.backend.model.dto.subject.SubjectDTORequest;
import com.capstone.backend.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/subject")
@Tag(name = "Subject", description = "API for Subject")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class SubjectController {

    SubjectService subjectService;

    @Operation(summary = "create Subject")
    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody SubjectDTORequest request) {
        return ResponseEntity.ok(subjectService.createSubject(request));
    }

    @Operation(summary = "Update subject")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody SubjectDTORequest request,
                                    @PathVariable @NotEmpty Long id) {
        return ResponseEntity.ok(subjectService.updateSubject(id, request));
    }

    @Operation(summary = "changeStatus Subject")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable @NotEmpty Long id) {
        subjectService.changeStatus(id);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Search Subject By Book SeriesId")
    @GetMapping("/display")
    public ResponseEntity<?> searchSubject(@ModelAttribute SubjectDTOFilter subjectDTOFilter) {
        return ResponseEntity.ok(subjectService.searchSubject(subjectDTOFilter));
    }

    @Operation(summary = "View Subject by Id")
    @GetMapping("/{id}")
    public ResponseEntity<?> viewSubject(@PathVariable @NotEmpty Long id) {
        return ResponseEntity.ok(subjectService.viewSubjectById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getListSubjects() {
        return ResponseEntity.ok(subjectService.getListSubjects());
    }

    @GetMapping("/list-by-book-series")
    public ResponseEntity<?> getListSubjectsByBookSeries(@RequestParam(required = false) Long bookSeriesId) {
        return ResponseEntity.ok(subjectService.getListSubjectsByBookSeries(bookSeriesId));
    }


}
