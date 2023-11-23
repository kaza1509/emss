package com.capstone.backend.model.dto.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationDTOResponse {
    @JsonProperty("access_token")
    String accessToken;
//    List<RoleDTODisplay> roleDTOResponses;
}
