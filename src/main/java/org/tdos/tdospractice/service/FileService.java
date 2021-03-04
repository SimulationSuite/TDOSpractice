package org.tdos.tdospractice.service;

import javafx.util.Pair;
import org.springframework.web.multipart.MultipartFile;
import org.tdos.tdospractice.body.UploadFile;

public interface FileService {

    Pair<Boolean, UploadFile> upload(MultipartFile file, int type);

    boolean download();

    boolean delete(String path, String fileName);

}
