package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ClassEntity {
    private String id;

    private String name;

    private String grade;

    private String major;
    
    private String department;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
