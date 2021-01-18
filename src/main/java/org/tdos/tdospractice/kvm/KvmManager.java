package org.tdos.tdospractice.kvm;

import com.github.dockerjava.api.command.CreateContainerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class KvmManager {

    @Autowired
    private KvmConfiguration kvmConfiguration;

    public Map<String, ContainerVO> ContainersMap;

    private List<DockerTool> dockerTools;

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    private Random random = new Random();

    private AutoportTool autoportTool;

    private KvmConfiguration.Docker docker;

    private void dockerInit() {
        this.ContainersMap = new HashMap<>();
        this.dockerTools = new ArrayList<>();
        this.docker = kvmConfiguration.getDocker();
        this.autoportTool = new AutoportTool(docker.getStartPort(), docker.getCount());
        List<String> list = docker.getServerInfo();
        if (list.size() <= 0) {
            throw new RuntimeException("No service failed to initialize");
        }
        for (int x = 0; x < list.size(); x++) {
            String[] s = list.get(x).split("@");
            DockerTool dockerTool = new DockerTool(docker.getCertPath()[x], s[0], s[1], x);
            Set<Integer> set = new HashSet<>();
            dockerTool.getAllContainers().forEach(c -> {
                ContainersMap.put(c.getContainername(), c);
                c.getPorts().forEach(p -> {
                    set.add(p.getPubPort());
                });
            });
            this.autoportTool.addRunPorts(x, set);
            this.dockerTools.add(dockerTool);
        }
    }

    private String createAndstartContainer(String studentID, String experimentID, String imageContainer, int type) {
        String containerName = studentID + "@" + experimentID;
        ContainerVO containerVO = findContainer(containerName);
        if (containerVO != null) {
            if (containerVO.getStatus() != ContainerVO.Status.running.ordinal()) {
                execContainer(containerVO, 0);
                ContainerVO cvo = findContainerFromTool(containerVO);
                if (cvo != null) {
                    putLockContainersMap(cvo);
                }
            }
            return containerVO.getContainerID();
        } else {
            int dockerIndex = randomDocker();
            int port = autoportTool.getFreePort(dockerIndex);
            DockerTool dockerTool = dockerTools.get(dockerIndex);
            CreateContainerResponse ccr = dockerTool.creatContainer(containerName, port, imageContainer, type);
            dockerTool.start(ccr.getId());
            ContainerVO cv = dockerTool.findContainer(ccr.getId());
            if (cv == null) {
                return null;
            }
            putLockContainersMap(cv);
            return ccr.getId();
        }
    }

    /**
     * Gets the running container
     *
     * @return
     */
    private List<ContainerVO> getRunContainers() {
        try {
            List<ContainerVO> clist = new ArrayList<>();
            rwLock.readLock();
            for (Map.Entry<String, ContainerVO> entry : ContainersMap.entrySet()) {
                if (entry.getValue().getStatus() == ContainerVO.Status.running.ordinal()) {
                    clist.add(entry.getValue());
                }
            }
            Collections.sort(clist);
            return clist;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * Get the latest container refresh status
     *
     * @param containerVO
     */
    private void RefreshMap(ContainerVO containerVO) {
        putLockContainersMap(findContainerFromTool(containerVO));
    }

    private ContainerVO findContainerFromTool(ContainerVO containerVO) {
        DockerTool dockerTool = dockerTools.get(containerVO.getNodeOrder());
        return dockerTool.findContainer(containerVO.getContainerID());
    }

    /**
     * @param containerVO
     * @param type        0 start, 1 stop, 2 restart 3 remove
     */
    private void execContainer(ContainerVO containerVO, int type) {
        DockerTool dockerTool = dockerTools.get(containerVO.getNodeOrder());
        if (type == 0) {
            dockerTool.start(containerVO.getContainerID());
            RefreshMap(containerVO);
            return;
        } else if (type == 1) {
            dockerTool.stop(containerVO.getContainerID());
            RefreshMap(containerVO);
            return;
        } else if (type == 2) {
            dockerTool.restart(containerVO.getContainerID());
            RefreshMap(containerVO);
            return;
        } else {
            dockerTool.remove(containerVO.getContainerID());
            removeLockContainersMap(containerVO);
            return;
        }
    }

    private void removeLockContainersMap(ContainerVO containerVO) {
        try {
            rwLock.writeLock();
            ContainersMap.remove(containerVO.getContainername());
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private void putLockContainersMap(ContainerVO containerVO) {
        try {
            rwLock.writeLock();
            ContainersMap.put(containerVO.getContainername(), containerVO);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    private ContainerVO findContainer(String containerName) {
        try {
            rwLock.readLock();
            if (ContainersMap.containsKey(containerName)) {
                for (Map.Entry<String, ContainerVO> c : ContainersMap.entrySet()) {
                    if (c.equals(containerName)) {
                        return c.getValue();
                    }
                }
            }
            return null;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    //radmon docker server
    private int randomDocker() {
        if (dockerTools.size() == 1) {
            return 0;
        }
        return random.nextInt(dockerTools.size());
    }

    @PostConstruct
    public void init() {
        dockerInit();
    }
}