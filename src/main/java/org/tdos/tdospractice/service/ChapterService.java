package org.tdos.tdospractice.service;

import javafx.util.Pair;

public interface ChapterService {

    Pair<Boolean,String> modifyChapterNameById(String id, String chapterName, String introduction);

}
