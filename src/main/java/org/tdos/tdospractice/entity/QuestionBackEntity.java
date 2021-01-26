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

    private String content;

    private String choice;

    private String answer;

    private String picUrl;

    private String modelId;

    private String categoryId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String studentAnswer;

}
