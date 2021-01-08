package org.tdos.tdospractice.service;

import javafx.util.Pair;
import org.tdos.tdospractice.body.InsertSection;


public interface SectionService {

    Pair<Boolean,String> modifySectionNameById(String id, String sectionName);

    Pair<Boolean, String> addSection(InsertSection insertSection);
}
