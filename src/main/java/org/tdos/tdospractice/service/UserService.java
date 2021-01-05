package org.tdos.tdospractice.service;

import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.utils.OnlineStudent;

import java.util.List;

public interface UserService {
    List<UserEntity> findAll();

    UserEntity findUserByIdAndPwd(String id,String password);

    List<OnlineStudent> getOnlineStudents(List<String> ids);

    List searchUser(String ownerID, String search);

    String deleteUser(List<String> userIDs, String ownerID);
}
