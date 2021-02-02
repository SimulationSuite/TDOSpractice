package org.tdos.tdospractice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tdos.tdospractice.entity.ImageEntity;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ImageMapper {

    List<ImageEntity> findImageByKindAndName(int kind, String imageName);

    List<Map<String, Object>> findImagequoteByKindAndName(int kind, String imageName);

    int findImageByName(String imageName);

    int addImage(ImageEntity imageEntity);

    void deleteImages(@Param("imagesID") List<String> imagesID);

    List<String> findExperimentImageByImageids(@Param("imageids") List<String> imageids);

    List<ImageEntity> findImageByExperimentId(@Param("experimentId") String experimentId);
}
