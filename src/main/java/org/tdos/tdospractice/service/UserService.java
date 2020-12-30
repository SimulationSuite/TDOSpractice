package org.tdos.tdospractice.service;

import org.tdos.tdospractice.entity.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> findAll();
}
