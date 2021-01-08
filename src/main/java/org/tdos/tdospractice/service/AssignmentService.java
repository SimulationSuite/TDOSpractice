package org.tdos.tdospractice.service;

import javafx.util.Pair;
import org.tdos.tdospractice.body.Assignment;
import org.tdos.tdospractice.entity.AssignmentEntity;

import java.util.List;
import java.util.Map;

public interface AssignmentService {

    Map<String, Object> deleteAssignmentById(List<String> id);

    AssignmentEntity addAssignment(Assignment assignment);

    Boolean modifyAssignmentNameById(Assignment assignment);

}
