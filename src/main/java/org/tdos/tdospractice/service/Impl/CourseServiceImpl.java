package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.tdos.tdospractice.body.AddCourse;
import org.tdos.tdospractice.body.AddCourseCompleted;
import org.tdos.tdospractice.body.ModifyCourseStatus;
import org.tdos.tdospractice.body.PrepareCourse;
import org.tdos.tdospractice.entity.CourseChapterSectionEntity;
import org.tdos.tdospractice.mapper.*;
import org.tdos.tdospractice.service.CourseService;
import org.tdos.tdospractice.type.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    @Override
    public PageInfo<Course> getAdminCourseList(Integer perPage, Integer page, String name) {
        PageHelper.startPage(page, perPage);
        PageInfo<Course> pageInfo = new PageInfo<>(courseMapper.getAdminCourseList(name));
        if (pageInfo.getList().size() > 0) {
            List<Course> courses = courseMapper.getAdminCourseListPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            courses.forEach(x -> {
                x.chapters.forEach(s -> s.sections = s.sections.stream().sorted(Comparator.comparing(Section::getOrder)).collect(Collectors.toList()));
                x.chapters = x.chapters.stream().sorted(Comparator.comparing(Chapter::getOrder)).collect(Collectors.toList());
            });
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            courses.forEach(x -> classNumbers.forEach(classNumber -> {
                if (!ObjectUtils.isEmpty(x.classId) && x.classId.equals(classNumber.classId)) {
                    x.numbers = classNumber.numbers;
                }
            }));
            courses.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
            });
            pageInfo.setList(courses);
        }
        return pageInfo;
    }

    @Override
    public PageInfo<Course> getAdminCourseListByClassId(String classId, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        PageInfo<Course> pageInfo = new PageInfo<>(courseMapper.getAdminCourseListByClassId(classId));
        if (pageInfo.getList().size() > 0) {
            List<Course> list = courseMapper.getAdminCourseListByClassIdPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            list.forEach(x -> classNumbers.forEach(classNumber -> {
                if (!ObjectUtils.isEmpty(x.classId) && x.classId.equals(classNumber.classId)) {
                    x.numbers = classNumber.numbers;
                }
            }));
            list.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
            });
            pageInfo.setList(list);
        }
        return pageInfo;
    }

    @Override
    public Pair<Boolean, String> prepareCourse(PrepareCourse prepareCourse) {
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
        course.ownerId = prepareCourse.user_id;
        course.type = 1;
        course.status = 0;
        course.modelId = course.id;
        course.startAt = null;
        course.endAt = null;
        writeCourse(course);
        return new Pair<>(true, "");
    }

    @Override
    public PageInfo<Course> getCourseListById(String userId, Integer perPage, Integer page, String name) {
        PageHelper.startPage(page, perPage);
        List<Course> courses = courseMapper.getCourseListById(userId, name);
        PageInfo<Course> pageInfo = new PageInfo<>(courses);
        if (courses.size() > 0) {
            List<Course> coursesList = courseMapper.getCourseListByIdPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            coursesList.forEach(x -> classNumbers.forEach(classNumber -> {
                if (!ObjectUtils.isEmpty(x.classId) && x.classId.equals(classNumber.classId)) {
                    x.numbers = classNumber.numbers;
                }
            }));
            coursesList.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
            });
            pageInfo.setList(coursesList);
        }
        return pageInfo;
    }

    @Override
    public Course AddAdminCourse(AddCourse addCourse) {
        Course course = new Course();
        course.name = addCourse.name;
        course.picUrl = addCourse.picUrl;
        course.ownerId = addCourse.ownerId;
        course.introduction = addCourse.introduction;
        course.chapters = addCourse.chapters.stream().map(x -> {
            Chapter chapter = new Chapter();
            chapter.introduction = x.introduction;
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
        return writeCourse(course);
    }

    @Override
    public Pair<Boolean, String> modifyCourseStatus(ModifyCourseStatus modifyCourseStatus) {
        if (courseMapper.hasCourseExist(modifyCourseStatus.courseId) == 0) {
            return new Pair<>(false, "course is not exist");
        }
        Course course = courseMapper.getCourseByCourseId(modifyCourseStatus.courseId);
        if (!course.ownerId.equals(modifyCourseStatus.userId)) {
            return new Pair<>(false, "course is not belong to userId: " + modifyCourseStatus.userId);
        }
        courseMapper.modifyCourseStatus(modifyCourseStatus.courseId, modifyCourseStatus.start, modifyCourseStatus.end);
        if (!ObjectUtils.isEmpty(modifyCourseStatus.classId)) {
            classCourseMapper.insertClassCourse(modifyCourseStatus.courseId, modifyCourseStatus.classId);
        }
        return new Pair<>(true, "");
    }

    @Override
    public PageInfo<Course> getAdminUnpublishedCourseList(String userId, Integer perPage, Integer page, String name) {
        PageHelper.startPage(page, perPage, true, true);
        List<Course> courses = courseMapper.getAdminUnpublishedCourseList(userId, name);
        PageInfo<Course> list = new PageInfo<>(courses);
        if (list.getList().size() > 0) {
            List<Course> coursesList = courseMapper.getAdminUnpublishedCourseListPerfect(list.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            coursesList.forEach(x -> {
                x.chapters.forEach(s -> s.sections = s.sections.stream().sorted(Comparator.comparing(Section::getOrder)).collect(Collectors.toList()));
                x.chapters = x.chapters.stream().sorted(Comparator.comparing(Chapter::getOrder)).collect(Collectors.toList());
            });
            coursesList = coursesList.stream().sorted(Comparator.comparing(x -> x.name)).collect(Collectors.toList());
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            coursesList.forEach(x -> classNumbers.forEach(classNumber -> {
                if (!ObjectUtils.isEmpty(x.classId) && x.classId.equals(classNumber.classId)) {
                    x.numbers = classNumber.numbers;
                }
            }));
            coursesList.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
            });
            list.setList(coursesList);
        }
        return list;
    }


    private Course writeCourse(Course course) {
        courseMapper.insertCourse(course);
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
        return course;
    }

    @Override
    public PageInfo<Course> getCourseList(String userId, String start, String end, Integer perPage, Integer page) {
        PageHelper.startPage(page, perPage);
        PageInfo<Course> pageInfo = new PageInfo<>(courseMapper.getCourseList(userId, start, end));
        if (pageInfo.getList().size() > 0) {
            List<Course> list = courseMapper.getCourseListPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            list.forEach(x -> classNumbers.forEach(classNumber -> {
                if (!ObjectUtils.isEmpty(x.classId) && x.classId.equals(classNumber.classId)) {
                    x.numbers = classNumber.numbers;
                }
            }));
            list.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
            });
            pageInfo.setList(list);
        }
        return pageInfo;
    }

    @Override
    public Course getCourseById(String courseId) {
        Course course = courseMapper.getCourseById(courseId);
        List<ClassNumber> classNumbers = classMapper.findClassNumber();
        classNumbers.forEach(classNumber -> {
            if (!ObjectUtils.isEmpty(course.classId) && course.classId.equals(classNumber.classId)) {
                course.numbers = classNumber.numbers;
            }
        });
        course.chapterNumber = course.chapters.size();
        course.sectionNumber = course.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
        return course;
    }

    @Override
    public PageInfo<Course> getExpiredList(Integer perPage, Integer page, String name) {
        PageHelper.startPage(page, perPage);
        PageInfo<Course> pageInfo = new PageInfo<>(courseMapper.getExpiredList(name));
        if (pageInfo.getList().size() > 0) {
            List<Course> list = courseMapper.getExpiredListPerfect(pageInfo.getList().stream().map(x -> x.id).collect(Collectors.toList()));
            List<ClassNumber> classNumbers = classMapper.findClassNumber();
            list.forEach(x -> classNumbers.forEach(classNumber -> {
                if (!ObjectUtils.isEmpty(x.classId) && x.classId.equals(classNumber.classId)) {
                    x.numbers = classNumber.numbers;
                }
            }));
            list.forEach(c -> {
                c.chapterNumber = c.chapters.size();
                c.sectionNumber = c.chapters.stream().mapToInt(chapter -> chapter.getSections().size()).sum();
            });
            pageInfo.setList(list);
        }
        return pageInfo;
    }

    @Override
    public Course AddAdminCourseCompleted(AddCourseCompleted addCourseCompleted) {
        Course course = courseMapper.getCourseById(addCourseCompleted.courseId);
        course.chapters = addCourseCompleted.chapters.stream().map(x -> {
            Chapter chapter = new Chapter();
            chapter.introduction = x.introduction;
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
        return course;
    }

}
