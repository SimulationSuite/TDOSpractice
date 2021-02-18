package org.tdos.tdospractice.body;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UploadFile {

    private int type;

    private String name;

    private Long size;

    private Long time;

    private int pages;

}
