package org.tdos.tdospractice.kvm;

import com.github.dockerjava.api.model.Image;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.tdos.tdospractice.entity.ImageEntity;
import org.tdos.tdospractice.mapper.ImageMapper;
import org.tdos.tdospractice.utils.JsonUtils;

import java.util.List;

@Component
@Slf4j(topic = "Message-Listener")
public class ImageMessageListener {

    @Autowired
    private KvmManager kvmManager;

    @Autowired
    private ImageMapper imageMapper;

    private final JsonUtils jsonUtils = new JsonUtils();

    @JmsListener(destination = "image-Queue")
    public void execMessage(String message) {
        ImageEntity imageEntity = jsonUtils.decode(Hex.decode(message), ImageEntity.class);
        //pull image
        kvmManager.pullImages(imageEntity.getImageName());
        List<Image> imageList = kvmManager.getImageInfo(imageEntity.getImageName());
        if (imageList.isEmpty() || imageList.size() < kvmManager.getToolSize()) {
            log.error("Image name is " + imageEntity.getImageName() + ", Failed to push");
            return;
        }
        imageEntity.setImageId(imageList.get(0).getId());
        imageEntity.setSize(imageList.get(0).getSize());
        //insert db
        imageMapper.addImage(imageEntity);
        log.info("Image name is " + imageEntity.getImageName() + ", Push success");
    }
}
