package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.tdos.tdospractice.entity.ClassEntity;
import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.mapper.ClassMapper;
import org.tdos.tdospractice.mapper.UserMapper;
import org.tdos.tdospractice.service.ClassService;
import org.tdos.tdospractice.service.UserService;
import org.tdos.tdospractice.body.ModifyUser;
import org.tdos.tdospractice.utils.OnlineStudent;

import javax.validation.Valid;
import java.net.BindException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends Throwable implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClassMapper classMapper;

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
        return ids.stream().map(userMapper::findUserById).filter(x -> x != null)
                .map(x -> {
                    if (x.getRoleID() == 2) {
                        ClassEntity am = classMapper.findClassById(x.getClassID());
                        if (am != null) {
                            return OnlineStudent.builder()
                                    .className(am.getName())
                                    .gradeName(am.getGrade())
                                    .type(x.getRoleID())
                                    .userID(x.getId())
                                    .name(x.getName()).build();
                        }
                    }
                    return OnlineStudent.builder().name(x.getName())
                            .type(x.getRoleID())
                            .userID(x.getId())
                            .build();

                }).collect(Collectors.toList());
    }

    @Override
    public PageInfo<UserEntity> searchUser(String ownerID, String search, int type , String classesId, int page , int per_page) {
        PageInfo<UserEntity> pageInfo = new PageInfo<>();
        UserEntity user = userMapper.findUserById(ownerID);
        if (user == null) {
            return pageInfo;
        }
        switch (type) {
            case 0: {
                return new PageInfo<UserEntity>(getList(search, 0, classesId,page,per_page));
            }
            case 1: {
                return new PageInfo<UserEntity>(getList(search, 1, classesId,page,per_page));
            }
            case 2: {
                return new PageInfo<UserEntity>(getList(search, 2, classesId,page,per_page));
            }
            default:
                return pageInfo;
        }
    }


    private List<UserEntity> getList(String search, int role,String classesId,int page,int per_page) {
        List<UserEntity> list;
        PageHelper.startPage(page,per_page);
        if (!StringUtils.hasText(search) && !StringUtils.hasText(classesId)) {
            list = userMapper.findAllByRoleID(role);
        } else if (!StringUtils.hasText(search) && StringUtils.hasText(classesId)){
            list = userMapper.findAllByRoleIDAndClasses(role,classesId);
        } else if(StringUtils.hasText(search) && StringUtils.hasText(classesId)) {
            list = userMapper.findAllByRoleIDAndClasses(role,classesId).stream()
                    .filter(x -> x.getId().contains(search) || x.getName().contains(search))
                    .collect(Collectors.toList());
        } else {
            list = userMapper.findAllByRoleID(role).stream()
                    .filter(x -> x.getId().contains(search) || x.getName().contains(search))
                    .collect(Collectors.toList());
       }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pair<Boolean, String> deleteUser(List<String> userIDs,int type, String ownerID) {
        UserEntity user = userMapper.findUserById(ownerID);
        if (user == null) {
            return new Pair<>(false, "jwt user_id is exist");
        }
        switch (type) {
            case 0: {
                boolean allExist = userIDs.stream().allMatch(x -> !StringUtils.isEmpty(userMapper.findUserById(x)) && userMapper.findUserById(x).getRoleID() == 0);
                if (!allExist) {
                    return new Pair<>(false, "user_id is invalid");
                }
            }
            break;
            case 1: {
                boolean allExist = userIDs.stream().allMatch(x -> !StringUtils.isEmpty(userMapper.findUserById(x)) && userMapper.findUserById(x).getRoleID() == 1);
                if (!allExist) {
                    return new Pair<>(false, "user_id is invalid");
                }
            }
            break;
            case 2: {
                boolean allExist = userIDs.stream().allMatch(x -> !StringUtils.isEmpty(userMapper.findUserById(x)) && userMapper.findUserById(x).getRoleID() == 2);
                if (!allExist) {
                    return new Pair<>(false, "user_id is invalid");
                }
            }
            break;
            default:
        }
        userIDs.forEach(x -> userMapper.deleteUserById(x));
        return new Pair<>(true,"");
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pair<Boolean, String> modifyUser(String ownerID, ModifyUser modifyUser) {
        UserEntity owner = userMapper.findUserById(ownerID);
        if (owner == null) {
            return new Pair<>(false, "jwt user_id is exist");
        }
        int role = 0;
        UserEntity user = userMapper.findUserById(modifyUser.useID);
        if (user == null) {
            return new Pair<>(false, "user_id is not exist");
        }
        if (owner.getRoleID() != role) {
            return new Pair<>(false, "jwt user_id cannot have power");
        }
        userMapper.updateUserById(modifyUser.useID, modifyUser.name, modifyUser.gender, modifyUser.phone, modifyUser.identificationNumber);
        return new Pair<>(true,"");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pair<Boolean, String> updateUserPassword(List<String> userIDs, String ownerID, String password) {
        UserEntity owner = userMapper.findUserById(ownerID);
        if (owner == null) {
            return new Pair<>(false, "jwt user_id is exist");
        }
        userIDs.forEach(id -> {
            UserEntity user = userMapper.findUserById(id);
            if (user != null) {
                userMapper.updatePasswordById(password,id);
            }
        });
        return new Pair<>(true, "");
    }
}
