package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SectionCoursewareEntity {

    private String sectionId;

    private String coursewareId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
