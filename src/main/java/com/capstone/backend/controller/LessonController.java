package com.capstone.backend.controller;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.lesson.LessonDTOFilter;
import com.capstone.backend.model.dto.lesson.LessonDTORequest;
import com.capstone.backend.model.dto.lesson.LessonDTOResponse;
import com.capstone.backend.service.LessonService;
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
@RequestMapping(API_VERSION + "/lesson")
@Tag(name = "Lesson", description = "API for Lesson")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class LessonController {
    LessonService lessonService;

    @Operation(summary = "Create Lesson")
    @PostMapping("")
    public ResponseEntity<LessonDTOResponse> create(
            @Valid @RequestBody LessonDTORequest request,
            @RequestParam Long chapterId
    ) {
        return ResponseEntity.ok(lessonService.createLesson(request, chapterId));
    }

    @Operation(summary = "Update Lesson")
    @PutMapping("/{id}")
    public ResponseEntity<LessonDTOResponse> update(@Valid @RequestBody LessonDTORequest request,
                                                    @PathVariable @NotEmpty Long id) {
        LessonDTOResponse lessonDTOResponse = lessonService.updateLesson(id, request);
        return ResponseEntity.ok(lessonDTOResponse);
    }

    @Operation(summary = "Change Status Lesson")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> changeStatus(@PathVariable @NotEmpty Long id) {
        lessonService.changeStatus(id);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Search Lesson By chapterId")
    @GetMapping("/display/{chapterId}")
    public PagingDTOResponse searchLesson(@ModelAttribute LessonDTOFilter lessonDTOFilter,
                                          @PathVariable(name = "chapterId" ) Long chapterId) {
        return lessonService.searchLesson(lessonDTOFilter, chapterId);
    }

    @Operation(summary = "View Lesson by Id")
    @GetMapping("/{id}")
    public ResponseEntity<LessonDTOResponse> viewLesson(@PathVariable @NotEmpty Long id) {
        LessonDTOResponse lessonDTOResponse = lessonService.viewLessonById(id);
        return ResponseEntity.ok(lessonDTOResponse);
    }

    @GetMapping("list-by-chapter")
    public ResponseEntity<?> getListLessonsByChapterId(@RequestParam(required = false) Long chapterId) {
        return ResponseEntity.ok(lessonService.getListLessonsByChapterId(chapterId));
    }
}
