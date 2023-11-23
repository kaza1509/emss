package com.capstone.backend.controller;

import com.capstone.backend.model.dto.bookseriesSubject.BookSeriesSubjectDTOFilter;
import com.capstone.backend.model.dto.bookseriesSubject.ChangeSubjectDTORequest;
import com.capstone.backend.service.BookSeriesSubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/book-series-subject")
@Tag(name = "BookSeriesSubject", description = "API for book series-subject")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class BookSeriesSubjectController {
    BookSeriesSubjectService bookSeriesSubjectService;

    @PostMapping("/change-subject")
    public ResponseEntity<?> changeSubjectInBookSeries(@Valid @RequestBody ChangeSubjectDTORequest request, @RequestParam Long bookSeriesId) {
        return ResponseEntity.ok(bookSeriesSubjectService.changeSubjectInBookSeries(request.getSubjects(), bookSeriesId));
    }

    @Operation(summary = "Search book series-Subject")
    @GetMapping("/display/{bookSeriesId}")
    public ResponseEntity<?> searchBookSeriesSubject(
            @ModelAttribute BookSeriesSubjectDTOFilter bookSeriesSubjectDTOFilter,
            @PathVariable Long bookSeriesId
    ) {
        return ResponseEntity.ok(bookSeriesSubjectService.searchBookSeriesSubject(bookSeriesSubjectDTOFilter, bookSeriesId));
    }
    @Operation(summary = "View book series-Subject by Id")
    @GetMapping
    public ResponseEntity<?> viewBookSeriesSubject(@RequestParam Long bookSeriesId) {
        return ResponseEntity.ok(bookSeriesSubjectService.viewBookSeriesSubjectById(bookSeriesId));
    }
    @Operation(summary = "get list book series-Subject")
    @GetMapping("/list")
    public ResponseEntity<?> getAllBookSeriesSubject() {
        return ResponseEntity.ok(bookSeriesSubjectService.getListBookSeriesSubject());
    }
}
