package com.capstone.backend.model.dto.reviewResource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResourceDTORequest {
    @NotBlank(message = "{message.not-blank}")
    String message;
    @Schema(example = "1", description = "ResourceId must be integer")
    @NotNull(message = "{resourceId.not-null}")
    Long resourceId;
}
