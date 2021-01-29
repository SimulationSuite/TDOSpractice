package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ContainerEntity {

    private String Id;

    private String containerId;

    private String name; //userID+@+实验ID+@+镜像ID

    private String userId;

    private String experimentId;

    private String imageId;

    private String ports;// pubport@pubport...

    private int nodeOrder;

    private int status;// 0 created/1 running/2 exited

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
