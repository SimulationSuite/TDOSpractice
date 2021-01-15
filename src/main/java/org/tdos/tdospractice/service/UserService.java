package org.tdos.tdospractice.service;

import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.body.ModifyUser;
import org.tdos.tdospractice.utils.OnlineStudent;

import java.util.List;

public interface UserService {
    List<UserEntity> findAll();

    UserEntity findUserByIdAndPwd(String id,String password);

    List<OnlineStudent> getOnlineStudents(List<String> ids);

    PageInfo<UserEntity> searchUser(String ownerID, String search, int type, String classes, int page , int per_page);

    Pair<Boolean, String> deleteUser(List<String> userIDs,int type, String ownerID);

    Pair<Boolean, String> modifyUser(String ownerID, ModifyUser modifyUser);

    Pair<Boolean, String> updateUserPassword(List<String> userIDs, String ownerID, String password);
}
