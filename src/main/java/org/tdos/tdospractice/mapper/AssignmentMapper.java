package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.AssignmentEntity;

import java.util.List;

@Mapper
@Repository
public interface AssignmentMapper {

    int deleteAssignmentById(@Param("id") String id);

    boolean ifSectionAssignmentByAssignmentId(String id);

    int modifyAssignmentNameById(@Param("id") String id, @Param("name") String name);

    int addAssignment(AssignmentEntity assignment);

}
