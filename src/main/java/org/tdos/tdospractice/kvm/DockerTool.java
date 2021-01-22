package org.tdos.tdospractice.kvm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.dockerjava.api.model.Capability.SYS_ADMIN;

@Slf4j(topic = "Kvm-Docker")
@Data
@NoArgsConstructor
public class DockerTool implements CommonTool {

    private DockerClient dockerClient;

    private String serverName;

    private int order;

    private Ports portBindings;

    private List<Image> imageList;

    private final String downloadPath = "/root/Download/";

    private final ExposedPort tcp = ExposedPort.tcp(6080);

    public enum Type {
        GUI, SSH
    }

    public DockerTool(String certsPath, String serverName, String serverURL, int order) {
        this.serverName = serverName;
        this.order = order;
        initClient(certsPath, serverURL);
        this.imageList = dockerClient.listImagesCmd().withShowAll(true).exec();
    }

    private void initClient(String certsPath, String serverURL) {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerTlsVerify(true)
                .withDockerCertPath(certsPath)
                .withDockerHost(serverURL)
                .withDockerConfig(certsPath)
                .build();
        this.dockerClient = DockerClientBuilder.getInstance(config).build();
        log.info("Connected successful " + this.serverName + " , Docker host is " + serverURL);
    }

    public List<ContainerVO> getAllContainers() {
        List<ContainerVO> voList = new ArrayList<>();
        List<Container> dockerSearch = dockerClient.listContainersCmd().withShowAll(true).exec();
        for (Iterator<Container> iterator = dockerSearch.iterator(); iterator.hasNext(); ) {
            Container container = (Container) iterator.next();
            InspectContainerResponse containerInfo =
                    dockerClient.inspectContainerCmd((container.getId())).exec();
            ContainerVO containerVO = ContainerVO.parse(container, containerInfo, this.order);
            voList.add(containerVO);
        }
        return voList;
    }

    public ContainerVO findContainer(String containerId) {
        Container container = (Container) dockerClient.listContainersCmd().withIdFilter(Collections.singleton(containerId)).exec();
        if (container == null) {
            return null;
        }
        InspectContainerResponse containerInfo =
                dockerClient.inspectContainerCmd(containerId).exec();
        return ContainerVO.parse(container, containerInfo, this.order);
    }

    @SneakyThrows
    public void pull(String imagesName) {
        if (isExistImages(imagesName)) {
            dockerClient.pullImageCmd(imagesName).start().awaitCompletion();
            Image newig = findImageList(imagesName);
            if (newig == null) {
                throw new RuntimeException(imagesName + " pull exception error");
            }
            imageList.add(newig);
        }
    }

    public Image findImageList(String imagesName) {
        List<Image> list = dockerClient.listImagesCmd().withShowAll(true).exec();
        for (Image i : list) {
            if (i.getRepoTags()[0].equals(imagesName)) {
                return i;
            }
        }
        return null;
    }

    private boolean isExistImages(String imagesName) {
        for (Image i : imageList) {
            if (i.getRepoTags()[0].equals(imagesName)) {
                return false;
            }
        }
        return true;
    }

    public List<String> getImageNames(List<String> imagesID) {
        return imageList.stream().filter(i -> imagesID.contains(i.getId()))
                .map(s -> s.getRepoTags()[0]).collect(Collectors.toList());
    }

    public void removeImage(String imageID) {
        dockerClient.removeImageCmd(imageID).exec();
        for (int x = 0; x < imageList.size(); x++) {
            if (imageList.get(x).getId().equals(imageID)) {
                imageList.remove(x);
            }
        }
    }

    /**
     * Start Container
     *
     * @param containerId
     */
    public void start(String containerId) {
        dockerClient.startContainerCmd(containerId).exec();
        log.info(serverName + " start the container, containerId is " + containerId);
    }

    /**
     * Stop Container
     *
     * @param containerId
     */
    public void stop(String containerId) {
        dockerClient.stopContainerCmd(containerId).exec();
        log.info(serverName + " stop the container, containerId is " + containerId);
    }

    /**
     * Restart Container
     *
     * @param containerId
     */
    public void restart(String containerId) {
        dockerClient.restartContainerCmd(containerId).exec();
        log.info(serverName + " restart the container, containerId is " + containerId);
    }

    public void remove(String containerId) {
        dockerClient.removeContainerCmd(containerId).exec();
        log.info(serverName + " remove the container, containerId is " + containerId);
    }

    /**
     * Download file from Container
     *
     * @param containerId
     * @param fileName
     * @return
     */
    public InputStream downloadArchiveContainer(String containerId, String fileName) {
        return dockerClient.copyArchiveFromContainerCmd(containerId, downloadPath + fileName).exec();
    }

    /**
     * Creat Container
     *
     * @param imageContainer
     * @param port
     * @param containerName
     * @param type
     * @return
     */
    public CreateContainerResponse creatContainer(String imageContainer, int port, String containerName, int type) {
        if (type == Type.GUI.ordinal()) {
            return createContainerGUI(imageContainer, port, containerName);
        }
        if (type == Type.SSH.ordinal()) {
            //TODO ssh?
        }
        return null;
    }

    //Create Centos GUI
    private CreateContainerResponse createContainerGUI(String imageContainer, int port, String containerName) {
        portBindings = new Ports();
        portBindings.bind(tcp, Ports.Binding.bindPort(port));
        CreateContainerResponse newContainer = dockerClient.createContainerCmd(imageContainer)
                .withCmd("true")
                .withName(containerName)
                .withExposedPorts(tcp)
                .withHostConfig(new HostConfig()
                        .withPortBindings(portBindings)
                        .withCapAdd(SYS_ADMIN))
                .withEntrypoint("bash", "-c", "/home/init.sh") //Start the novnc
                .exec();
        return newContainer;
    }

    //Create SSH2
    private CreateContainerResponse createContainerSSH() {
        return null;
    }

    /**
     * Close the Docker connection
     *
     * @throws IOException
     */
    public void closeClient() throws IOException {
        dockerClient.close();
    }

}
