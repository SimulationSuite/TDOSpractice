package org.tdos.tdospractice.type;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class StudentQuestionAnswer {

    private String id;

    private int type;

    private String content;

    private String choice;

    private String answer;

    private String picUrl;

    private String modelId;

    private String categoryId;

    private String studentAnswer;

    private int score;

    private int totalScore;

}
