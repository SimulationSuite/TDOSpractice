package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AssignmentEntity {

    private String id;

    private String sectionId;

    private String name;

    private LocalDateTime endAt;

    private int qualifiedScore;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean canUpdate;

}
