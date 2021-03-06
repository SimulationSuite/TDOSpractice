package org.tdos.tdospractice.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class StudentAnswerEntity {

    private String id;

    private String questionId;

    private String assignmentId;

    private String userId;

    private String name;

    private String answer;

    private Integer score;

    private Integer status;

    private String committedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
