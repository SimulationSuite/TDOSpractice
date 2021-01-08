package org.tdos.tdospractice.service;

import javafx.util.Pair;
import org.tdos.tdospractice.body.InsertChapter;

public interface ChapterService {

    Pair<Boolean,String> modifyChapterNameById(String id, String chapterName, String introduction);

    Pair<Boolean, String> addChapter(InsertChapter insertChapter);
}
