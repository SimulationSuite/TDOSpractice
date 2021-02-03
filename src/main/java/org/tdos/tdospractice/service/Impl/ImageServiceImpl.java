package org.tdos.tdospractice.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.SneakyThrows;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.tdos.tdospractice.entity.ImageEntity;
import org.tdos.tdospractice.kvm.KvmManager;
import org.tdos.tdospractice.mapper.ImageMapper;
import org.tdos.tdospractice.service.ImageService;
import org.tdos.tdospractice.utils.JsonUtils;

import java.util.List;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Qualifier("ImageQueue")
    @Autowired
    private ActiveMQQueue imageQueue;

    @Autowired
    private KvmManager kvmManager;

    private final JsonUtils jsonUtils = new JsonUtils();

    @Override
    public PageInfo<ImageEntity> getImageList(int kind, String imageName, int page, int perPage) {
        PageHelper.startPage(page, perPage);
        List<ImageEntity> list = imageMapper.findImageByKindAndName(kind, imageName);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<Map<String, Object>> getImagequoteList(int kind, String imageNmae, int page, int perPage) {
        PageHelper.startPage(page, perPage);
        List<Map<String, Object>> objectMap = imageMapper.findImagequoteByKindAndName(kind, imageNmae);
        return new PageInfo<>(objectMap);
    }

    @SneakyThrows
    @Override
    public int addImage(String imageName, String introduction, int kind) {
        int count = imageMapper.findImageByName(imageName);
        if (count > 0) {
            return -1;
        }
        ImageEntity imageEntity = ImageEntity.builder()
                .imageName(imageName).introduction(introduction)
                .type(0).kind(kind).build();
        String message = Hex.encodeHexString(jsonUtils.encode(imageEntity));
        jmsMessagingTemplate.convertAndSend(this.imageQueue, message);
        return 0;
    }

    @Override
    public int deleteImages(List<String> imagesID) {
        List<String> getlist = imageMapper.findExperimentImageByImageids(imagesID);
        if (getlist.size() > 0) {
            return -1;
        }
        if (!kvmManager.isExistImageID(imagesID)) {
            return -1;
        }
        if (kvmManager.isQuoteContainer(imagesID)) {
            return -1;
        }
        imageMapper.deleteImages(imagesID);
        kvmManager.removeImages(imagesID);
        return 0;
    }

}
