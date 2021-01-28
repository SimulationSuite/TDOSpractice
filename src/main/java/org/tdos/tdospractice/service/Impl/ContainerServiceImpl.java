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
import org.tdos.tdospractice.kvm.KvmManager;
import org.tdos.tdospractice.mapper.ContainerMapper;
import org.tdos.tdospractice.service.ContainerService;
import org.tdos.tdospractice.utils.JsonUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ContainerServiceImpl implements ContainerService {

    @Autowired
    private ContainerMapper containerMapper;

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
    public PageInfo<Map<String, Object>> getRunContainerByTeacher(String classId, String filter, int page, int perPage) {
        PageHelper.startPage(page, perPage);
        return new PageInfo<>(containerMapper.findRunContainerByTeacher(classId, filter));
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
    public List<ContainerEntity> createContainers(String containerId) {

        return null;
    }

    @Override
    public boolean execContainer(String containerId, int type) {
        if (type < KvmManager.ExecType.START.ordinal() || KvmManager.ExecType.RESTART.ordinal() > type) {
            return true;
        }
        ContainerEntity containerEntity = containerMapper.findContainerById(containerId);
        if (containerEntity == null) {
            return true;
        }
        if (KvmManager.ExecType.START.ordinal() == type && containerEntity.getStatus() == 1) {
            return true;
        }
        kvmManager.execContainer(containerEntity.getContainerId(), containerEntity.getNodeOrder(), type);
        int status;
        if (KvmManager.ExecType.STOP.ordinal() == type) {
            status = 2;
        } else {
            status = 1;
        }
        containerMapper.updateContainerByIds(status, Collections.singletonList(containerId));
        return false;
    }
}
