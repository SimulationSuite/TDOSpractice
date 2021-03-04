package org.tdos.tdospractice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.tdos.tdospractice.body.AddChapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class CourseTests {

    String nodeTool = "http://localhost:8111";

    static class AddCourseTest {
        public String name;

        public String pic_url;

        public List<AddChapter> chapters;

        public String introduction;

        public String owner_id;
    }

    static class InsertCourseChapterCompletedTest {

        public String course_id;

        public List<AddChapterTest> chapters;
    }

    static class AddChapterTest {

        public String id;

        public String name;

        public int order;

        public List<AddSectionTest> sections;

    }

    static class AddSectionTest {

        public String id;

        public String name;

        public int order;

        public List<AddSmallSectionTest> small_sections;
    }

    static class AddSmallSectionTest {

        public String id;

        public String name;

        public int order;
    }

    @Test
    String addAdminCourse() {
        AddCourseTest addCourse = new AddCourseTest();
        addCourse.name = "课程" + UUID.randomUUID();
        addCourse.pic_url = "3231312312313";
        addCourse.introduction = "课程简介";
        addCourse.chapters = new ArrayList<>();
        addCourse.owner_id = "3234234";
        String body = HttpRequest.post(nodeTool + "/insert_course")
                .connectTimeout(5000)
                .readTimeout(5000)
                .contentType(HttpRequest.CONTENT_TYPE_JSON)
                .send(JSON.toJSONString(addCourse))
                .body();
        JSONObject jo = JSONObject.parseObject(body);
        if (jo.getInteger("code") == 200) {
            String courseId = jo.getJSONObject("data").getString("id");
            System.out.println("新建课程id: " + courseId);
            List<AddChapterTest> chapters = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                AddChapterTest addChapterTest = new AddChapterTest();
                addChapterTest.name = "第" + (i + 1) + "章";
                addChapterTest.order = i + 1;
                addChapterTest.sections = new ArrayList<>();
                for (int j = 0; j < 10; j++) {
                    AddSectionTest addSectionTest = new AddSectionTest();
                    addSectionTest.name = "第" + (j + 1) + "节";
                    addSectionTest.order = j + 1;
                    addSectionTest.small_sections = new ArrayList<>();
                    for (int k = 0; k < 10; k++) {
                        AddSmallSectionTest addSmallSectionTest = new AddSmallSectionTest();
                        addSmallSectionTest.name = "第" + (k + 1) + "知识点";
                        addSmallSectionTest.order = k + 1;
                        addSectionTest.small_sections.add(addSmallSectionTest);
                    }
                    addChapterTest.sections.add(addSectionTest);
                }
                chapters.add(addChapterTest);
            }
            InsertCourseChapterCompletedTest insertCourseChapterCompletedTest = new InsertCourseChapterCompletedTest();
            insertCourseChapterCompletedTest.chapters = chapters;
            insertCourseChapterCompletedTest.course_id = courseId;
            HttpRequest.post(nodeTool + "/insert_course_completed")
                    .connectTimeout(5000)
                    .readTimeout(5000)
                    .contentType(HttpRequest.CONTENT_TYPE_JSON)
                    .send(JSON.toJSONString(insertCourseChapterCompletedTest))
                    .body();
            return courseId;
        }
        return "";
    }

    static class ModifyCourseStatusTest {

        public String owner_id;

        public String course_id;

        public String start;

        public String end;

        public List<String> user_id_list;

        public Integer status;

    }

    // 添加多门管理员内置课程
    @Test
    void addMoreAdminCourse() {
        for (int i = 0; i < 1; i++) {
            String id = addAdminCourse();
            ModifyCourseStatusTest modifyCourseStatusTest = new ModifyCourseStatusTest();
            modifyCourseStatusTest.course_id = id;
            modifyCourseStatusTest.status = 1;
            modifyCourseStatusTest.owner_id = "3234234";
            HttpRequest.post(nodeTool + "/modify_course_status")
                    .connectTimeout(5000)
                    .readTimeout(5000)
                    .contentType(HttpRequest.CONTENT_TYPE_JSON)
                    .send(JSON.toJSONString(modifyCourseStatusTest))
                    .body();
        }
    }


}
