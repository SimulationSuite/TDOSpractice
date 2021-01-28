package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.ContainerEntity;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ContainerMapper {

    int insertContainer(ContainerEntity containerEntity);

    List<Map<String, Object>> findContainerAll();

    List<Map<String, Object>> findContainerByClass(String classId);

    List<Map<String, Object>> findContainerByTeacher();

    List<Map<String, Object>> findExperimentCount();

    List<ContainerEntity> findRunContainers();

    ContainerEntity findContainerById(String containerId);

    int updateContainerByIds(int status, @Param("experimentIds") List<String> containerIds);

    int deleteByExperimentIds(@Param("experimentIds") List<String> experimentIds);

    List<Map<String, Object>> findRunContainerByTeacher(String classId, String filter);
}
