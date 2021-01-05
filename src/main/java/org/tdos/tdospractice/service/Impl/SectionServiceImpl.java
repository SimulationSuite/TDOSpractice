package org.tdos.tdospractice.service.Impl;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.mapper.SectionMapper;
import org.tdos.tdospractice.service.SectionService;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionMapper sectionMapper;

    @Override
    public Pair<Boolean, String> modifySectionNameById(String id, String sectionName) {
        if (sectionMapper.hasSection(id) > 0) {
            sectionMapper.modifySectionNameById(id, sectionName);
            return new Pair<>(true, "");
        } else {
            return new Pair<>(false, "chapter id is not exist");
        }
    }

}
