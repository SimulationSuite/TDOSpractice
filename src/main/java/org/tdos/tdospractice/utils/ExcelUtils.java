package org.tdos.tdospractice.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.tdos.tdospractice.entity.CategoryEntity;
import org.tdos.tdospractice.type.Personnel;
import org.tdos.tdospractice.entity.QuestionBackEntity;
import org.tdos.tdospractice.mapper.CategoryMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelUtils {

    /**
     * 导出excel
     *
     * @param excelName 导出的excel路径（需要带.xlsx)
     * @param headList  excel的标题字段（与数据中map中键值对应）
     * @throws Exception
     */
    public static void createExcel(HttpServletResponse response, String excelName, String[] headList)
            throws Exception {
        // 创建新的Excel 工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 在Excel工作簿中建一工作表，其名为缺省值
        XSSFSheet sheet = workbook.createSheet();
        // 在索引0的位置创建行（最顶端的行）
        XSSFRow row = sheet.createRow(0);
        // 设置excel头（第一行）的头名称
        for (int i = 0; i < headList.length; i++) {

            // 在索引0的位置创建单元格（左上端）
            XSSFCell cell = row.createCell(i);
            // 定义单元格为字符串类型
            cell.setCellType(CellType.STRING);
            // 在单元格中输入一些内容
            cell.setCellValue(headList[i]);
        }
        // 把相应的Excel 工作簿存盘
        response.setContentType("application/octet-stream");
        //默认Excel名称
        response.setHeader("Content-Disposition", "attachment;fileName=" + excelName);
        workbook.write(response.getOutputStream());
    }

    private Workbook workbook;

    public ExcelUtils(InputStream is, String filename) throws IOException {
        try {
            if (filename.endsWith(".xls")) {
                // Excel2003及以前版本
                workbook = new HSSFWorkbook(is);
            } else {
                // Excel2007及以后版本
                workbook = new XSSFWorkbook(is);
            }
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 读取指定sheet页指定行数据
     *
     * @param sheetIx 指定sheet页，从0开始
     * @param start   指定开始行，从0开始
     * @param end     指定结束行，从0开始
     * @return List<List < String>>
     */
    public List<List<String>> read(int sheetIx, int start, int end) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        List<List<String>> rowList = new ArrayList<>();

        if (end > getRowCount(sheetIx)) {
            end = getRowCount(sheetIx);
        }
        // 第一行总列数
        int colNum = sheet.getRow(0).getLastCellNum();
        for (int i = start; i < end; i++) {
            List<String> colList = new ArrayList<>();
            Row row = sheet.getRow(i);
            for (int j = 0; j < colNum; j++) {
                if (row == null) {
                    colList.add(null);
                    continue;
                }
                colList.add(getCellValue(row.getCell(j)));
            }
            rowList.add(colList);
        }
        return rowList;
    }

    private String getCellValue(Cell cell) {
        String cellValue;
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            // 数字
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellValue = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                } else {
                    DataFormatter dataFormatter = new DataFormatter();
                    cellValue = dataFormatter.formatCellValue(cell);
                }
                break;
            // 字符串
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            // Boolean
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            // 公式
            case FORMULA:
                cellValue = cell.getCellFormula();
                break;
            // 空值
            case BLANK:
                cellValue = null;
                break;
            // 错误
            case ERROR:
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }


    public Integer getRowCount(int sheetIx) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        if (sheet.getPhysicalNumberOfRows() == 0) {
            return 0;
        }
        return sheet.getLastRowNum() + 1;
    }

    public List<Personnel> parsePersonnel(List<List<String>> lists) {
        return lists.stream().map(x -> {
            int gender = 0;
            if (StringUtils.hasText(x.get(3))) {
                gender = x.get(3).equals("男") ? 0 : 1;
            }
            int type;
            if (x.get(1).equals("教师")) {
                type = 1;
            } else if (x.get(1).equals("学生")) {
                type = 2;
            }else {
                type = 0;
            }
            return new Personnel(x.get(0), type, x.get(2), gender, x.get(4), x.get(5), x.get(6), x.get(7),x.get(8),x.get(9));
        }).collect(Collectors.toList());
    }

    public List<QuestionBackEntity> parseQuestionBack(List<List<String>> lists) {
        return lists.stream().map(x -> {
            int type;
            if (x.get(0).equals("选择题")) {
                type = 0;
            } else {
                type = 1;
            }
            String categoryId = "";
            QuestionBackEntity questionBackEntity = new QuestionBackEntity();
            questionBackEntity.setType(type);
            questionBackEntity.setContent(x.get(1));
            questionBackEntity.setChoice(x.get(2));
            questionBackEntity.setAnswer(x.get(3));
            questionBackEntity.setCategoryId(x.get(4));
            return questionBackEntity;
        }).collect(Collectors.toList());
    }

}
