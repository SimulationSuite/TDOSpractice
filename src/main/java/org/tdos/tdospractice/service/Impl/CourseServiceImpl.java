package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.tdos.tdospractice.body.*;
import org.tdos.tdospractice.entity.*;
import org.tdos.tdospractice.mapper.*;
import org.tdos.tdospractice.service.CourseService;
import org.tdos.tdospractice.service.FileService;
import org.tdos.tdospractice.type.*;
import org.tdos.tdospractice.type.Chapter;
import org.tdos.tdospractice.type.Section;
import org.tdos.tdospractice.type.SmallSection;
import org.tdos.tdospractice.utils.UUIDPattern;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ClassCourseMapper classCourseMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private CourseChapterSectionMapper courseChapterSectionMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private SmallSectionMapper smallSectionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CoursewareMapper coursewareMapper;

    @Autowired
    private QuestionBackMapper questionBackMapper;

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private ExperimentMapper experimentMapper;

    @Autowired
    private ChapterSectionExperimentMapper chapterSectionExperimentMapper;

    @Autowired
    private ExperimentImageMapper experimentImageMapper;


    private static final String EMPTY_UUID = "fb0a1080-b11e-427c-8567-56ca6105ea07";

    @Autowired
    private FileService fileService;

    @Override
    public PageInfo<Course> getAdminCourseList(Integer perPage, Integer page, String name) {
        PageHelper.startPage(page, perPage);
        PageHelper.orderBy("c.created_at desc");
        PageInfo<Course> pageInfo = new PageInfo<>(courseMapper.getAdminCourseList(name));
        if (pageInfo.getList().size() > 0) {
            List<Course> courses = courseMapper.getAdminCourseListPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            courses.forEach(x -> {
                x.chapters.forEach(s -> s.sections = s.sections.stream().sorted(Comparator.comparing(Section::getOrder)).collect(Collectors.toList()));
                x.chapters = x.chapters.stream().sorted(Comparator.comparing(Chapter::getOrder)).collect(Collectors.toList());
            });
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            courses.forEach(x -> countCourseClass(x, classNumbers));
            courses.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
                AtomicInteger smallSectionNumber = new AtomicInteger();
                c.chapters.forEach(chapter -> chapter.sections.forEach(section ->
                        smallSectionNumber.set(smallSectionNumber.get() + section.smallSections.size())));
                c.smallSectionNumber = smallSectionNumber.get();
            });
            pageInfo.setList(courses);
        }
        return pageInfo;
    }

    @Override
    public Pair<Boolean, Object> getAdminCourseListByClassId(String classId, Integer perPage, Integer page) {
        if (ObjectUtils.isEmpty(classId)) {
            return new Pair<>(false, "班级id是空");
        }
        if (!UUIDPattern.isValidUUID(classId)) {
            return new Pair<>(false, "班级id不是uuid");
        }
        PageHelper.startPage(page, perPage);
        PageHelper.orderBy("c.created_at desc");
        PageInfo<Course> pageInfo = new PageInfo<>(courseMapper.getAdminCourseListByClassId(classId));
        if (pageInfo.getList().size() > 0) {
            List<Course> list = courseMapper.getAdminCourseListByClassIdPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            list.forEach(x -> countCourseClass(x, classNumbers));
            list.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
            });
            pageInfo.setList(list);
        }
        return new Pair<>(true, pageInfo);
    }


    @Override
    public Pair<Boolean, PrepareCourseReturn> prepareCourse(PrepareCourse prepareCourse) {
        PrepareCourseReturn prepareCourseReturn = new PrepareCourseReturn();
        if (ObjectUtils.isEmpty(prepareCourse.courseId)) {
            prepareCourseReturn.setErrMessage("课程id不为空");
            return new Pair<>(false, prepareCourseReturn);
        }
        if (ObjectUtils.isEmpty(prepareCourse.userId)) {
            prepareCourseReturn.setErrMessage("userId不能为空");
            return new Pair<>(false, prepareCourseReturn);
        }
        if (!UUIDPattern.isValidUUID(prepareCourse.courseId)) {
            prepareCourseReturn.setErrMessage("课程id不是uuid");
            return new Pair<>(false, prepareCourseReturn);
        }
        if (courseMapper.hasCourseExist(prepareCourse.courseId) == 0) {
            prepareCourseReturn.setErrMessage("课程不存在");
            return new Pair<>(false, prepareCourseReturn);
        }
        Course course = courseMapper.getCourseByCourseId(prepareCourse.courseId);
        if (course == null || ObjectUtils.isEmpty(course.id)) {
            prepareCourseReturn.setErrMessage("课程不存在");
            return new Pair<>(false, prepareCourseReturn);
        }
        if (course.type == 1) {
            prepareCourseReturn.setErrMessage("课程不是管理员发布的");
            return new Pair<>(false, prepareCourseReturn);
        }
        String modelId = courseMapper.getModelCourse(prepareCourse.courseId, prepareCourse.userId);
        if (!ObjectUtils.isEmpty(modelId)) {
            prepareCourseReturn.setExist(true);
            prepareCourseReturn.setCourseId(modelId);
            return new Pair<>(true, prepareCourseReturn);
        }

        course.ownerId = prepareCourse.userId;
        course.type = 1;
        course.status = 0;
        course.modelId = course.id;
        course.startAt = null;
        course.endAt = null;
        Map<String, String> map = new HashMap<>();
        writeCourse(course, map);

        // 课件
        List<ChapterSectionCoursewareEntity> chapterSectionCoursewareEntityList =
                coursewareMapper.getChapterSectionCoursewareByCourseId(prepareCourse.courseId);
        List<ChapterSectionCoursewareEntity> list = new ArrayList<>();
        chapterSectionCoursewareEntityList.forEach(chapterSectionCoursewareEntity -> {
            if (!chapterSectionCoursewareEntity.getChapterId().equals(EMPTY_UUID)) {
                String preChapterId = chapterSectionCoursewareEntity.getChapterId();
                chapterSectionCoursewareEntity.setChapterId(map.get(preChapterId));
                list.add(chapterSectionCoursewareEntity);
            } else {
                String preSectionId = chapterSectionCoursewareEntity.getSectionId();
                chapterSectionCoursewareEntity.setSectionId(map.get(preSectionId));
                list.add(chapterSectionCoursewareEntity);
            }
        });
        if (list.size() > 0) {
            coursewareMapper.addChapterSectionCourseware(list);
        }


        // 作业
        Map<String, String> assignmentMap = new HashMap<>();
        List<AssignmentEntity> assignmentEntityList = assignmentMapper.getAssignmentByCourseId(prepareCourse.courseId);
        assignmentEntityList.forEach(assignmentEntity -> {
            String preSectionId = assignmentEntity.getSectionId();
            String preAssignmentId = assignmentEntity.getId();
            assignmentEntity.setStatus(0);
            assignmentEntity.setSectionId(map.get(preSectionId));
            assignmentMapper.addAssignment(assignmentEntity);
            assignmentMap.put(preAssignmentId, assignmentEntity.getId());
        });

        // 题库
        List<QuestionBackAssignmentEntity> questionBackAssignmentEntityList =
                questionBackMapper.getQuestionBackAssignmentByCourse(prepareCourse.courseId);
        List<QuestionBackAssignmentEntity> questionBackAssignmentEntityArrayList = new ArrayList<>();
        questionBackAssignmentEntityList.forEach(questionBackAssignmentEntity -> {
            questionBackAssignmentEntity.setAssignmentId(assignmentMap.get(questionBackAssignmentEntity.getAssignmentId()));
            questionBackAssignmentEntityArrayList.add(questionBackAssignmentEntity);
        });
        if (questionBackAssignmentEntityArrayList.size() > 0) {
            questionBackMapper.addQuestionBackAssignmentList(questionBackAssignmentEntityArrayList);
        }

        //实验
        List<ChapterSectionExperimentEntity> chapterSectionExperimentEntities =
                chapterSectionExperimentMapper.getChapterSectionExperimentByCourse(prepareCourse.courseId);
        chapterSectionExperimentEntities.forEach(chapterSectionExperimentEntity -> {
            String preExperimentId = chapterSectionExperimentEntity.getExperiment_id();
            List<ExperimentImageEntity> experimentImageEntities = experimentImageMapper
                    .findImageByExperiment(preExperimentId);

            ExperimentEntity experimentEntity = experimentMapper.findById(preExperimentId);
            experimentEntity.setType(1);
            experimentEntity.setParent_id(preExperimentId);
            experimentMapper.insert(experimentEntity);

            experimentImageEntities.forEach(experimentImageEntity ->
                    experimentImageEntity.setExperiment_id(experimentEntity.getId()));
            if (experimentImageEntities.size() > 0) {
                experimentImageMapper.insertExperimentImages(experimentImageEntities);
            }
            chapterSectionExperimentEntity.setExperiment_id(experimentEntity.getId());
            chapterSectionExperimentEntity.setSection_id(map.get(chapterSectionExperimentEntity.getSection_id()));
        });
        if (chapterSectionExperimentEntities.size() > 0) {
            chapterSectionExperimentMapper.insert(chapterSectionExperimentEntities);
        }
        prepareCourseReturn.setExist(true);
        prepareCourseReturn.setCourseId(course.id);
        return new Pair<>(true, prepareCourseReturn);
    }

    @Override
    public PageInfo<Course> getCourseListById(String userId, Integer perPage, Integer page, String name) {
//        PageHelper.orderBy();
        PageHelper.startPage(page, perPage, "c.created_at desc");
        List<Course> courses = courseMapper.getCourseListById(userId, name);
        PageInfo<Course> pageInfo = new PageInfo<>(courses);
        if (courses.size() > 0) {
            List<Course> coursesList = courseMapper.getCourseListByIdPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            coursesList.forEach(course -> {
                if (course.status != 0 && course.endAt != null && course.endAt.isBefore(LocalDateTime.now())) {
                    course.status = -1;
                }
            });
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            coursesList.forEach(x -> countCourseClass(x, classNumbers));
            coursesList.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
                AtomicInteger smallSectionNumber = new AtomicInteger();
                c.chapters.forEach(chapter -> chapter.sections.forEach(section ->
                        smallSectionNumber.set(smallSectionNumber.get() + section.smallSections.size())));
                c.smallSectionNumber = smallSectionNumber.get();
            });
            pageInfo.setList(coursesList);
        }
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public Pair<Boolean, Object> AddAdminCourse(AddCourse addCourse) {
        if (ObjectUtils.isEmpty(addCourse.name)) {
            return new Pair<>(false, "章名不存在");
        }
        if (courseMapper.hasCourseNameExist(addCourse.name) > 0) {
            return new Pair<>(false, "课程名程已存在");
        }
        if (ObjectUtils.isEmpty(addCourse.picUrl)) {
            return new Pair<>(false, "图片链接不存在");
        }
        if (ObjectUtils.isEmpty(addCourse.introduction)) {
            return new Pair<>(false, "课程简介不能为空");
        }
        if (ObjectUtils.isEmpty(addCourse.ownerId)) {
            return new Pair<>(false, "课程ownedId不存在");
        }
        if (addCourse.chapters == null) {
            return new Pair<>(false, "章不能为空");
        }
        Course course = new Course();
        course.name = addCourse.name;
        course.picUrl = addCourse.picUrl;
        course.ownerId = addCourse.ownerId;
        course.introduction = addCourse.introduction;
        course.chapters = addCourse.chapters.stream().map(x -> {
            Chapter chapter = new Chapter();
            chapter.name = x.name;
            chapter.order = x.order;
            chapter.sections = x.sections.stream().map(y -> {
                Section section = new Section();
                section.name = y.name;
                section.order = y.order;
                section.smallSections = y.smallSections.stream().map(z -> {
                    SmallSection smallSection = new SmallSection();
                    smallSection.name = z.name;
                    smallSection.order = z.order;
                    return smallSection;
                }).collect(Collectors.toList());
                return section;
            }).collect(Collectors.toList());
            return chapter;
        }).collect(Collectors.toList());

        return new Pair<>(true, writeCourse(course));
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public Pair<Boolean, String> modifyCourseStatus(ModifyCourseStatus modifyCourseStatus) {
        if (ObjectUtils.isEmpty(modifyCourseStatus.ownerId)) {
            return new Pair<>(false, "拥有者不能是空");
        }
        if (ObjectUtils.isEmpty(modifyCourseStatus.courseId)) {
            return new Pair<>(false, "课程id不能是空");
        }
        if (!UUIDPattern.isValidUUID(modifyCourseStatus.courseId)) {
            return new Pair<>(false, "课程id不是uuid");
        }
        if (courseMapper.hasCourseExist(modifyCourseStatus.courseId) == 0) {
            return new Pair<>(false, "课程id不存在");
        }
        Course course = courseMapper.getCourseByCourseId(modifyCourseStatus.courseId);
        if (!course.ownerId.equals(modifyCourseStatus.ownerId)) {
            return new Pair<>(false, "课程不属于拥有者: " + modifyCourseStatus.ownerId);
        }
        if (modifyCourseStatus.status != null && courseMapper.hasTeacherCourseNameExist(course.name, modifyCourseStatus.ownerId) > 0) {
            return new Pair<>(false, "课程名程已存在");
        }
        courseMapper.modifyCourseStatus(modifyCourseStatus.courseId, modifyCourseStatus.status == null || modifyCourseStatus.status == 0 ? 0 : 1, modifyCourseStatus.start, modifyCourseStatus.end);
        if (modifyCourseStatus.userIds != null && modifyCourseStatus.userIds.size() > 0) {
            if (!modifyCourseStatus.userIds.stream().allMatch((userId -> userMapper.findUserById(userId) != null
                    && userMapper.findUserById(userId).getRoleID() == 2))) {
                return new Pair<>(false, "学生列表不存在或者不全是学生");
            }
            classCourseMapper.deleteByCourseId(modifyCourseStatus.courseId);
            modifyCourseStatus.userIds.forEach(userId -> {
                UserEntity userEntity = userMapper.findUserById(userId);
                classCourseMapper.insertClassCourse(userId, modifyCourseStatus.courseId, userEntity.getClassID());
            });
        }
        return new Pair<>(true, "");
    }

    @Override
    public PageInfo<Course> getAdminCourseListByStatus(String userId, Integer perPage, Integer page, String name, Integer status) {
        PageHelper.startPage(page, perPage, "c.created_at desc");
        List<Course> courses = courseMapper.getAdminCourseListByStatus(userId, name, status);
        PageInfo<Course> list = new PageInfo<>(courses);
        if (list.getList().size() > 0) {
            List<Course> coursesList = courseMapper.getAdminUnpublishedCourseListPerfect(list.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            coursesList.forEach(x -> {
                x.chapters.forEach(s -> s.sections = s.sections.stream().sorted(Comparator.comparing(Section::getOrder)).collect(Collectors.toList()));
                x.chapters = x.chapters.stream().sorted(Comparator.comparing(Chapter::getOrder)).collect(Collectors.toList());
            });
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            coursesList.forEach(x -> countCourseClass(x, classNumbers));
            coursesList.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
                AtomicInteger smallSectionNumber = new AtomicInteger();
                c.chapters.forEach(chapter -> chapter.sections.forEach(section ->
                        smallSectionNumber.set(smallSectionNumber.get() + section.smallSections.size())));
                c.smallSectionNumber = smallSectionNumber.get();
            });
            list.setList(coursesList);
        }
        return list;
    }

    private Course writeCourse(Course course, Map<String, String> map) {
        courseMapper.insertCourse(course);
        course.chapters.forEach(x -> {
            List<CourseChapterSectionEntity> list = new ArrayList<>();
            String preChapterId = x.id;
            chapterMapper.insertChapter(x);
            map.put(preChapterId, x.id);
            if (x.sections.size() > 0) {
                x.sections.forEach(section -> {
                    String sectionId = section.id;
                    sectionMapper.insertSection(section);
                    map.put(sectionId, section.id);
                    section.smallSections.forEach(smallSection -> {
                        smallSectionMapper.insertSmallSection(smallSection);
                        list.add(CourseChapterSectionEntity.builder().courseId(course.id)
                                .chapterId(x.id)
                                .sectionId(section.id)
                                .smallSectionId(smallSection.id)
                                .build());
                    });
                    list.add(CourseChapterSectionEntity.builder().courseId(course.id)
                            .chapterId(x.id)
                            .sectionId(section.id)
                            .smallSectionId(EMPTY_UUID)
                            .build());
                });
            }
            list.add(CourseChapterSectionEntity.builder().courseId(course.id)
                    .chapterId(x.id)
                    .sectionId(EMPTY_UUID)
                    .smallSectionId(EMPTY_UUID)
                    .build());

            if (list.size() > 0) {
                courseChapterSectionMapper.insertCourseChapterSectionList(list);
            }
        });
        course.chapters.forEach(c -> {
            c.sections = c.sections.stream().sorted(Comparator.comparing(Section::getOrder)).collect(Collectors.toList());
            c.sections.forEach(section -> section.smallSections = section.smallSections.stream().sorted(Comparator.comparing(SmallSection::getOrder)).collect(Collectors.toList()));
        });
        course.chapters = course.chapters.stream().sorted(Comparator.comparing(Chapter::getOrder)).collect(Collectors.toList());
        return course;
    }


    private Course writeCourse(Course course) {
        courseMapper.insertCourse(course);
        course.chapters.forEach(x -> {
            List<CourseChapterSectionEntity> list = new ArrayList<>();
            chapterMapper.insertChapter(x);
            if (x.sections.size() > 0) {
                x.sections.forEach(section -> {
                    sectionMapper.insertSection(section);
                    section.smallSections.forEach(smallSection -> {
                        smallSectionMapper.insertSmallSection(smallSection);
                        list.add(CourseChapterSectionEntity.builder().courseId(course.id)
                                .chapterId(x.id)
                                .sectionId(section.id)
                                .smallSectionId(smallSection.id)
                                .build());
                    });
                    list.add(CourseChapterSectionEntity.builder().courseId(course.id)
                            .chapterId(x.id)
                            .sectionId(section.id)
                            .smallSectionId(EMPTY_UUID)
                            .build());
                });
            }
            list.add(CourseChapterSectionEntity.builder().courseId(course.id)
                    .chapterId(x.id)
                    .sectionId(EMPTY_UUID)
                    .smallSectionId(EMPTY_UUID)
                    .build());

            if (list.size() > 0) {
                courseChapterSectionMapper.insertCourseChapterSectionList(list);
            }
        });
        course.chapters.forEach(c -> {
            c.sections = c.sections.stream().sorted(Comparator.comparing(Section::getOrder)).collect(Collectors.toList());
            c.sections.forEach(section -> section.smallSections = section.smallSections.stream().sorted(Comparator.comparing(SmallSection::getOrder)).collect(Collectors.toList()));
        });
        course.chapters = course.chapters.stream().sorted(Comparator.comparing(Chapter::getOrder)).collect(Collectors.toList());
        return course;
    }

    @Override
    public PageInfo<Course> getCourseList(String userId, String name, String start, String end, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        PageHelper.orderBy("c.created_at desc");
        PageInfo<Course> pageInfo = new PageInfo<>(courseMapper.getCourseList(userId, name, start, end));
        if (pageInfo.getList().size() > 0) {
            List<Course> list = courseMapper.getCourseListPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            list.forEach(x -> countCourseClass(x, classNumbers));
            list.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
                AtomicInteger smallSectionNumber = new AtomicInteger();
                c.chapters.forEach(chapter -> chapter.sections.forEach(section ->
                        smallSectionNumber.set(smallSectionNumber.get() + section.smallSections.size())));
                c.smallSectionNumber = smallSectionNumber.get();
            });
            pageInfo.setList(list);
        }
        return pageInfo;
    }

    @Override
    public Pair<Boolean, Object> getCourseById(String courseId) {
        if (ObjectUtils.isEmpty(courseId)) {
            return new Pair<>(false, "课程id不能是空");
        }
        if (!UUIDPattern.isValidUUID(courseId)) {
            return new Pair<>(false, "课程id不是uuid");
        }
        Course course = courseMapper.getCourseById(courseId);
        if (course != null) {
            if (course.status != 0 && course.endAt != null && course.endAt.isBefore(LocalDateTime.now())) {
                course.status = -1;
            }
            course.chapters = course.chapters.stream().filter(chapter -> chapter.id != null).sorted(Comparator.comparing(x->x.order)).collect(Collectors.toList());
            course.chapters.forEach(chapter -> {
                chapter.sections = chapter.sections.stream().filter(section -> section.id != null).sorted(Comparator.comparing(x->x.order)).collect(Collectors.toList());
                chapter.sections.forEach(section -> {
                    section.smallSections = section.smallSections.stream().filter(smallSection -> smallSection.id != null).sorted(Comparator.comparing(x->x.order)).collect(Collectors.toList());
                });
            });
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            countCourseClass(course, classNumbers);

            course.chapterNumber = course.chapters.size();
            course.sectionNumber = course.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
            AtomicInteger smallSectionNumber = new AtomicInteger();
            course.chapters.forEach(chapter -> chapter.sections.forEach(section ->
                    smallSectionNumber.set(smallSectionNumber.get() + section.smallSections.size())));
            course.smallSectionNumber = smallSectionNumber.get();
        }
        return new Pair<>(true, course);
    }

    private void countCourseClass(Course course, List<ClassNumber> classNumbers) {
        List<ClassCourse> classCourses = classCourseMapper.findListByCourseId(course.id);
        List<Classes> classesList = new ArrayList<>();

        Map<String, List<ClassCourse>> map = classCourses.stream().collect(Collectors.groupingBy(ClassCourse::getClassId));

        map.forEach((x, y) -> {
            Classes classes = new Classes();
            classes.userIds = y.stream().map(ClassCourse::getUserId).collect(Collectors.toList());
            classes.classId = x;
            classes.completed = false;
            classNumbers.forEach(classNumber -> {
                if (classNumber.classId.equals(x) && classNumber.numbers == classes.userIds.size()) {
                    classes.completed = true;
                }
            });

            classesList.add(classes);
        });

        course.classesList = classesList;
        course.numbers = classCourses.size();
    }

    @Override
    public PageInfo<Course> getExpiredList(Integer perPage, Integer page, String name) {
        PageHelper.startPage(page, perPage);
        PageHelper.orderBy("c.created_at desc");

        PageInfo<Course> pageInfo = new PageInfo<>(courseMapper.getExpiredList(name));
        if (pageInfo.getList().size() > 0) {
            List<Course> list = courseMapper.getExpiredListPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            list.forEach(x -> countCourseClass(x, classNumbers));
            list.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
                AtomicInteger smallSectionNumber = new AtomicInteger();
                c.chapters.forEach(chapter -> chapter.sections.forEach(section ->
                        smallSectionNumber.set(smallSectionNumber.get() + section.smallSections.size())));
                c.smallSectionNumber = smallSectionNumber.get();
            });
            pageInfo.setList(list);
        }
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public Pair<Boolean, Object> AddAdminCourseCompleted(AddCourseCompleted addCourseCompleted) {
        if (ObjectUtils.isEmpty(addCourseCompleted.courseId)) {
            return new Pair<>(false, "课程id不能是空");
        }
        if (ObjectUtils.isEmpty(addCourseCompleted.chapters) || addCourseCompleted.chapters.size() == 0) {
            return new Pair<>(false, "章不能是空");
        }
        Course course = courseMapper.getCourseById(addCourseCompleted.courseId);
        course.chapters.forEach(chapter -> chapterMapper.removeChapter(chapter.id));
        course.chapters = addCourseCompleted.chapters.stream().map(x -> {
            Chapter chapter = new Chapter();
            chapter.name = x.name;
            chapter.order = x.order;
            chapter.sections = x.sections.stream().map(y -> {
                Section section = new Section();
                section.name = y.name;
                section.order = y.order;
                section.smallSections = y.smallSections.stream().map(z -> {
                    SmallSection smallSection = new SmallSection();
                    smallSection.name = z.name;
                    smallSection.order = z.order;
                    return smallSection;
                }).collect(Collectors.toList());
                return section;
            }).collect(Collectors.toList());
            return chapter;
        }).collect(Collectors.toList());
        List<CourseChapterSectionEntity> list = new ArrayList<>();
        course.chapters.forEach(x -> {
            chapterMapper.insertChapter(x);
            x.sections.forEach(section -> {
                sectionMapper.insertSection(section);
                section.smallSections.forEach(smallSection -> {
                    smallSectionMapper.insertSmallSection(smallSection);
                    list.add(CourseChapterSectionEntity.builder().courseId(course.id)
                            .chapterId(x.id)
                            .sectionId(section.id)
                            .smallSectionId(smallSection.id)
                            .build());
                });
            });
        });
        if (list.size() > 0) {
            courseChapterSectionMapper.insertCourseChapterSectionList(list);
            course.chapters.forEach(c -> {
                c.sections = c.sections.stream().sorted(Comparator.comparing(Section::getOrder)).collect(Collectors.toList());
                c.sections.forEach(section -> section.smallSections = section.smallSections.stream().sorted(Comparator.comparing(SmallSection::getOrder)).collect(Collectors.toList()));
            });
            course.chapters = course.chapters.stream().sorted(Comparator.comparing(Chapter::getOrder)).collect(Collectors.toList());
        }
        return new Pair<>(true, course);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public Pair<Boolean, String> insertCourseChapterCompleted(AddChapterCompleted addChapterCompleted) {
        if (ObjectUtils.isEmpty(addChapterCompleted.courseId)) {
            return new Pair<>(false, "课程id不能是空");
        }
        if (ObjectUtils.isEmpty(addChapterCompleted.chapter)) {
            return new Pair<>(false, "章不能是空");
        }
        if (!UUIDPattern.isValidUUID(addChapterCompleted.courseId)) {
            return new Pair<>(false, "课程id不是uuid");
        }
        Course course = courseMapper.getCourseById(addChapterCompleted.courseId);
        if (course.chapters.stream().map(Chapter::getId)
                .collect(Collectors.toList()).contains(addChapterCompleted.chapter.id)) {
            chapterMapper.removeChapter(addChapterCompleted.chapter.id);
        }
        Chapter chapter = new Chapter();
        chapter.name = addChapterCompleted.chapter.name;
        chapter.order = addChapterCompleted.chapter.order;
        chapter.sections = addChapterCompleted.chapter.sections.stream().map(y -> {
            Section section = new Section();
            section.name = y.name;
            section.order = y.order;
            section.smallSections = y.smallSections.stream().map(z -> {
                SmallSection smallSection = new SmallSection();
                smallSection.name = z.name;
                smallSection.order = z.order;
                return smallSection;
            }).collect(Collectors.toList());
            return section;
        }).collect(Collectors.toList());
        chapterMapper.insertChapter(chapter);
        List<CourseChapterSectionEntity> list = new ArrayList<>();
        if (chapter.sections.size() > 0) {
            chapter.sections.forEach(section -> {
                sectionMapper.insertSection(section);
                section.smallSections.forEach(smallSection -> {
                    smallSectionMapper.insertSmallSection(smallSection);
                    list.add(CourseChapterSectionEntity.builder().courseId(course.id)
                            .chapterId(chapter.id)
                            .sectionId(section.id)
                            .smallSectionId(smallSection.id)
                            .build());
                });
                list.add(CourseChapterSectionEntity.builder().courseId(course.id)
                        .chapterId(chapter.id)
                        .sectionId(section.id)
                        .smallSectionId(EMPTY_UUID)
                        .build());
            });
        }
        list.add(CourseChapterSectionEntity.builder().courseId(course.id)
                .chapterId(chapter.id)
                .sectionId(EMPTY_UUID)
                .smallSectionId(EMPTY_UUID)
                .build());

        if (list.size() > 0) {
            courseChapterSectionMapper.insertCourseChapterSectionList(list);
        }
        return new Pair<>(true, "");
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public Pair<Boolean, String> modifyExpiredCourseStatus(ModifyExpiredCourseStatus modifyExpiredCourseStatus) {
        if (ObjectUtils.isEmpty(modifyExpiredCourseStatus.ownerId)) {
            return new Pair<>(false, "拥有者不能是空");
        }
        courseMapper.modifyExpiredCourseStatus(modifyExpiredCourseStatus.ownerId);
        return new Pair<>(true, "");
    }

    @Override
    public PageInfo<Course> getChangedList(Integer perPage, Integer page, String name) {
        PageHelper.startPage(page, perPage);
        PageHelper.orderBy("c.created_at desc");
        PageInfo<Course> pageInfo = new PageInfo<>(courseMapper.getChangedList(name));
        if (pageInfo.getList().size() > 0) {
            List<Course> list = courseMapper.getChangedListPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            list.forEach(x -> countCourseClass(x, classNumbers));
            list.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
                AtomicInteger smallSectionNumber = new AtomicInteger();
                c.chapters.forEach(chapter -> chapter.sections.forEach(section ->
                        smallSectionNumber.set(smallSectionNumber.get() + section.smallSections.size())));
                c.smallSectionNumber = smallSectionNumber.get();
            });
            pageInfo.setList(list);
        }
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public Pair<Boolean, String> removeCourseById(DeleteCourse deleteCourse) {
        if (ObjectUtils.isEmpty(deleteCourse.ownerId)) {
            return new Pair<>(false, "拥有者不能是空");
        }
        if (ObjectUtils.isEmpty(deleteCourse.courseId)) {
            return new Pair<>(false, "课程id不能是空");
        }
        if (!UUIDPattern.isValidUUID(deleteCourse.courseId)) {
            return new Pair<>(false, "课程id不是uuid");
        }
        Course course = courseMapper.getCourseByCourseId(deleteCourse.courseId);
        if (!course.ownerId.equals(deleteCourse.ownerId)) {
            return new Pair<>(false, "课程不属于这个拥有者: " + deleteCourse.ownerId);
        }
        course.chapters.stream().filter(chapter -> !chapter.id.equals(EMPTY_UUID)).forEach(chapter -> {
            chapter.sections.stream().filter(section -> !section.id.equals(EMPTY_UUID)).forEach(section -> {
                sectionMapper.removeSection(section.id);
                section.smallSections.stream().filter(smallSection -> !smallSection.id.equals(EMPTY_UUID)).forEach(smallSection -> {
                    smallSectionMapper.removeSmallSection(smallSection.id);
                });
            });
            chapterMapper.removeChapter(chapter.id);
        });
        courseMapper.removeCourse(deleteCourse.courseId);
        // 删除课程图片
        fileService.delete(course.picUrl);
        return new Pair<>(true, "");
    }

    @Override
    public Pair<Boolean, String> modifyCourseName(ModifyCourseName modifyCourseName) {
        if (courseMapper.hasCourseExist(modifyCourseName.courseId) == 0) {
            return new Pair<>(false, "课程不存在");
        }
        Course course = courseMapper.getCourseByCourseId(modifyCourseName.courseId);
        if (!course.ownerId.equals(modifyCourseName.ownerId)) {
            return new Pair<>(false, "课程不属于这个拥有者: " + modifyCourseName.ownerId);
        }
        courseMapper.modifyCourseName(modifyCourseName.courseId, modifyCourseName.courseName);
        return new Pair<>(true, "");
    }

    @Override
    public PageInfo<Course> getPublicCourseListById(String userId, Integer perPage, Integer page, String name) {
        PageHelper.startPage(page, perPage, "c.created_at desc");
        List<Course> courses = courseMapper.getPublicCourseListById(userId, name);
        PageInfo<Course> pageInfo = new PageInfo<>(courses);
        if (courses.size() > 0) {
            List<Course> coursesList = courseMapper.getCourseListByIdPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            coursesList.forEach(course -> {
                if (course.status != 0 && course.endAt != null && course.endAt.isBefore(LocalDateTime.now())) {
                    course.status = -1;
                }
            });
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            coursesList.forEach(x -> countCourseClass(x, classNumbers));
            coursesList.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
                AtomicInteger smallSectionNumber = new AtomicInteger();
                c.chapters.forEach(chapter -> chapter.sections.forEach(section ->
                        smallSectionNumber.set(smallSectionNumber.get() + section.smallSections.size())));
                c.smallSectionNumber = smallSectionNumber.get();
            });
            pageInfo.setList(coursesList);
        }
        return pageInfo;
    }

}
