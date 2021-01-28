package org.tdos.tdospractice.body;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContainerPorts {

    private int innerPort;

    private int pubPort;
}
