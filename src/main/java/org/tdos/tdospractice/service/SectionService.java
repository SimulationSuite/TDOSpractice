package org.tdos.tdospractice.service;

import javafx.util.Pair;


public interface SectionService {

    Pair<Boolean,String> modifySectionNameById(String id, String sectionName);

}
