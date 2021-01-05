package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.mapper.ChapterMapper;
import org.tdos.tdospractice.service.ChapterService;

import java.util.UUID;

@Service
public class ChapterServiceImpl implements ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;

    @Override
    public Pair<Boolean, String> modifyChapterNameById(String id, String chapterName, String introduction) {
        if (chapterMapper.hasChapter(id) > 0) {
            chapterMapper.modifyChapterNameById(id, chapterName, introduction);
            return new Pair<>(true, "");
        } else {
            return new Pair<>(false, "chapter id is not exist");
        }
    }

}
