package org.tdos.tdospractice.entity;

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

    private int type;//0 内置 1 引用

    private String parentImageId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
