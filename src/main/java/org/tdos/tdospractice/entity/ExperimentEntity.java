package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ExperimentEntity {
    private String id;

    private String section_id;

    private String name;

    private String pic_url;

    private Date end_at;

    private String step_url;

    private Long duration;

    private String image_id;

    private String category_id;

    private int type; //0. 基础实验 1.引用实验

    private String parent_experiment_id;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
