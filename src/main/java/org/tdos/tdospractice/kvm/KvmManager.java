package org.tdos.tdospractice.kvm;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tdos.tdospractice.entity.ContainerEntity;
import org.tdos.tdospractice.entity.ImageEntity;
import org.tdos.tdospractice.utils.HashUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class KvmManager {

    @Autowired
    private KvmConfiguration kvmConfiguration;

    private final Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2);

    private List<DockerTool> dockerTools;

    private Random random = new Random();

    private KvmConfiguration.Docker docker;

    private final String separator = "@";

    public enum ExecType {
        START, STOP, RESTART
    }

    private Cache<String, ImageEntity> cache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

    private void dockerInit() {
        this.dockerTools = new ArrayList<>();
        this.docker = kvmConfiguration.getDocker();
        List<KvmConfiguration.ServerInfo> list = docker.getServerInfo();
        if (list.size() <= 0) {
            throw new RuntimeException("No service failed to initialize");
        }
        int index = 0;
        for (KvmConfiguration.ServerInfo serverInfo : list) {
            String[] s = serverInfo.getName().split(separator);
            DockerTool dockerTool = new DockerTool(serverInfo.getPath(), s[0], s[1], index, docker.getStartPort(), docker.getCount());
            dockerTool.addPortList();
            this.dockerTools.add(dockerTool);
            index++;
        }
    }

    /**
     * @param userId
     * @param experimentId
     * @param imageEntity
     * @param courseId
     * @return
     */
    public ContainerEntity createContainer(String userId, String experimentId, ImageEntity imageEntity, String courseId) {
        int dockerIndex = randomDocker();
        DockerTool dockerTool = dockerTools.get(dockerIndex);
        List<Integer> ports = getFreePorts(dockerTool);
        if (ports.size() == 0) {
            return null;
        }
        String containerName = userId + separator + experimentId + separator + imageEntity.getId() + separator + courseId;
        CreateContainerResponse ccr = dockerTool.creatContainer(imageEntity.getImageName(), HashUtils.encodesha3(containerName), imageEntity.getKind(), ports);
        if (ccr == null) {
            return null;
        }
        return ContainerEntity.builder()
                .containerId(ccr.getId())
                .name(containerName)
                .userId(userId)
                .experimentId(experimentId)
                .imageId(imageEntity.getId())
                .url(getURL(imageEntity, ports.get(0), dockerTool.getIP()))
                .ports(ports.stream().map(String::valueOf).collect(Collectors.joining(separator)))
                .nodeOrder(dockerIndex)
                .status(0).build();
    }

    private String getURL(ImageEntity imageEntity, int port, String IP) {
        return String.format("ws://%s:%d%s", IP, port, imageEntity.getUrl());
    }

    private List<Integer> getFreePorts(DockerTool dockerTool) {
        List<Integer> list = dockerTool.getFreePort(1);
        return list;
    }

    public void asyncExecContainer(List<ContainerEntity> containers, int type) {
        try {
            List<CompletableFuture<Void>> list = new ArrayList<>();
            switch (type) {
                case 0://start
                    containers.forEach(l -> {
                        list.add(CompletableFuture.runAsync(() -> {
                            dockerTools.get(l.getNodeOrder()).start(l.getContainerId());
                        }, executor));
                    });
                    break;
                case 1://stop
                    containers.forEach(l -> {
                        list.add(CompletableFuture.runAsync(() -> {
                            dockerTools.get(l.getNodeOrder()).stop(l.getContainerId());
                        }, executor));
                    });
                    break;
                case 2://restart
                    containers.forEach(l -> {
                        list.add(CompletableFuture.runAsync(() -> {
                            dockerTools.get(l.getNodeOrder()).restart(l.getContainerId());
                        }, executor));
                    });
                    break;
            }
            CompletableFuture.allOf(list.toArray(new CompletableFuture[]{})).join();
        } catch (Exception e) {
            return;
        }
    }

    public byte[] downloadFile(ContainerEntity containerEntity, String fileName) {
        DockerTool dockerTool = dockerTools.get(containerEntity.getNodeOrder());
        return dockerTool.downloadArchiveContainer(containerEntity.getContainerId(), fileName);
    }

    public int getRunContainerCount() {
        return dockerTools.stream().mapToInt(d -> d.getAllgetAllContainers(false).size()).sum();
    }

    public void removeContainers(List<ContainerEntity> containerEntities) {
        try {
            List<CompletableFuture<Void>> list = new ArrayList<>();
            containerEntities.forEach(c -> {
                list.add(CompletableFuture.runAsync(() -> {
                    DockerTool dockerTool = dockerTools.get(c.getNodeOrder());
                    dockerTool.stopAndremove(c.getContainerId(), c.getPubPorts());
                }, executor));
            });
            CompletableFuture.allOf(list.toArray(new CompletableFuture[]{})).join();
        } catch (Exception e) {
            return;
        }
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

    public void addImage(ImageEntity imageEntity) {
        cache.put(imageEntity.getImageId(), imageEntity);
    }

    public List<ImageEntity> getCache() {
        return (List<ImageEntity>) cache.asMap().values();
    }
}
