package org.tdos.tdospractice.kvm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tdos.tdospractice.entity.ContainerEntity;
import org.tdos.tdospractice.mapper.ContainerMapper;
import org.tdos.tdospractice.mapper.CourseMapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class OptimizeContainer {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ContainerMapper containerMapper;

    @Autowired
    private KvmManager kvmManager;

    @Scheduled(cron = "0 0 24 ? * SUN")
    public void execute() {
        //获取已过期的课程不包含归档的
        List<String> courses = new ArrayList<>();
        if (courses.size() == 0) {
            return;
        }
        List<ContainerEntity> containers = containerMapper.findContainerByCourseIds(courses);
        if (containers.size() == 0) {
            return;
        }
        kvmManager.removeContainers(containers);
    }
}
