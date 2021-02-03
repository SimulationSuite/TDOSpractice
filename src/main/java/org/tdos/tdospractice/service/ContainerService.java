package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import org.tdos.tdospractice.entity.ContainerEntity;

import java.util.List;
import java.util.Map;

public interface ContainerService {

    PageInfo<Map<String, Object>> getRunContainerList(int type, String classId, int page, int perPage);

    PageInfo<Map<String, Object>> getRunExperiment(int page, int perPage);

    PageInfo<Map<String, Object>> getRunContainerByTeacher(String courseId, String filter, int page, int perPage);

    boolean stopRunContainerList();

    List<ContainerEntity> createContainers(String userId, String experimentId,String courseId);

    boolean execContainer(List<String> containerId, int type);

    byte[] downloadCode(String containerId, String fileNmae);

    void removeContainers(List<String> containerIds);
}