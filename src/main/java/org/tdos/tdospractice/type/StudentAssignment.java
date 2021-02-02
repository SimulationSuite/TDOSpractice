package org.tdos.tdospractice.type;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class StudentAssignment {

    private String assignmentId;

    private String userId;

    private String assignmentName;

    private Integer score;

    private Integer status;

    private String userName;

    private LocalDateTime committedAt;

    private LocalDateTime endAt;

    private String className;

}
