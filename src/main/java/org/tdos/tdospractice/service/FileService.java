package org.tdos.tdospractice.service;

import javafx.util.Pair;
import org.springframework.web.multipart.MultipartFile;
import org.tdos.tdospractice.body.UploadFile;

public interface FileService {

    Pair<Boolean,String> upload(MultipartFile file, int type);

    boolean download();

}
