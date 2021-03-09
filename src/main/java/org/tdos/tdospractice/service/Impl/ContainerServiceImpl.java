package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.ContainerEntity;
import org.tdos.tdospractice.entity.ImageEntity;
import org.tdos.tdospractice.kvm.KvmManager;
import org.tdos.tdospractice.mapper.ContainerMapper;
import org.tdos.tdospractice.mapper.ImageMapper;
import org.tdos.tdospractice.service.ContainerService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ContainerServiceImpl implements ContainerService {

    @Autowired
    private ContainerMapper containerMapper;

    @Autowired
    private ImageMapper imageMapper;

    @Qualifier("ConrtainerQueue")
    @Autowired
    private ActiveMQQueue containerQueue;

    @Autowired
    private KvmManager kvmManager;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    private final JsonUtils jsonUtils = new JsonUtils();

    @Override
    public PageInfo<Map<String, Object>> getRunContainerList(int type, String classId, int page, int perPage) {
        PageHelper.startPage(page, perPage);
        switch (type) {
            case 0://admin
                return new PageInfo<>(containerMapper.findContainerAll());
            case 1://teacher
                return new PageInfo<>(containerMapper.findContainerByTeacher());
            case 2://student
                return new PageInfo<>(containerMapper.findContainerByClass(classId));
            default:
                return null;
        }
    }

    @Override
    public PageInfo<Map<String, Object>> getRunExperiment(int page, int perPage) {
        PageHelper.startPage(page, perPage);
        return new PageInfo<>(containerMapper.findExperimentCount());
    }

    @Override
    public PageInfo<Map<String, Object>> getRunContainerByTeacher(String courseId, String filter, String teacherId, int page, int perPage) {
        PageHelper.startPage(page, perPage);
        return new PageInfo<>(containerMapper.findRunContainerByTeacher(courseId, filter, teacherId));
    }

    @Override
    public boolean stopRunContainerList() {
        List<ContainerEntity> list = containerMapper.findRunContainers();
        if (list.size() == 0) {
            return true;
        }
        jmsMessagingTemplate.convertAndSend(this.containerQueue, Hex.encodeHexString(jsonUtils.encode(list)));
        return false;
    }

    @Override
    public List<ContainerEntity> createContainers(String userId, String experimentId, String courseId) {
        List<ImageEntity> imageEntityList = imageMapper.findImageByExperimentId(experimentId);
        if (imageEntityList.size() == 0) {
            return null;
        }
        List<ContainerEntity> list = new ArrayList<>();
        imageEntityList.forEach(i -> {
            ContainerEntity containerEntity = containerMapper.findContainerByName(String.format("%s@%s@%s@%s", userId, experimentId, i.getId(), courseId));
            if (containerEntity == null) {
                //create container
                ContainerEntity c = kvmManager.createContainer(userId, experimentId, i, courseId);
                if (c != null) {
                    if (courseId != null && !courseId.equals("")) {
                        c.setCourseId(courseId);
                    }
                    containerMapper.insertContainer(c);
                }
                list.add(c);
            } else {
                list.add(containerEntity);
            }
        });
        return list;
    }

    @Override
    public Response execContainer(List<String> containerIds, int type) {
        if (type < KvmManager.ExecType.START.ordinal() || KvmManager.ExecType.RESTART.ordinal() < type) {
            return Response.error("异常操作");
        }
        if (containerIds.size() == 0) {
            return Response.error("异常操作");
        }
        List<ContainerEntity> containers = containerMapper.findContainerByIds(containerIds);
        if (containers.size() != containerIds.size()) {
            return Response.error("未知实验");
        }
        for (ContainerEntity c : containers) {
            if (KvmManager.ExecType.START.ordinal() == type && c.getStatus() == 1) {
                continue;
            }
            kvmManager.execContainer(c.getContainerId(), c.getNodeOrder(), type);
        }
        int status;
        if (KvmManager.ExecType.STOP.ordinal() == type) {
            status = 2;
        } else {
            status = 1;
        }
        containerMapper.updateContainerByIds(status, containerIds);
        return Response.success();
    }

    @Override
    public byte[] downloadCode(String containerId, String fileName) {
        ContainerEntity containerEntity = containerMapper.findContainerById(containerId);
        if (containerEntity == null || containerEntity.getStatus() != 1) {
            return null;
        }
        return kvmManager.downloadFile(containerEntity, fileName);
    }

    @Override
    public void removeContainers(List<String> containerIds) {
        List<ContainerEntity> cl = containerMapper.findContainerByIds(containerIds);
        kvmManager.removeContainers(cl);
        containerMapper.deleteByContainerIds(containerIds);
    }

    @Override
    public ContainerEntity createAndRunContainers(String userId, String experimentId, String courseId, String imageId) {
        ImageEntity imageEntity = imageMapper.findImageByImagesid(imageId);
        ContainerEntity containerEntity = containerMapper.findContainerByName(String.format("%s@%s@%s@%s", userId, experimentId, imageEntity.getId(), courseId));
        if (containerEntity != null) {
            return null;
        }
        //create container
        ContainerEntity c = kvmManager.createContainer(userId, experimentId, imageEntity, courseId);
        if (c != null) {
            if (courseId != null && !courseId.equals("")) {
                c.setCourseId(courseId);
            }
            kvmManager.execContainer(c.getContainerId(), c.getNodeOrder(), KvmManager.ExecType.START.ordinal());
            c.setStatus(1);
            containerMapper.insertContainer(c);
        }
        return c;
    }

    @Override
    public void stopExperiment(String userId, String experimentId) {
        List<String> containerIds = new ArrayList<>();
        containerMapper.findContainerByExperimentidAndUserid(userId, experimentId).stream().forEach(c -> {
            kvmManager.execContainer(c.getContainerId(), c.getNodeOrder(), KvmManager.ExecType.STOP.ordinal());
            containerIds.add(c.getContainerId());
        });
        if (containerIds.size() > 0) {
            containerMapper.updateContainerByIds(2, containerIds);
        }
    }
}
