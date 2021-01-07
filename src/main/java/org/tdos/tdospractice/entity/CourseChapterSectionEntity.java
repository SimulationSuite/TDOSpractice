package org.tdos.tdospractice.entity;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class CourseChapterSectionEntity {

    private String courseId;

    private String chapterId;

    private String sectionId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
