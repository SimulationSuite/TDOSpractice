package org.tdos.tdospractice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ImageEntity implements Serializable {

    private String Id;

    private String imageName;

    private String imageId;

    private long size;

    private String introduction;

    private int kind;//0 Docker-SSH 1 Docker-GUI

    private String url;

    private int type;//0 内置 1 引用

    private String parentImageId;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;
}
