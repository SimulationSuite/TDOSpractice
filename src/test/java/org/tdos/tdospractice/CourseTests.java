package org.tdos.tdospractice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.kevinsawicki.http.HttpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.tdos.tdospractice.body.AddChapter;
import org.tdos.tdospractice.body.ChapterSectionCourseware;

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

    static class AddCourseWareTest{
        public String id;

        public String name;

        public Integer type;

        public Integer kind;

        public String url;

        public Integer duration;

        public Integer size;

        public String categoryId;
    }

    static class AddChapterSectionCourseWareTest{
        public List<ChapterSectionCourseware> chapterSectionCoursewareList;
    }

    static class ChapterSectionCourseware{
        public String chapterId;

        public String sectionId;

        public String coursewareId;
    }

    String getPublicAdminCourse() {
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
                AddCourseWareTest addCourseWare = new AddCourseWareTest();
                addCourseWare.name = "课件"+UUID.randomUUID();
                addCourseWare.type = 0;
                addCourseWare.kind = 0;
                addCourseWare.url = "/video/3ec310c5-7767-4cc6-a2e1-896b39307018.mp4";
                addCourseWare.duration = 122;
                addCourseWare.size = 12231;
                addCourseWare.categoryId = "73cab2fb-3aa6-4031-b3f4-e9da75ba1cf7";
                String coursewareBody = HttpRequest.post(nodeTool + "/addCourseware")
                        .connectTimeout(5000)
                        .readTimeout(5000)
                        .contentType(HttpRequest.CONTENT_TYPE_JSON)
                        .send(JSON.toJSONString(addCourseWare))
                        .body();
                JSONObject courseWare = JSONObject.parseObject(coursewareBody);
                if (courseWare.getInteger("code") == 200) {
                    String courseWareId = courseWare.getJSONObject("data").getString("id");
                    AddChapterSectionCourseWareTest addChapterSectionCourseWare = new AddChapterSectionCourseWareTest();
                    ChapterSectionCourseware chapterSectionCourseware = new ChapterSectionCourseware();
                    chapterSectionCourseware.chapterId = courseId;
                    chapterSectionCourseware.sectionId = "fb0a1080-b11e-427c-8567-56ca6105ea07";
                    chapterSectionCourseware.coursewareId = courseWareId;
                    HttpRequest.post(nodeTool + "/addChapterSectionCourseware")
                            .connectTimeout(5000)
                            .readTimeout(5000)
                            .contentType(HttpRequest.CONTENT_TYPE_JSON)
                            .send(JSON.toJSONString(addChapterSectionCourseWare))
                            .body();
                }
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

    @Test
    void addAdminCourse() {
        getPublicAdminCourse();
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
    void addMorePublicAdminCourse() {
        for (int i = 0; i < 10; i++) {
            String id = getPublicAdminCourse();
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

    static class PrepareCourseTest {

        public String course_id;

        public String user_id;

    }

    // 老师备课课程
    String getPrepareTeacherCourse() {
        String id = getPublicAdminCourse();
        PrepareCourseTest prepareCourseTest = new PrepareCourseTest();
        prepareCourseTest.course_id = id;
        prepareCourseTest.user_id = "123123";
        String body = HttpRequest.post(nodeTool + "/prepare_course")
                .connectTimeout(5000)
                .readTimeout(5000)
                .contentType(HttpRequest.CONTENT_TYPE_JSON)
                .send(JSON.toJSONString(prepareCourseTest))
                .body();
        JSONObject jo = JSONObject.parseObject(body);
        if (jo.getInteger("code") == 200) {
            return jo.getJSONObject("data").getString("course_id");
        }
        return "";
    }

    // 老师备课多门课程
    @Test
    void addMorePrepareTeacherCourse() {
        for (int i = 0; i < 1; i++) {
            getPrepareTeacherCourse();
        }
    }

    // 老师发布多门课程
    @Test
    void addMorePublicTeacherCourse() {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            String id = getPrepareTeacherCourse();
            ids.add(id);
            System.out.println("老师新建课程id：" + id);
        }
        ids.forEach(id -> {
            ModifyCourseStatusTest modifyCourseStatusTest = new ModifyCourseStatusTest();
            modifyCourseStatusTest.owner_id = "123123"; // 老師的id
            modifyCourseStatusTest.status = 1;
            modifyCourseStatusTest.course_id = id;
            modifyCourseStatusTest.start = "2021-02-22 07:46:37";
            modifyCourseStatusTest.end = "2021-03-22 07:46:37";
            List<String> userList = new ArrayList<>();
            userList.add("1000000"); // 添加学生
            modifyCourseStatusTest.user_id_list = userList;
            HttpRequest.post(nodeTool + "/modify_course_status")
                    .connectTimeout(5000)
                    .readTimeout(5000)
                    .contentType(HttpRequest.CONTENT_TYPE_JSON)
                    .send(JSON.toJSONString(modifyCourseStatusTest))
                    .body();
        });
    }


}
