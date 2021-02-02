package org.tdos.tdospractice.service;

import javafx.util.Pair;
import org.tdos.tdospractice.body.DeleteChapter;
import org.tdos.tdospractice.body.InsertChapter;

public interface ChapterService {

    Pair<Boolean,String> modifyChapterNameById(String id, String chapterName, String introduction);

    Pair<Boolean, String> addChapter(InsertChapter insertChapter);

    Pair<Boolean, String> removeChapter(DeleteChapter deleteChapter);

    Pair<Boolean, Object> getChapter(String chapterId);
}
