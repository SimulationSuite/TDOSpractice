package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.tdos.tdospractice.entity.CategoryEntity;
import org.tdos.tdospractice.entity.ClassEntity;
import org.tdos.tdospractice.entity.QuestionBackEntity;
import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.mapper.CategoryMapper;
import org.tdos.tdospractice.mapper.ClassMapper;
import org.tdos.tdospractice.mapper.UserMapper;
import org.tdos.tdospractice.mapper.QuestionBackMapper;
import org.tdos.tdospractice.service.ExcelService;
import org.tdos.tdospractice.type.Personnel;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.utils.ExcelUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private QuestionBackMapper questionBackMapper;

    @Override
    public String getCategoryName(){
        String result = "";
        List<CategoryEntity> categoryEntityList = categoryMapper.findAllChildCategory();
        List<String> categoryNameList = categoryEntityList.stream().map(x -> x.getName()).collect(Collectors.toList());
        for(int i =0;i<categoryNameList.size();i++)
        {
            result += categoryNameList.get(i);
        }
        return result;
    }


    //正则验证
    public static boolean isCorrect(String rgx, String res)
    {
        Pattern p = Pattern.compile(rgx);

        Matcher m = p.matcher(res);

        return m.matches();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<String> uploadExcel(String userID, InputStream in, String filename) throws IOException {

        String rgxPhone = "^[1]\\d{10}$";
        String rgxIdx = "^\\d{15}|^\\d{17}([0-9]|X|x)$";
        ExcelUtils utils = new ExcelUtils(in, filename);
        int end = utils.getRowCount(0);
        List<Personnel> personnel =  utils.parsePersonnel(utils.read(0, 1, end));
        for(int i = 0;i<personnel.size();i++){
            Personnel p = personnel.get(i);
            if(!isCorrect(rgxPhone, p.getPhone())){
                return Response.error("手机号错误");
            }
            if(!isCorrect(rgxIdx, p.getIdentificationNumber())){
                return Response.error("身份证号错误");
            }
        }
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
                        userMapper.insertUserById(p.getId(),p.getName(),p.getGender(),"12345678",1,"fb0a1080-b11e-427c-8567-56ca6105ea07",p.getPhone(),p.getIdentificationNumber());
                    }
                    break;
                 //管理员
                case 0:
                    if (user == null) {
                        userMapper.insertUserById(p.getId(),p.getName(),p.getGender(),"12345678",0,"fb0a1080-b11e-427c-8567-56ca6105ea07",p.getPhone(),p.getIdentificationNumber());
                    }
                    break;
            }
        }
        return Response.success("success");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<String> uploadQbExcel(InputStream in, String filename) throws IOException {
        ExcelUtils utils = new ExcelUtils(in, filename);
        int end = utils.getRowCount(0);
        List<QuestionBackEntity> questionBackEntityList =  utils.parseQuestionBack(utils.read(0, 1, end));
        if(questionBackEntityList.size() < 1)
        {
            return Response.error("无题目内容。");
        }
        List<CategoryEntity> categoryEntityList = categoryMapper.findAllChildCategory();
        for (QuestionBackEntity q : questionBackEntityList) {
            List<String> categoryId = categoryEntityList.stream().filter(x -> x.getName().equals(q.getCategoryId())).map(CategoryEntity::getId).collect(Collectors.toList());
            try{
                q.setCategoryId(categoryId.get(0));
                if (ObjectUtils.isEmpty(q.getType())) {
                    return Response.error("存在题目类型为空。");
                }
                if (ObjectUtils.isEmpty(q.getContent())) {
                    return Response.error("存在题目内容为空。");
                }
                if(q.getType() == 0){
                    if (ObjectUtils.isEmpty(q.getChoice())) {
                        return Response.error("题目选择不能为空。");
                    }
                }
                if (ObjectUtils.isEmpty(q.getAnswer())) {
                    return Response.error("题目答案不能为空。");
                }
                if (ObjectUtils.isEmpty(q.getCategoryId())) {
                    return Response.error("题目分类不能为空。");
                }
                if(questionBackMapper.hasQuestionBackNameExist(q.getContent())>0)
                {
                    return Response.error("存在题目详情重复。");
                }
                questionBackMapper.addQuestionBack(q);
            }
            catch (Exception e)
            {
                return Response.error(e.toString());
            }
        }
        return Response.success("success");
    }
}
