package org.tdos.tdospractice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ContainerEntity {

    private String containerId;

    private String name; //userID+@+实验ID+@+镜像ID

    private String userId;

    private String courseId;

    private String experimentId;

    private String imageId;

    private String url;

    private String ports;// pubport@pubport...

    private int nodeOrder;

    private int status;// 0 created/1 running/2 exited

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

    public List<Integer> getPubPorts() {
        List<Integer> list = new ArrayList<>();
        if (this.ports != null && !this.ports.equals("")) {
            String[] st = ports.split("@");
            for (String s : st) {
                list.add(Integer.parseInt(s));
            }
        }
        return list;
    }
}
