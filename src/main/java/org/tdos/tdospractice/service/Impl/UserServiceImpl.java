package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.tdos.tdospractice.entity.ClassEntity;
import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.mapper.UserMapper;
import org.tdos.tdospractice.service.ClassService;
import org.tdos.tdospractice.service.UserService;
import org.tdos.tdospractice.utils.OnlineStudent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends Throwable implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClassService classService;

    @Override
    public List<UserEntity> findAll() {
        return userMapper.findAll();
    }

    @Override
    public UserEntity findUserByIdAndPwd(String id, String password) {
        return userMapper.findUserByIdAndPwd(id,password);
    }

    @Override
    public List<OnlineStudent> getOnlineStudents(List<String> ids) {
        return ids.stream().map(userMapper::findUserById).filter(x -> x!=null)
                .map(x -> {
                    if (x.getClassID() != null) {
                        ClassEntity am = classService.findClassById(x.getClassID());
                        if (am != null) {
                            return OnlineStudent.builder()
                                    .className(am.getName())
                                    .gradeName(am.getGrade())
                                    .type(x.getRoleID() == 1 ? 1 : 0)
                                    .userID(x.getId())
                                    .name(x.getName()).build();
                        }
                    }
                    return OnlineStudent.builder().name(x.getName())
                            .type(x.getRoleID() == 1 ? 0 : 1)
                            .userID(x.getId())
                            .build();

                }).filter(x -> x.type == 0).collect(Collectors.toList());
    }

    @Override
    public List searchUser(String ownerID, String search) {
        UserEntity user = userMapper.findUserById(ownerID);
        if (StringUtils.isEmpty(user)) {
            return new ArrayList<>();
        }
        int role;
        switch (user.getRoleID()) {
            case 0: {
                role = 1;
                return getList(search, role);
            }
            case 1: {
                role =2;
                return getList(search, role);
            }
            case 2:
            default:
                return new ArrayList<>();
        }
    }


    private List getList(String search, Integer role) {
        List<UserEntity> list;
        if (StringUtils.isEmpty(search)) {
            list = userMapper.findAllByRoleID(role);
        } else {
            list = userMapper.findAllByRoleID(role).stream()
                    .filter(x -> x.getId().contains(search) || x.getName().contains(search))
                    .collect(Collectors.toList());
       }
        return list.stream().collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteUser(List<String> userIDs, String ownerID) {
        UserEntity user = userMapper.findUserById(ownerID);
        if (StringUtils.isEmpty(user)) {
            return "jwt user_id is exist";
        }
        switch (user.getRoleID()) {
            case 0: {
                boolean allExist = userIDs.stream().allMatch(x -> !StringUtils.isEmpty(userMapper.findUserById(x)) && userMapper.findUserById(x).getRoleID() == 1);
                if (!allExist) {
                    return "user_id is invalid";
                }
            }
            break;
            case 1: {
                boolean allExist = userIDs.stream().allMatch(x -> !StringUtils.isEmpty(userMapper.findUserById(x)) && userMapper.findUserById(x).getRoleID() == 2);
                if (!allExist) {
                    return "user_id is invalid";
                }
            }
            break;
            default:
                return "jwt user_id cannot have power";
        }
        userIDs.forEach(x -> userMapper.deleteUserById(x));
        return "1";
    }
}
