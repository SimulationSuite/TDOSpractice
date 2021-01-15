package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tdos.tdospractice.service.ExcelService;
import org.tdos.tdospractice.service.SecurityService;
import org.tdos.tdospractice.type.Response;
import org.tdos.tdospractice.utils.ExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.tdos.tdospractice.utils.Constants.JWT_HEADER_KEY;

@RestController
public class ExcelController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ExcelService excelService;

    @GetMapping("/download_excel")
    public void exportExcel(HttpServletResponse response) throws Exception {
        ExcelUtils.createExcel(response, "template.xlsx", new String[]{"姓名","类型（教师、学生）","编号（学号、工号）","性别","系","年级","班级"});
    }

    @RequestMapping(value = "/upload_excel", method = RequestMethod.POST)
    @ResponseBody
    public Response<String> uploadExcel(@RequestPart("excel_file") MultipartFile uploadFile, @RequestHeader(JWT_HEADER_KEY) String jwt) throws IOException {
        String userID = securityService.getUserId(jwt);
        return excelService.uploadExcel(userID, uploadFile.getInputStream(), uploadFile.getName());
    }
}