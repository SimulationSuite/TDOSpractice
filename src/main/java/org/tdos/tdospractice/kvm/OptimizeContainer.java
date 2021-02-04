package org.tdos.tdospractice.kvm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tdos.tdospractice.entity.ContainerEntity;
import org.tdos.tdospractice.mapper.ContainerMapper;
import org.tdos.tdospractice.mapper.CourseMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OptimizeContainer {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ContainerMapper containerMapper;

    @Autowired
    private KvmManager kvmManager;

    @Scheduled(cron = "0 0 23 ? * SUN")
    public void execute() {
        List<String> courses = courseMapper.getExpiredList("")
                .stream().map(x -> x.id).collect(Collectors.toList());
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
