package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.tdos.tdospractice.body.*;
import org.tdos.tdospractice.entity.ChapterSectionCoursewareEntity;
import org.tdos.tdospractice.entity.ClassCourse;
import org.tdos.tdospractice.entity.CourseChapterSectionEntity;
import org.tdos.tdospractice.entity.UserEntity;
import org.tdos.tdospractice.mapper.*;
import org.tdos.tdospractice.service.CourseService;
import org.tdos.tdospractice.type.*;
import org.tdos.tdospractice.type.Chapter;
import org.tdos.tdospractice.type.Section;
import org.tdos.tdospractice.type.SmallSection;
import org.tdos.tdospractice.utils.UUIDPattern;

import java.time.LocalDateTime;
import java.util.*;
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


    private static final String EMPTY_UUID = "fb0a1080-b11e-427c-8567-56ca6105ea07";

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
            });
            pageInfo.setList(courses);
        }
        return pageInfo;
    }

    @Override
    public Pair<Boolean, Object> getAdminCourseListByClassId(String classId, Integer perPage, Integer page) {
        if (ObjectUtils.isEmpty(classId)) {
            return new Pair<>(false, "class_id is not be null");
        }
        if (!UUIDPattern.isValidUUID(classId)) {
            return new Pair<>(false, "class_id is not be uuid");
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
    public Pair<Boolean, String> prepareCourse(PrepareCourse prepareCourse) {
        if (ObjectUtils.isEmpty(prepareCourse.courseId)) {
            return new Pair<>(false, "course_id can not be null");
        }
        if (ObjectUtils.isEmpty(prepareCourse.userId)) {
            return new Pair<>(false, "user_id  can not be null");
        }
        if (!UUIDPattern.isValidUUID(prepareCourse.courseId)) {
            return new Pair<>(false, "course_id is not be uuid");
        }
        if (courseMapper.hasCourseExist(prepareCourse.courseId) == 0) {
            return new Pair<>(false, "course is not exist");
        }
        Course course = courseMapper.getCourseByCourseId(prepareCourse.courseId);
        if (course == null || ObjectUtils.isEmpty(course.id)) {
            return new Pair<>(false, "select course is not exist");
        }
        if (course.type == 1) {
            return new Pair<>(false, "select course is not admin public");
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
        coursewareMapper.addChapterSectionCourseware(list);
        return new Pair<>(true, null);
    }

    @Override
    public PageInfo<Course> getCourseListById(String userId, Integer perPage, Integer page, String name) {
        PageHelper.startPage(page, perPage);
        PageHelper.orderBy("c.created_at desc");
        List<Course> courses = courseMapper.getCourseListById(userId, name);
        PageInfo<Course> pageInfo = new PageInfo<>(courses);
        if (courses.size() > 0) {
            List<Course> coursesList = courseMapper.getCourseListByIdPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            coursesList.forEach(x -> countCourseClass(x, classNumbers));
            coursesList.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
            });
            pageInfo.setList(coursesList);
        }
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public Pair<Boolean, Object> AddAdminCourse(AddCourse addCourse) {
        if (ObjectUtils.isEmpty(addCourse.name)) {
            return new Pair<>(false, "name can not be null");
        }
        if (courseMapper.hasCourseNameExist(addCourse.name) > 0) {
            return new Pair<>(false, "course name has been existed");
        }
        if (ObjectUtils.isEmpty(addCourse.picUrl)) {
            return new Pair<>(false, "pic_url  can not be null");
        }
        if (ObjectUtils.isEmpty(addCourse.introduction)) {
            return new Pair<>(false, "introduction  can not be null");
        }
        if (ObjectUtils.isEmpty(addCourse.ownerId)) {
            return new Pair<>(false, "owner_id can not be null");
        }
        if (addCourse.chapters == null) {
            return new Pair<>(false, "chapters can not be null");
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
            return new Pair<>(false, "owner_id can not be null");
        }
        if (ObjectUtils.isEmpty(modifyCourseStatus.courseId)) {
            return new Pair<>(false, "course_id  can not be null");
        }
        if (!UUIDPattern.isValidUUID(modifyCourseStatus.courseId)) {
            return new Pair<>(false, "course_id is not be uuid");
        }
        if (courseMapper.hasCourseExist(modifyCourseStatus.courseId) == 0) {
            return new Pair<>(false, "course is not exist");
        }
        Course course = courseMapper.getCourseByCourseId(modifyCourseStatus.courseId);
        if (!course.ownerId.equals(modifyCourseStatus.ownerId)) {
            return new Pair<>(false, "course is not belong to owner_id: " + modifyCourseStatus.ownerId);
        }
        courseMapper.modifyCourseStatus(modifyCourseStatus.courseId, modifyCourseStatus.status == null || modifyCourseStatus.status == 0 ? 0 : 1, modifyCourseStatus.start, modifyCourseStatus.end);
        if (modifyCourseStatus.userIds != null && modifyCourseStatus.userIds.size() > 0) {
            if (!modifyCourseStatus.userIds.stream().allMatch((userId -> userMapper.findUserById(userId) != null
                    && userMapper.findUserById(userId).getRoleID() == 2))) {
                return new Pair<>(false, "user_id list is not exist or not all student");
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
        PageHelper.startPage(page, perPage, true, true);
        PageHelper.orderBy("c.created_at desc");
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
            });
            list.setList(coursesList);
        }
        return list;
    }

    private Course writeCourse(Course course, Map<String, String> map) {
        courseMapper.insertCourse(course);
        List<CourseChapterSectionEntity> list = new ArrayList<>();
        course.chapters.forEach(x -> {
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
        if (list.size() > 0) {
            courseChapterSectionMapper.insertCourseChapterSectionList(list);
            course.chapters.forEach(c -> {
                c.sections = c.sections.stream().sorted(Comparator.comparing(Section::getOrder)).collect(Collectors.toList());
                c.sections.forEach(section -> section.smallSections = section.smallSections.stream().sorted(Comparator.comparing(SmallSection::getOrder)).collect(Collectors.toList()));
            });
            course.chapters = course.chapters.stream().sorted(Comparator.comparing(Chapter::getOrder)).collect(Collectors.toList());
        }
        return course;
    }


    private Course writeCourse(Course course) {
        courseMapper.insertCourse(course);
        List<CourseChapterSectionEntity> list = new ArrayList<>();
        course.chapters.forEach(x -> {
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
        if (list.size() > 0) {
            courseChapterSectionMapper.insertCourseChapterSectionList(list);
            course.chapters.forEach(c -> {
                c.sections = c.sections.stream().sorted(Comparator.comparing(Section::getOrder)).collect(Collectors.toList());
                c.sections.forEach(section -> section.smallSections = section.smallSections.stream().sorted(Comparator.comparing(SmallSection::getOrder)).collect(Collectors.toList()));
            });
            course.chapters = course.chapters.stream().sorted(Comparator.comparing(Chapter::getOrder)).collect(Collectors.toList());
        }
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
            });
            pageInfo.setList(list);
        }
        return pageInfo;
    }

    @Override
    public Pair<Boolean, Object> getCourseById(String courseId) {
        if (ObjectUtils.isEmpty(courseId)) {
            return new Pair<>(false, "course_id is not be null");
        }
        if (!UUIDPattern.isValidUUID(courseId)) {
            return new Pair<>(false, "course_id is not be uuid");
        }
        Course course = courseMapper.getCourseById(courseId);
        if (course != null) {
            course.chapters = course.chapters.stream().filter(chapter -> chapter.id != null).collect(Collectors.toList());
            course.chapters.forEach(chapter -> {
                chapter.sections = chapter.sections.stream().filter(section -> section.id != null).collect(Collectors.toList());
                chapter.sections.forEach(section -> {
                    section.smallSections = section.smallSections.stream().filter(smallSection -> smallSection.id != null).collect(Collectors.toList());
                });
            });
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            countCourseClass(course, classNumbers);

            course.chapterNumber = course.chapters.size();
            course.sectionNumber = course.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
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
            });
            pageInfo.setList(list);
        }
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public Pair<Boolean, Object> AddAdminCourseCompleted(AddCourseCompleted addCourseCompleted) {
        if (ObjectUtils.isEmpty(addCourseCompleted.courseId)) {
            return new Pair<>(false, "course_id is not be null");
        }
        if (ObjectUtils.isEmpty(addCourseCompleted.chapters) || addCourseCompleted.chapters.size() == 0) {
            return new Pair<>(false, "chapters is not be null");
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
            return new Pair<>(false, "course_id is not be null");
        }
        if (ObjectUtils.isEmpty(addChapterCompleted.chapter)) {
            return new Pair<>(false, "chapter is not be null");
        }
        if (!UUIDPattern.isValidUUID(addChapterCompleted.courseId)) {
            return new Pair<>(false, "course_id is not be uuid");
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
            return new Pair<>(false, "owner_id can not be null");
        }
        if (ObjectUtils.isEmpty(modifyExpiredCourseStatus.courseId)) {
            return new Pair<>(false, "course_id  can not be null");
        }
        if (!UUIDPattern.isValidUUID(modifyExpiredCourseStatus.courseId)) {
            return new Pair<>(false, "course_id is not be uuid");
        }
        if (courseMapper.hasCourseExist(modifyExpiredCourseStatus.courseId) == 0) {
            return new Pair<>(false, "course is not exist");
        }
        Course course = courseMapper.getCourseByCourseId(modifyExpiredCourseStatus.courseId);
        if (!course.ownerId.equals(modifyExpiredCourseStatus.ownerId)) {
            return new Pair<>(false, "course is not belong to owner_id: " + modifyExpiredCourseStatus.ownerId);
        }
        if (course.endAt == null || !course.endAt.isBefore(LocalDateTime.now())) {
            return new Pair<>(false, "end at is not been expired");
        }
        if (course.status != 1) {
            return new Pair<>(false, "course has not been teacher published or has been changed");
        }
        courseMapper.modifyExpiredCourseStatus(modifyExpiredCourseStatus.courseId, 2);
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
            });
            pageInfo.setList(list);
        }
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public Pair<Boolean, String> removeCourseById(DeleteCourse deleteCourse) {
        if (ObjectUtils.isEmpty(deleteCourse.ownerId)) {
            return new Pair<>(false, "owner_id can not be null");
        }
        if (ObjectUtils.isEmpty(deleteCourse.courseId)) {
            return new Pair<>(false, "course_id  can not be null");
        }
        if (!UUIDPattern.isValidUUID(deleteCourse.courseId)) {
            return new Pair<>(false, "course_id is not be uuid");
        }
        Course course = courseMapper.getCourseByCourseId(deleteCourse.courseId);
        if (!course.ownerId.equals(deleteCourse.ownerId)) {
            return new Pair<>(false, "course is not belong to owner_id: " + deleteCourse.ownerId);
        }
        course.chapters.stream().filter(chapter -> !chapter.id.equals(EMPTY_UUID)).forEach(x -> chapterMapper.removeChapter(x.id));
        courseMapper.removeCourse(deleteCourse.courseId);
        return new Pair<>(true, "");
    }

}
