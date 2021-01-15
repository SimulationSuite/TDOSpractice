package org.tdos.tdospractice.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Personnel {

    //姓名
    public String name;

    //类型（教师、学生）
    public int type;

    //编号（学号、工号）
    public String id;

    //性别
    public int gender; // 0 男 1 女

    //系
    public String major;

    //年级
    public String grade;

    //班级
    public String classes;

    //证件号
    public String identificationNumber;

    //手机号
    public String phone;

    // 专业
    public String department;

//    @Override
//    public String toString() {
//        return "Personnel{" +
//                "id='" + id + '\'' +
//                ", name='" + name + '\'' +
//                ", identificationNumber='" + identificationNumber + '\'' +
//                ", gender=" + gender +
//                ", phone='" + phone + '\'' +
//                ", department='" + department + '\'' +
//                ", major='" + major + '\'' +
//                ", grade='" + grade + '\'' +
//                ", classes='" + classes + '\'' +
//                ", type=" + type +
//                '}';
//    }

    public Personnel(String name, int type, String id, int gender, String major , String grade, String classes,String phone,String identificationNumber,String department) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.major = major;
        this.grade = grade;
        this.classes = classes;
        this.type = type;
        this.phone = phone;
        this.identificationNumber = identificationNumber;
        this.department = department;
    }
}
