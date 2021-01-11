package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.QuestionBackEntity;

import java.util.List;

@Mapper
@Repository
public interface QuestionBackMapper {

    int deleteQuestionBackById(@Param("id") String id);

    boolean ifSectionQuestionBackByQuestionBackId(String id);

    int modifyQuestionBackNameById(QuestionBackEntity questionBack);

    int addQuestionBack(QuestionBackEntity questionBack);

}
