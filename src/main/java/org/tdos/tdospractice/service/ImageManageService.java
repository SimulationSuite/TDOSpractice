package org.tdos.tdospractice.service;

import org.tdos.tdospractice.entity.ImageEntity;

import java.util.List;
import java.util.Map;

public interface ImageManageService {

    List<ImageEntity> getImageList(int kind, String imageName, int page, int perPage);

    List<Map<String, Object>> getImagequoteList(int kind, String imageName, int page, int perPage);

    int addImage(String imageName, String introduction);

    int deleteImages(List<String> imagesID);
}
