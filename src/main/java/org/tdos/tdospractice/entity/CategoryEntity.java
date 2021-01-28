package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CategoryEntity {

    private String id;

    private String name;

    private String parent_category_id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
