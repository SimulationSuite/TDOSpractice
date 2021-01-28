package org.tdos.tdospractice.kvm;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tdos.tdospractice.entity.ContainerEntity;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class KvmManager {

    @Autowired
    private KvmConfiguration kvmConfiguration;

    private final Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2);

    private List<DockerTool> dockerTools;

    private Random random = new Random();

    private KvmConfiguration.Docker docker;

    private void dockerInit() {
        this.dockerTools = new ArrayList<>();
        this.docker = kvmConfiguration.getDocker();
        List<String> list = docker.getServerInfo();
        if (list.size() <= 0) {
            throw new RuntimeException("No service failed to initialize");
        }
        for (int x = 0; x < list.size(); x++) {
            String[] s = list.get(x).split("@");
            DockerTool dockerTool = new DockerTool(docker.getCertPath()[x], s[0], s[1], x, docker.getStartPort(), docker.getCount());
            dockerTool.addPortList();
            this.dockerTools.add(dockerTool);
        }
    }

    /**
     * @param userId
     * @param experimentId
     * @param imageName
     * @param kind
     * @return
     */
    public ContainerEntity createContainer(String userId, String experimentId, String imageName, int kind) {
        int dockerIndex = randomDocker();
        DockerTool dockerTool = dockerTools.get(dockerIndex);
        String containerName = userId + "@" + experimentId;
        CreateContainerResponse ccr = dockerTool.creatContainer(imageName, containerName, kind);
        if (ccr == null) {
            return null;
        }
        return ContainerEntity.builder()
                .containerId(ccr.getId())
                .name(containerName)
                .userId(userId)
                .experimentId(experimentId)
                .nodeOrder(dockerIndex)
                .status(0).build();
    }

    /**
     * @param ContainerID
     * @param nodeOrder
     * @param type        0 start, 1 stop, 2 restart
     */
    private void execContainer(String ContainerID, int nodeOrder, int type) {
        DockerTool dockerTool = dockerTools.get(nodeOrder);
        if (type == 0) {
            dockerTool.start(ContainerID);
            return;
        } else if (type == 1) {
            dockerTool.stop(ContainerID);
            return;
        } else if (type == 2) {
            dockerTool.restart(ContainerID);
            return;
        }
    }

    public void stopContainers(List<ContainerEntity> containerEntities) {
        List<CompletableFuture<Void>> list = new ArrayList<>();
        for (int x = 0; x < dockerTools.size(); x++) {
            int finalX = x;
            containerEntities.stream().filter(f -> f.getNodeOrder() == finalX).forEach(l -> {
                list.add(CompletableFuture.runAsync(() -> {
                    dockerTools.get(finalX).stop(l.getContainerId());
                }, executor));
            });
        }
        CompletableFuture.allOf(list.toArray(new CompletableFuture[]{})).join();
    }

    public int getRunContainerCount() {
        return dockerTools.stream().mapToInt(d -> d.getAllgetAllContainers(false).size()).sum();
    }

    public void removeContainers(List<ContainerEntity> containerEntities) {

    }

    public void pullImages(String imagesName) {
        List<CompletableFuture<Void>> list = new ArrayList<>();
        dockerTools.forEach(d -> {
            list.add(CompletableFuture.runAsync(() -> {
                d.pull(imagesName);
            }, executor));
        });
        CompletableFuture.allOf(list.toArray(new CompletableFuture[]{})).join();
    }

    public int getToolSize() {
        return dockerTools.size();
    }

    public List<Image> getImageInfo(String imagesName) {
        List<Image> list = new ArrayList<>();
        dockerTools.forEach(d -> {
            list.add(d.findImageList(imagesName));
        });
        return list;
    }

    //radmon docker server
    private int randomDocker() {
        if (dockerTools.size() == 1) {
            return 0;
        }
        return random.nextInt(dockerTools.size());
    }

    public List<Container> getAll() {
        List<Container> containers = new ArrayList<>();
        dockerTools.forEach(d -> {
            containers.addAll(d.getAllgetAllContainers(true));
        });
        return containers;
    }

    @PostConstruct
    public void init() {
        dockerInit();
    }

    public boolean isQuoteContainer(List<String> imagesID) {
        return getAll().stream()
                .filter(s -> imagesID.contains(s.getImageId()))
                .collect(Collectors.toList()).size() > 0;
    }

    public void removeImages(List<String> imagesID) {
        dockerTools.forEach(d -> {
            imagesID.forEach(d::removeImage);
        });
    }

    public boolean isExistImageID(List<String> imageID) {
        return dockerTools.stream().anyMatch(s -> s.isExistImageID(imageID));
    }
}
