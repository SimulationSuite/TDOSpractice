package org.tdos.tdospractice.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ExperimentImageEntity {

    private String experiment_id;

    private String image_id;

    private String name;

    private String size;

    private Integer kind;

    private String introduction;


}
