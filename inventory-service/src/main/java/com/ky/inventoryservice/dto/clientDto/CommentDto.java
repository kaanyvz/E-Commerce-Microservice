package com.ky.inventoryservice.dto.clientDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private UUID id;
    private String createdBy;
    private LocalDateTime createdDate;
    private String text;
    private String creator;
}
