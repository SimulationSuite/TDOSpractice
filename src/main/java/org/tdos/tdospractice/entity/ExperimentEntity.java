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

    private String name;

    private String pic_url;

    private Date end_at;

    private String step;

    private Long duration;

    private String category_id;

    private String introduce;

    private String section_id; //只用于接收参数

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
