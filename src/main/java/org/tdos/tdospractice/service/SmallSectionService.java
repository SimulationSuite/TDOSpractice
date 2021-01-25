package org.tdos.tdospractice.service;

import javafx.util.Pair;
import org.tdos.tdospractice.body.DeleteSmallSection;
import org.tdos.tdospractice.body.InsertSmallSection;

public interface SmallSectionService {
    Pair<Boolean, String> modifySmallSectionNameById(String sectionID, String smallSectionName);

    Pair<Boolean, String> addSmallSection(InsertSmallSection insertSmallSection);

    Pair<Boolean, String> removeSmallSection(DeleteSmallSection deleteSmallSection);
}
