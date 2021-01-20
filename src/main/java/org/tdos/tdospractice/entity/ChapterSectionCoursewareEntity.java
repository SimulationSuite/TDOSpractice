package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChapterSectionCoursewareEntity {

    private String relativeId;

    private String coursewareId;

    private int type;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
