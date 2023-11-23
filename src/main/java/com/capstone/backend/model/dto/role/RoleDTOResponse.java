package com.capstone.backend.model.dto.role;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDTOResponse {
    Long roleId;
    String roleName;
    String description;
    LocalDateTime createdAt;
    Boolean active;
    String creator;
}
