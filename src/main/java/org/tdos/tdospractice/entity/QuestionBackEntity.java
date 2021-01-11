package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

public class QuestionBackEntity {

    private String id;

    private int type;

    private String answer;

    private String question;

    private String modelId;

    private String categoryId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
