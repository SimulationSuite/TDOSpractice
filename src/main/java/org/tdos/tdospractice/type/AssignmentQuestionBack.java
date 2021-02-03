package org.tdos.tdospractice.type;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AssignmentQuestionBack {

    private String id;

    private String sectionId;

    private String name;

    private LocalDateTime endAt;

    private int qualifiedScore;

    private int type;

    private String content;

    private String choice;

    private String answer;

    private String picUrl;

    private Integer status;
}
