package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChapterEntity {

    private String id;

    private String name;

    private String introduction;

    private int order;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
