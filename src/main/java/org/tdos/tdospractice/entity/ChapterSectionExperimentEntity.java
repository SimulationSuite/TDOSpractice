package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChapterSectionExperimentEntity {

    private String id;

    private String section_id;

    private String experiment_id;

    private LocalDateTime createdAt;
}
