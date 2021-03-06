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

    List<Map<String, Object>> findContainerByClass(@Param("classId") String classId);

    List<Map<String, Object>> findContainerByTeacher();

    List<Map<String, Object>> findExperimentCount();

    List<ContainerEntity> findRunContainers();

    List<ContainerEntity> findContainerByIds(@Param("containerIds") List<String> containerIds);

    List<ContainerEntity> findContainerByExperimentidAndUserid(String userId, String experimentId);

    int updateContainerByIds(int status, @Param("containerIds") List<String> containerIds);

    int deleteByExperimentIds(@Param("experimentIds") List<String> experimentIds);

    int deleteByContainerIds(@Param("containerIds") List<String> containerIds);

    List<Map<String, Object>> findRunContainerByTeacher(String courseId, String filter, String teacherId);

    ContainerEntity findContainerByName(String ContainerName);

    ContainerEntity findContainerById(String containerId);

    List<ContainerEntity> findContainerByImagesIds(@Param("imagesIds") List<String> imagesIds);

    List<ContainerEntity> findContainerByCourseIds(@Param("courseIds") List<String> courseIds);

    List<ContainerEntity> findContainerByUserId(@Param("userIds") List<String> courseIds);
}
