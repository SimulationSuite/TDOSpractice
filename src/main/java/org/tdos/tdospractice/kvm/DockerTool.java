package org.tdos.tdospractice.kvm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.github.dockerjava.api.model.Capability.SYS_ADMIN;

@Slf4j(topic = "Kvm-Docker")
@Data
@NoArgsConstructor
public class DockerTool implements CommonTool {

    private String IP;

    private DockerClient dockerClient;

    private String serverName;

    private int order;

    private Ports portBindings;

    private List<Image> imageList;

    private int startPort;

    private int count;

    private Set<Integer> usedPorts;

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    private final String downloadPath = "/root/Downloads/";

    private final ExposedPort tcp = ExposedPort.tcp(6080);

    public enum Type {
        SSH, GUI
    }

    public DockerTool(String certsPath, String serverName, String serverURL, int order, int startPort, int count) {
        this.serverName = serverName;
        this.order = order;
        this.startPort = startPort;
        this.count = count;
        this.usedPorts = new HashSet<>();
        initClient(certsPath, serverURL);
        this.IP = parseIP(serverURL);
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

    private String parseIP(String serverURL) {
        String s = serverURL.substring(serverURL.indexOf("//") + 2);
        String[] arr = s.split(":");
        return arr[0];
    }

    public void addPortList() {
        List<Integer> ports = new ArrayList<>();
        getAllgetAllContainers(true).forEach(l -> {
            for (ContainerPort c : l.getPorts()) {
                if (c.getPrivatePort() != null && c.getPublicPort() != null) {
                    ports.add(c.getPublicPort());
                }
            }
        });
        addPorts(ports);
    }

    public void addPorts(Collection<Integer> ports) {
        rwLock.writeLock().lock();
        try {
            usedPorts.addAll(ports);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void removePorts(Collection<Integer> ports) {
        rwLock.writeLock().lock();
        try {
            usedPorts.removeAll(ports);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public List<Integer> getFreePort(int counts) {
        List<Integer> list = new ArrayList<>();
        int item = counts;
        rwLock.readLock().lock();
        try {
            for (int x = startPort; x <= (startPort + count); x++) {
                if (!usedPorts.contains(x)) {
                    list.add(x);
                    if (--item == 0) {
                        break;
                    }
                }
            }
            return list;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public List<Container> getAllgetAllContainers(boolean type) {
        return dockerClient.listContainersCmd().withShowAll(type).exec();
    }

    @SneakyThrows
    public void pull(String imagesName) {
        if (isExistImageName(imagesName)) {
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

    private boolean isExistImageName(String imagesName) {
        for (Image i : imageList) {
            if (i.getRepoTags()[0].equals(imagesName)) {
                return false;
            }
        }
        return true;
    }

    public boolean isExistImageID(List<String> imageID) {
        for (Image i : imageList) {
            if (imageID.contains(i.getId())) {
                return true;
            }
        }
        return false;
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
        List<Container> containers = dockerClient.listContainersCmd().withIdFilter(Collections.singleton(containerId)).exec();
        if (containers.size() == 0) {
            dockerClient.startContainerCmd(containerId).exec();
            log.info(serverName + " start the container, containerId is " + containerId);
        }
    }

    /**
     * Stop Container
     *
     * @param containerId
     */
    public void stop(String containerId) {
        List<Container> containers = dockerClient.listContainersCmd().withIdFilter(Collections.singleton(containerId)).exec();
        if (containers.size() > 0) {
            dockerClient.stopContainerCmd(containerId).exec();
            log.info(serverName + " stop the container, containerId is " + containerId);
        }
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
        List<Container> containers = dockerClient.listContainersCmd().withIdFilter(Collections.singleton(containerId)).exec();
        if (containers.size() > 0) {
            dockerClient.removeContainerCmd(containerId).exec();
            log.info(serverName + " remove the container, containerId is " + containerId);
        }
    }

    public void stopAndremove(String containerId) {
        stop(containerId);
        remove(containerId);
    }

    /**
     * Download file from Container
     *
     * @param containerId
     * @param fileName
     * @return
     */
    public byte[] downloadArchiveContainer(String containerId, String fileName) {
        try {
            InputStream s = dockerClient.copyArchiveFromContainerCmd(containerId, downloadPath + fileName).exec();
            return IOUtils.toByteArray(s);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creat Container
     *
     * @param imageContainer
     * @param containerName
     * @param type
     * @param ports
     * @return
     */
    public CreateContainerResponse creatContainer(String imageContainer, String containerName, int type, List<Integer> ports) {
        if (type == Type.GUI.ordinal()) {
            addPorts(ports);
            return createContainerGUI(imageContainer, ports.get(0), containerName);
        }
        if (type == Type.SSH.ordinal()) {
            addPorts(ports);
            return createContainerSSH(imageContainer, ports, containerName);
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
                .withEntrypoint("bash", "-c", "/usr/local/init.sh") //Start the novnc
                .exec();

        return newContainer;
    }

    //Create SSH2
    private CreateContainerResponse createContainerSSH(String imageContainer, List<Integer> ports, String containerName) {
        ExposedPort tcp = ExposedPort.tcp(2222);
        ExposedPort tcp22 = ExposedPort.tcp(22);
        Ports portBindings = new Ports();
        portBindings.bind(tcp, Ports.Binding.bindPort(ports.get(0)));
        portBindings.bind(tcp22, Ports.Binding.bindPort(ports.get(1)));
        CreateContainerResponse newContainer = dockerClient.createContainerCmd(imageContainer)
                .withName(containerName)
                .withExposedPorts(tcp)
                .withHostConfig(new HostConfig()
                        .withPrivileged(true)
                        .withPortBindings(portBindings)
                        .withCapAdd(SYS_ADMIN))
                .exec();
        return newContainer;
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
