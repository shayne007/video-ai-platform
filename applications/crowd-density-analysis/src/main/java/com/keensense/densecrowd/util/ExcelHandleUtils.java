package com.keensense.densecrowd.util;


import com.keensense.common.platform.bo.video.CrowdDensity;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.text.*;
import java.util.*;

/**
 * 操作Excel表格的功能类
 */
public final class ExcelHandleUtils {

    /**
     * @描述：验证excel文件
     * @参数：@param filePath　文件完整路径
     * @参数：@return
     * @返回值：boolean
     */

    public boolean validateExcel(String filePath) {

        /** 检查文件名是否为空或者是否是Excel格式的文件 */

        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            return false;

        }

        /** 检查文件是否存在 */

        File file = new File(filePath);

        if (file == null || !file.exists()) {
            return false;

        }

        return true;

    }

    public static boolean isExcel2003(String filePath) {

        return filePath.matches("^.+\\.(?i)(xls)$");

    }


    /**
     * @描述：是否是2007的excel，返回true是2007
     * @参数：@param filePath　文件完整路径
     * @参数：@return
     * @返回值：boolean
     */

    public static boolean isExcel2007(String filePath) {

        return filePath.matches("^.+\\.(?i)(xlsx)$");

    }

    /**
     * 读取Excel，兼容 Excel 2003/2007/2010
     *
     * @param filePath
     * @return
     */
    public static String readExcel(String filePath) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //同时支持Excel 2003、2007
            File excelFile = new File(filePath); //创建文件对象
            FileInputStream is = new FileInputStream(excelFile); //文件流
            Workbook workbook = WorkbookFactory.create(is); //这种方式 Excel 2003/2007/2010 都是可以处理的
            int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量

            //遍历每个Sheet
            for (int s = 0; s < sheetCount; s++) {
                Sheet sheet = workbook.getSheetAt(s);
                int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数

                //遍历每一行  即每一条记录
                for (int r = 0; r < rowCount; r++) {
                    Row row = sheet.getRow(r);
                    int cellCount = row.getPhysicalNumberOfCells(); //获取总列数

                    //遍历每一列
                    for (int c = 0; c < cellCount; c++) {
                        Cell cell = row.getCell(c);
                        int cellType = cell.getCellType();
                        String cellValue = null;
                        switch (cellType) {
                            case Cell.CELL_TYPE_STRING: //文本
                                cellValue = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC: //数字、日期
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    cellValue = fmt.format(cell.getDateCellValue()); //日期型
                                } else {
                                    cellValue = String.valueOf(Math.round(cell.getNumericCellValue())); //数字
                                }
                                break;
                            case Cell.CELL_TYPE_BOOLEAN: //布尔型
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case Cell.CELL_TYPE_BLANK: //空白
                                cellValue = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_ERROR: //错误
                                cellValue = "错误";
                                break;
                            case Cell.CELL_TYPE_FORMULA: //公式
                                cellValue = "错误";
                                break;
                            default:
                                cellValue = "错误";
                        }

                        System.out.print(cellValue + "    ");
                    }
                    System.out.println();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void exportCrowdExcel(HSSFWorkbook workbook, int sheetNum, String sheetTitle, String[] headers,
                                         List<CrowdDensity> resultList, OutputStream out) throws Exception {
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetTitle);

        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);

        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);


        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell((short) i);

            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text.toString());
        }

        // 遍历集合数据，产生数据行
        if (ValidateHelper.isNotEmptyList(resultList)) {
            int index = 1;
            for (CrowdDensity resultBo : resultList) {
                row = sheet.createRow(index);

                // 监控点
                HSSFCell cell_00 = row.createCell(0);
                cell_00.setCellStyle(style2);
                HSSFRichTextString richString = new HSSFRichTextString(resultBo.getCameraName());
                cell_00.setCellValue(richString);

                // 人数
                HSSFCell cell_01 = row.createCell(1);
                cell_01.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCount() + "");
                cell_01.setCellValue(richString);

                // 经过时间
                HSSFCell cell_02 = row.createCell(2);
                cell_02.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCreateTime());
                cell_02.setCellValue(richString);


                //目标图
                HSSFCellStyle linkStyle = workbook.createCellStyle();
                HSSFCell cell_3 = row.createCell(3);
                cell_3.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell_3.setCellStyle(linkStyle);
                String imageUrl = resultBo.getPictureLocalPath();
                if (StringUtils.isNotEmptyString(imageUrl)) {
                    cell_3.setCellFormula("HYPERLINK(REPLACE(SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\")," +
                            "\"[\",\"\"),FIND(\"ExportResult\",SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\")," +
                            "\"[\",\"\")),50,\"" + imageUrl.replace("\\", "/") + "\")," +
                            "\"" + "图片超链接" + "\")\n");
                } else {
                    cell_3.setCellValue("没有快照");
                }


                index++;
            }
        }
    }
}