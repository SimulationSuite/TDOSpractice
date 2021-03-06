package org.tdos.tdospractice.type;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class StudentExperimentReport {
    private String experiment_id;

    private int score;

    private int status;

    private int isCorrect;

    private String name;

    private LocalDateTime end_at;

}
