package org.tdos.tdospractice.body;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class BindExperiments {

    private List<String> experiment_id;

    private String section_id;

}
