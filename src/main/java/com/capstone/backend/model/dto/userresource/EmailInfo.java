package com.capstone.backend.model.dto.userresource;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailInfo {
    String to;
    String subject;
    String content;
}
