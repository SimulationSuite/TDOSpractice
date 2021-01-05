package org.tdos.tdospractice.service;

import org.tdos.tdospractice.entity.ClassEntity;

import java.util.List;

public interface ClassService {
    List<ClassEntity> findAll();

    ClassEntity findClassById(String id);
}
