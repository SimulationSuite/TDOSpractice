package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.UserEntity;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    List<UserEntity> findAll();

    UserEntity findUserByIdAndPwd(@Param("id") String id, @Param("password") String password);

    UserEntity findUserById(String id);

    int findAllUserByRoleId(int role_id);

    List<UserEntity> findAllByRoleID(@Param("role_id") int role_id ,@Param("name") String name);

    List<UserEntity> findAllByRoleIDAndClasses(@Param("role_id") int role_id, @Param("class_id") String class_id,@Param("name") String name);

    int deleteUserById(String id);

    int updateUserById(@Param("id") String id, @Param("name") String name, @Param("gender") int gender, @Param("phone") String phone,@Param("identificationNumber") String identificationNumber);

    int updatePasswordById(@Param("password") String password, @Param("id") String id);

    int insertUserById(@Param("id") String id, @Param("name") String name, @Param("gender") int gender, @Param("password") String password, @Param("roleId") int roleId, @Param("classId") String classId, @Param("phone") String phone, @Param("identificationNumber") String identificationNumber);
}
