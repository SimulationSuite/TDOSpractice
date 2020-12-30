package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.UserEntity;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    List<UserEntity> findAll();
}
