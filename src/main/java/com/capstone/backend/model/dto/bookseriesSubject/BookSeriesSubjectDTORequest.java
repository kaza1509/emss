package com.capstone.backend.model.dto.bookseriesSubject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookSeriesSubjectDTORequest {
    @Schema(description = "bookSeriesId is mandatory", example = "1")
    @NotNull(message = "bookSeriesId is mandatory")
    Long bookSeriesId;

    @Schema(description = "subjectId is mandatory", example = "1")
    @NotNull (message = "subjectId is mandatory")
    Long subjectId;
}
