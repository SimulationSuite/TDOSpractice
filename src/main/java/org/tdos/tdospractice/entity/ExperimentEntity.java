package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ExperimentEntity {
    private String id;

    private String name;

    private String pic_url;

    private Date end_at;

    private String step;

    private Long duration;

    private String category_id;

    private String introduce;

    private String report_requirement;

    private Integer type;// 0.管理员内置 1.教师开设

    private String parent_id; //父ID

    private List<String> images;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    private String section_id; //只用于接收参数

    private List<ExperimentImageEntity> imagesinfo;

    private Integer image_count;
}
