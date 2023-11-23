package com.capstone.backend.model.dto.reportresource;

import com.capstone.backend.model.dto.user.UserDTOResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResourceDTOResponse {
    Long id;
    String message;
    LocalDateTime createdAt;
    UserDTOResponse reporter;
}
