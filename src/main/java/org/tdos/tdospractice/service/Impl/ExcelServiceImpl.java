package org.tdos.tdospractice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.tdos.tdospractice.entity.ClassEntity;
import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.mapper.ClassMapper;
import org.tdos.tdospractice.mapper.UserMapper;
import org.tdos.tdospractice.service.ExcelService;
import org.tdos.tdospractice.type.Personnel;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.utils.ExcelUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClassMapper classMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<String> uploadExcel(String userID, InputStream in, String filename) throws IOException {
        ExcelUtils utils = new ExcelUtils(in, filename);
        int end = utils.getRowCount(0);
        List<Personnel> personnel =  utils.parsePersonnel(utils.read(0, 1, end));
        UserEntity owner = userMapper.findUserById(userID);
        if (owner == null) {
            return Response.error("user is not exist");
        }
//        } else {
//            switch (owner.getRoleID()) {
//                case 0:
//                    if (personnel.stream().anyMatch(p -> p.getType() != 1)) {
//                        return Response.error("excel data error,manager only import teachers");
//                    }
//                    break;
//                case 1:
//                    if (personnel.stream().anyMatch(p -> p.getType() != 0 || StringUtils.isEmpty(p.getClasses()) || StringUtils.isEmpty(p.getGrade()) || StringUtils.isEmpty(p.getDepartment()))) {
//                        return  Response.error("excel data error,teacher only import students and students must have classes, grade, major, department");
//                    }
//                    break;
//                case 2:
//                    return  Response.error("excel data error");
//            }
//        }
        for ( int j=0 ;j <personnel.size();j++){
            Personnel p = personnel.get(j);
            UserEntity user = userMapper.findUserById(p.getId());
            if (user != null) {
                return Response.error("user is exist");
            }
            switch (p.getType()) {
                // 学生
                case 2:
                    String id;
                    ClassEntity findClass = classMapper.findClassByClasses(p.getClasses());
                    if(findClass == null){
                        ClassEntity classEntity = new ClassEntity();
                        classEntity.setGrade(p.getGrade());
                        classEntity.setDepartment(p.getDepartment());
                        classEntity.setMajor(p.getMajor());
                        classEntity.setName(p.getClasses());
                        classMapper.insertClassByUser(classEntity);
                        id = classEntity.getId();
                    }else {
                        id = findClass.getId();
                    }
                     userMapper.insertUserById(p.getId(),p.getName(),p.getGender(),"12345678",2,id,p.getPhone(),p.getIdentificationNumber());
                    break;
                // 教师
                case 1:
                    if (user == null) {
                        userMapper.insertUserById(p.getId(),p.getName(),p.getGender(),"12345678",1,"",p.getPhone(),p.getIdentificationNumber());
                    }
                    break;
                 //管理员
                case 0:
                    if (user == null) {
                        userMapper.insertUserById(p.getId(),p.getName(),p.getGender(),"12345678",0,"",p.getPhone(),p.getIdentificationNumber());
                    }
                    break;
            }
        }
        return Response.success("success");
    }
}