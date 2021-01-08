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

    private String categoryId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean canUpdate;

}
