package org.tdos.tdospractice.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class StudentScoreEntity implements Serializable {

    private String id;

    private String assignmentId;

    private String userId;

    private Integer score;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
