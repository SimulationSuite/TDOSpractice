package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

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

    private int isCorrect; //0.未批改 1. 已批改

    private Date end_at;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
