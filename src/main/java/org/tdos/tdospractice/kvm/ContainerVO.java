package org.tdos.tdospractice.kvm;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerPort;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContainerVO implements Comparable<ContainerVO> {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String containername;//学生学号+@+实验ID

    private String containerID;

    private String imageName;

    private List<ContainerPorts> ports;

    private int status; // 0 created/1 restarting/2 running/3 paused/4 exited

    private int nodeOrder;

    private String createTime;

    public enum Status {
        created, restarting, running, paused, exited
    }

    @Data
    @AllArgsConstructor
    public static class ContainerPorts {

        private int innerPort;

        private int pubPort;
    }

    @SneakyThrows
    public static ContainerVO parse(Container container, InspectContainerResponse containerInfo, int order) {
        ContainerPort[] containerPort = container.getPorts();
        List<ContainerPorts> ports = getPortList(containerPort);
        return ContainerVO.builder()
                .containerID(containerInfo.getId())
                .containername(containerInfo.getName())
                .imageName(container.getImage())
                .ports(ports)
                .status(parseState(containerInfo.getState().getStatus()))
                .nodeOrder(order)
                .createTime(simpleDateFormat.format(formatter.parse(containerInfo.getCreated())))
                .build();
    }

    private static List<ContainerPorts> getPortList(ContainerPort[] containerPorts) {
        List<ContainerPorts> ports = new ArrayList<>();
        for (ContainerPort c : containerPorts) {
            if (c.getPrivatePort() != null && c.getPublicPort() != null) {
                ContainerPorts cp = new ContainerPorts(c.getPrivatePort(), c.getPublicPort());
                ports.add(cp);
            }
        }
        return ports;
    }

    //Get experimental ID
    private String getExperimentID() {
        return containername.split("@")[1];
    }

    private static int parseState(String status) {
        if (status.equals("created")) {
            return Status.created.ordinal();
        } else if (status.equals("restarting")) {
            return Status.restarting.ordinal();
        } else if (status.equals("running")) {
            return Status.running.ordinal();
        } else if (status.equals("paused")) {
            return Status.paused.ordinal();
        } else {
            return Status.exited.ordinal();
        }
    }

    @Override
    public int compareTo(ContainerVO o) {
        return o.getCreateTime().compareTo(this.createTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContainerVO)) return false;
        ContainerVO that = (ContainerVO) o;
        return that.getContainername().equals(this.containername)
                && that.getContainerID().equals(this.containerID);
    }
}
