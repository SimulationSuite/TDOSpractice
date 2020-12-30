package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.mapper.UserMapper;
import org.tdos.tdospractice.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<UserEntity> findAll() {
        return userMapper.findAll();
    }
}
