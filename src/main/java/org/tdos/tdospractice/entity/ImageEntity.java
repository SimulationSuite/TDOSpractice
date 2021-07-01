package org.tdos.tdospractice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private int state;//0已下载 1正在下载中

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageEntity that = (ImageEntity) o;

        if (!Id.equals(that.Id)) return false;
        if (!imageName.equals(that.imageName)) return false;
        return imageId.equals(that.imageId);
    }
}
