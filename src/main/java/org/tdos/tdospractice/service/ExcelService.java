package org.tdos.tdospractice.service;

import org.tdos.tdospractice.type.Response;

import java.io.IOException;
import java.io.InputStream;

public interface ExcelService {
    Response<String> uploadExcel(String userID, InputStream in, String filename) throws IOException;
    Response<String> uploadQbExcel(InputStream in, String filename) throws IOException;
    String getCategoryName();
}
