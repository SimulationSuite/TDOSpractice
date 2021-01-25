package org.tdos.tdospractice.type;

import lombok.Getter;

import java.util.List;

@Getter
public class Chapter {

    public String id;

    public String name;

    public int order;

    public List<Section> sections;

}
