package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class QuestionBackAssignmentEntity {

    private String assignmentId;

    private String questionId;

    private int order;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
