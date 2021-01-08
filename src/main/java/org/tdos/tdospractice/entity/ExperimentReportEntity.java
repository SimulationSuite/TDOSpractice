package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ExperimentReportEntity {

    private String id;

    private String experiment_id;

    private String user_id;

    private String url;

    private String score;

    private int status; //0. 未提交 1.已提交

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
