package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CoursewareEntity {

    private String id;

    private String name;

    private int kind;

    private int type;

    private String url;

    private Integer duration;

    private int size;

    private String categoryId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
