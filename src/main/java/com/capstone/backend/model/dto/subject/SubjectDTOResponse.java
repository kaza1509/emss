package com.capstone.backend.model.dto.subject;

import com.capstone.backend.model.dto.bookseries.BookSeriesDTOResponse;
import com.capstone.backend.model.dto.bookseriesSubject.BookSeriesSubjectDTOResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class SubjectDTOResponse {
    Long id;
    String name;
    boolean active;
    LocalDateTime createdAt;
    @JsonIgnore
    List<BookSeriesDTOResponse> bookSeriesDTOResponse;
    @JsonIgnore
    BookSeriesSubjectDTOResponse bookSeriesSubject;
    String createBy;
}
