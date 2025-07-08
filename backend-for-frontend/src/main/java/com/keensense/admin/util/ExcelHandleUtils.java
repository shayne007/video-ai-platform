package com.keensense.admin.util;


import com.keensense.admin.entity.sys.SysLog;
import com.keensense.admin.request.CameraRequest;
import com.keensense.admin.vo.ResultQueryVo;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 读取Excel，兼容 Excel 2003/2007/2010 区域
     *
     * @param filePath
     * @return
     */
    public static List<CameraRequest> readExcelForCamera(String filePath) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        List<CameraRequest> cameraList = new ArrayList<CameraRequest>();
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
                for (int r = 1; r < rowCount; r++) {
                    Row row = sheet.getRow(r);
                    int cellCount = row.getPhysicalNumberOfCells(); //获取总列数

                    CameraRequest camera = new CameraRequest();
                    //遍历每一列
                    for (int c = 1; c < cellCount; c++) {
                        Cell cell = row.getCell(c);
                        int cellType = Cell.CELL_TYPE_STRING;
                        String cellValue = null;
                        if (null != cell) {
                            cellType = cell.getCellType();
                            switch (cellType) {
                                case Cell.CELL_TYPE_STRING: //文本
                                    cellValue = cell.getStringCellValue();
                                    break;
                                case Cell.CELL_TYPE_NUMERIC: //数字、日期
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        cellValue = fmt.format(cell.getDateCellValue()); //日期型
                                    } else {
                                        cellValue = String.valueOf(cell.getNumericCellValue()); //数字
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
                        }


                        System.out.println(cellValue + "    ");

                        if (StringUtils.isNotEmptyString(cellValue)) {
                            switch (c) {
                                // 主键
                                case 1:
                                    camera.setExtcameraid(cellValue);
                                    break;

                                // ip
                                case 3:
                                    camera.setIp(cellValue);
                                    break;
                                // 通道
                                case 4:
                                    camera.setChannel(Long.valueOf(Math.round(Float.valueOf(cellValue))));
                                    break;
                                // 监控点名称
                                case 5:
                                    camera.setName(cellValue);
                                    break;

                                case 6:
                                    camera.setUrl(cellValue);
                                    break;

                                // 经度
                                case 7:
                                    camera.setLongitude(cellValue);
                                    break;
                                //纬度
                                case 8:
                                    camera.setLatitude(cellValue);
                                    break;
                            }
                        }

                    }
                    cameraList.add(camera);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cameraList;
    }

    /**
     * 读取Excel，兼容 Excel 2003/2007/2010 监控点导入fxm
     *
     * @param filePath
     * @return
     */
    public static List<CameraRequest> readExcelFromCamera(String filePath) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        List<CameraRequest> cameraList = new ArrayList<CameraRequest>();
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
                if (rowCount > 2) rowCount = rowCount - 2; //最后一行不读取  //即备注不处理
                //遍历每一行  即每一条记录
                for (int r = 1; r < rowCount; r++) {
                    Row row = sheet.getRow(r);
                    int cellCount = row.getPhysicalNumberOfCells(); //获取总列数

                    CameraRequest camera = new CameraRequest();
                    //遍历每一列
                    for (int c = 0; c < cellCount; c++) {
                        Cell cell = row.getCell(c);
                        int cellType = Cell.CELL_TYPE_STRING;
                        String cellValue = null;
                        if (null != cell && cellType != Cell.CELL_TYPE_BLANK && cellType != Cell.CELL_TYPE_ERROR) {
                            cellType = cell.getCellType();
                            switch (cellType) {
                                case Cell.CELL_TYPE_STRING: //文本
                                    cellValue = cell.getStringCellValue();
                                    break;
                                case Cell.CELL_TYPE_NUMERIC: //数字、日期
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        cellValue = fmt.format(cell.getDateCellValue()); //日期型
                                    } else {
                                        double doubleVal = cell.getNumericCellValue();
                                        long longVal = Math.round(cell.getNumericCellValue());
                                        if (Double.parseDouble(longVal + ".0") == doubleVal) {
                                            cellValue = String.valueOf(longVal);
                                        } else {
                                            cellValue = String.valueOf(doubleVal);
                                        }
                                    }
                                    break;
                                case Cell.CELL_TYPE_BOOLEAN: //布尔型
                                    cellValue = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case Cell.CELL_TYPE_BLANK: //空白
                                    cellValue = cell.getStringCellValue();
                                    break;
                                case Cell.CELL_TYPE_ERROR: //错误
                                    cellValue = "";
                                    break;
                                case Cell.CELL_TYPE_FORMULA: //公式
                                    cellValue = "";
                                    break;
                                default:
                                    cellValue = "";
                            }
                        }

                        if (StringUtils.isNotEmptyString(cellValue)) {
                            switch (c) {
                                // 监控点名称
                                case 0:
                                    camera.setName(cellValue);
                                    break;
                                // 区域ID
                                case 1:
                                    camera.setRegion(cellValue);
                                    break;
                                // 经度
                                case 2:
                                    camera.setLongitude(cellValue);
                                    break;
                                //纬度
                                case 3:
                                    camera.setLatitude(cellValue);
                                    break;
                                //激活状态
                                case 4:
                                    camera.setStatus(Long.parseLong(cellValue));
                                    break;
                                //IP
                                case 5:
                                    camera.setIp(cellValue);
                                    break;
                                //厂商
                                case 6:
                                    camera.setBrandid(Long.valueOf(cellValue));
                                    if (2 == camera.getBrandid()) {
                                        camera.setUrl("rtsp://username:password@" + camera.getIp() + ":554/cam/realmonitor?channel=1&subtype=0");
                                    } else {
                                        camera.setUrl("rtsp://username:password@" + camera.getIp() + ":554/h264/ch1/main/av_stream");
                                    }
                                    break;
                                //账号
                                case 7:
                                    camera.setAccount(cellValue);
                                    String url = camera.getUrl();
                                    if (StringUtils.isNotEmptyString(url)) {
                                        url = url.replace("username", camera.getAccount());
                                        camera.setUrl(url);
                                    }
                                    break;
                                //密码
                                case 8:
                                    camera.setPassword(cellValue);
                                    String url1 = camera.getUrl();
                                    if (StringUtils.isNotEmptyString(url1)) {
                                        url1 = url1.replace("password", camera.getPassword());
                                        camera.setUrl(url1);
                                    }
                                    break;
                                //设备id
                                case 9:
                                    camera.setExtcameraid(cellValue);
                                    break;
                                //端口号
                                case 10:
                                    camera.setPort1(Long.valueOf(cellValue));
                                    break;
                            }
                        }

                    }
                    if (StringUtils.isNotEmptyString(camera.getName())) {
                        cameraList.add(camera);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cameraList;
    }

    /**
     * @param workbook
     * @param sheetNum   (sheet的位置，0表示第一个表格中的第一个sheet)
     * @param sheetTitle （sheet的名称）
     * @param headers    （表格的标题）
     * @param out        （输出流）
     * @throws Exception
     * @Title: exportExcel
     * @Description: 导出Excel的方法
     * @author: evan @ 2014-01-09
     */
    @SuppressWarnings("deprecation")
    public static void exportPersonExcel(HSSFWorkbook workbook, int sheetNum, String sheetTitle, String[] headers, List<ResultQueryVo> resultList, OutputStream out) throws Exception {
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
            for (ResultQueryVo resultBo : resultList) {
                row = sheet.createRow(index);

                // 类型
                HSSFCell cell_00 = row.createCell(0);
                cell_00.setCellStyle(style2);
                HSSFRichTextString richString = new HSSFRichTextString("人");
                cell_00.setCellValue(richString);

                // 监控点
                HSSFCell cell_01 = row.createCell(1);
                cell_01.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCameraName());
                cell_01.setCellValue(richString);


                // 经过时间
                HSSFCell cell_02 = row.createCell(2);
                cell_02.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCreatetimeStr());
                cell_02.setCellValue(richString);

                // 上衣颜色
                HSSFCell cell_3 = row.createCell(3);
                cell_3.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getUpcolorStr());
                cell_3.setCellValue(richString);

                // 下衣颜色
                HSSFCell cell_4 = row.createCell(4);
                cell_4.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getLowcolorStr());
                cell_4.setCellValue(richString);

                // 性别
                HSSFCell cell_5 = row.createCell(5);
                cell_5.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getSex() == 1 ? "男" : (resultBo.getSex() == 2 ? "女" : "未知"));
                cell_5.setCellValue(richString);

                // 年龄
                String age = "";
                Integer gage = resultBo.getAge();
                if (gage != null) {
                    if (gage == 4) {
                        age = "小孩";
                    } else if (gage == 8) {
                        age = "青年";
                    } else if (gage == 16) {
                        age = "中年";
                    } else if (gage == 32) {
                        age = "老年";
                    }
                } else {
                    age = "未知";
                }
                HSSFCell cell_6 = row.createCell(6);
                cell_6.setCellStyle(style2);
                richString = new HSSFRichTextString(age);
                cell_6.setCellValue(richString);

                // 是否背包
                HSSFCell cell_7 = row.createCell(7);
                cell_7.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getBag() == 1 ? "是" : "否");
                cell_7.setCellValue(richString);


                // 姿态
                String angle = "未知";
                if (resultBo.getAngle() == 128) {
                    angle = "正面";
                } else if (resultBo.getAngle() == 256) {
                    angle = "侧面";
                } else if (resultBo.getAngle() == 512) {
                    angle = "背面";
                }
                HSSFCell cell_8 = row.createCell(8);
                cell_8.setCellStyle(style2);
                richString = new HSSFRichTextString(angle);
                cell_8.setCellValue(richString);

                //体态
                HSSFCell cell_9 = row.createCell(9);
                cell_9.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getBodyCharacter());
                cell_9.setCellValue(richString);

                //手持刀棍
                String hasKnife = "未知";
                if (resultBo.getHasKnife() != null){
                    if (resultBo.getHasKnife() == 1) {
                        hasKnife = "是";
                    } else if (resultBo.getHasKnife() == 0) {
                        hasKnife = "否";
                    }
                }
                HSSFCell cell_10 = row.createCell(10);
                cell_10.setCellStyle(style2);
                richString = new HSSFRichTextString(hasKnife);
                cell_10.setCellValue(richString);

                // 目标图片
                HSSFCellStyle linkStyle = workbook.createCellStyle();
                HSSFFont cellFont = workbook.createFont();
                cellFont.setUnderline((byte) 1);
                cellFont.setColor(HSSFColor.BLUE.index);
                linkStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                linkStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                linkStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                linkStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                linkStyle.setFont(cellFont);

                // 眼镜
                HSSFCell cell_11 = row.createCell(11);
                cell_11.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getGlasses() == 0 ? "否" : (resultBo.getGlasses() == 1 ? "是" : "未知"));
                cell_11.setCellValue(richString);

                // 打伞
                HSSFCell cell_12 = row.createCell(12);
                cell_12.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getUmbrella() == 0 ? "否" : (resultBo.getUmbrella() == 1 ? "是" : "未知"));
                cell_12.setCellValue(richString);

                // 手提包
                HSSFCell cell_13 = row.createCell(13);
                cell_13.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getHandbag() == 0 ? "否" : (resultBo.getHandbag() == 1 ? "是" : "未知"));
                cell_13.setCellValue(richString);

                // 拉杆箱
                HSSFCell cell_14 = row.createCell(14);
                cell_14.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getLuggage() == 0 ? "否" : (resultBo.getLuggage() == 1 ?
                        "是" : "未知"));
                cell_14.setCellValue(richString);

                // 手推车
                HSSFCell cell_15 = row.createCell(15);
                cell_15.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getTrolley() == 0 ? "否" : (resultBo.getTrolley() == 1 ?
                        "是" : "未知"));
                cell_15.setCellValue(richString);

                //上衣款式
                HSSFCell cell_16 = row.createCell(16);
                cell_16.setCellStyle(style2);
                String coatStyle = "未知";
                if (resultBo.getCoatStyle() != null) {
                    if (resultBo.getCoatStyle().equals("1")) {
                        coatStyle = "长袖";
                    } else if (resultBo.getCoatStyle().equals("2")) {
                        coatStyle = "短袖";
                    }
                }
                richString = new HSSFRichTextString(coatStyle);
                cell_16.setCellValue(richString);
                //下衣款式
                HSSFCell cell_17 = row.createCell(17);
                cell_17.setCellStyle(style2);
                String trousersStyle = "未知";
                if (resultBo.getTrousersStyle() != null) {
                    if (resultBo.getTrousersStyle().equals("1")) {
                        trousersStyle = "长裤";
                    } else if (resultBo.getTrousersStyle().equals("2")) {
                        trousersStyle = "短裤";
                    } else if (resultBo.getTrousersStyle().equals("3")) {
                        trousersStyle = "裙子";
                    }
                }
                richString = new HSSFRichTextString(trousersStyle);
                cell_17.setCellValue(richString);
                // "是否戴口罩","是否戴帽子","发型","上衣纹理","下衣纹理"
                //是否戴口罩
                HSSFCell cell_18 = row.createCell(18);
                cell_18.setCellStyle(style2);
                String respirator = resultBo.getRespirator();
                richString = new HSSFRichTextString(respirator);
                cell_18.setCellValue(richString);
                //是否戴帽子
                HSSFCell cell_19 = row.createCell(19);
                cell_19.setCellStyle(style2);
                String capString = resultBo.getCap();
                richString = new HSSFRichTextString(capString);
                cell_19.setCellValue(richString);
                //发型
                HSSFCell cell_20 = row.createCell(20);
                cell_20.setCellStyle(style2);
                String hairStyle = resultBo.getHairStyle();
                richString = new HSSFRichTextString(StringUtils.isNotEmptyString(hairStyle) ? hairStyle : "未知");
                cell_20.setCellValue(richString);
                //上衣纹理
                HSSFCell cell_21 = row.createCell(21);
                cell_21.setCellStyle(style2);
                String coatTexture = resultBo.getCoatTexture();
                richString = new HSSFRichTextString(coatTexture);
                cell_21.setCellValue(richString);
                //下衣纹理
                HSSFCell cell_22 = row.createCell(22);
                cell_22.setCellStyle(style2);
                String trousersTexture = resultBo.getTrousersTexture();
                richString = new HSSFRichTextString(trousersTexture);
                cell_22.setCellValue(richString);

                //抱小孩
                HSSFCell cell_23 = row.createCell(23);
                cell_23.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getChestHold());
                cell_23.setCellValue(richString);

                //民族
                HSSFCell cell_24 = row.createCell(24);
                cell_24.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getMinority());
                cell_24.setCellValue(richString);

                //目标图
                HSSFCell cell_25 = row.createCell(25);
                cell_25.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell_25.setCellStyle(linkStyle);
                String imageUrl = resultBo.getPictureLocalPath();
                cell_25.setCellFormula("HYPERLINK(REPLACE(SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\"),FIND(\"ExportResult\",SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\")),50,\"" + imageUrl.replace("\\", "/") + "\"),\"" + "图片超链接" + "\")\n");


                //人脸图
                /*HSSFCell cell_25 = row.createCell(25);
                cell_25.setCellStyle(linkStyle);
                cell_25.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                String faceImgUrl = resultBo.getPictureLocalPath1();
                if (StringUtils.isNotEmptyString(faceImgUrl)) {
                    cell_25.setCellFormula("HYPERLINK(REPLACE(SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\"),FIND(\"ExportResult\",SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\")),50,\"" + faceImgUrl.replace("\\", "/") + "\"),\"" + "图片超链接" + "\")\n");
                } else {
                    cell_25.setCellFormula(null);
                }*/
                index++;
            }
        }
    }

    /**
     * @param workbook
     * @param sheetNum   (sheet的位置，0表示第一个表格中的第一个sheet)
     * @param sheetTitle （sheet的名称）
     * @param headers    （表格的标题）
     * @param out        （输出流）
     * @throws Exception
     * @Title: exportExcel
     * @Description: 导出Excel的方法
     * @author: evan @ 2014-01-09
     */
    @SuppressWarnings("deprecation")
    public static void exportBikeExcel(HSSFWorkbook workbook, int sheetNum, String sheetTitle, String[] headers, List<ResultQueryVo> resultList, OutputStream out) throws Exception {
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
            for (ResultQueryVo resultBo : resultList) {
                row = sheet.createRow(index);

                // 类型
                HSSFCell cell_00 = row.createCell(0);
                cell_00.setCellStyle(style2);
                HSSFRichTextString richString = new HSSFRichTextString("人骑车");
                cell_00.setCellValue(richString);

                // 监控点
                HSSFCell cell_01 = row.createCell(1);
                cell_01.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCameraName());
                cell_01.setCellValue(richString);


                // 经过时间
                HSSFCell cell_02 = row.createCell(2);
                cell_02.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCreatetimeStr());
                cell_02.setCellValue(richString);

                // 车款
                String wheels = "未知";
                Byte wels = resultBo.getWheels();
                if (wels != null) {
                    if (wels == 2) {
                        wheels = "二轮车";
                    } else if (wels == 3) {
                        wheels = "三轮车";
                    }
                }
                HSSFCell cell_3 = row.createCell(3);
                cell_3.setCellStyle(style2);
                richString = new HSSFRichTextString(wheels);
                cell_3.setCellValue(richString);

                //上衣颜色
                HSSFCell cell_4 = row.createCell(4);
                cell_4.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isNotEmptyString(resultBo.getPassengersUpColorStr())
                        ? resultBo.getPassengersUpColorStr() : "未知");
                cell_4.setCellValue(richString);

                //车辆类型
                String bikeGenre = "未知";
                if (resultBo.getBikeGenre() == 1) {
                    bikeGenre = "摩托车";
                } else if (resultBo.getBikeGenre() == 2) {
                    bikeGenre = "摩托车";
                } else if (resultBo.getBikeGenre() == 3) {
                    bikeGenre = "自行车";
                } else if (resultBo.getBikeGenre() == 4) {
                    bikeGenre = "电动车";
                } else if (resultBo.getBikeGenre() == 5) {
                    bikeGenre = "三轮车";
                }
                HSSFCell cell_5 = row.createCell(5);
                cell_5.setCellStyle(style2);
                richString = new HSSFRichTextString(bikeGenre);
                cell_5.setCellValue(richString);

                //是否戴头盔
                String helmet = resultBo.getHelmetStr();
                HSSFCell cell_6 = row.createCell(6);
                cell_6.setCellStyle(style2);
                richString = new HSSFRichTextString(helmet);
                cell_6.setCellValue(richString);

                //头盔颜色
                HSSFCell cell_7 = row.createCell(7);
                cell_7.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isNotEmptyString(resultBo.getHelmetcolorStr()) ?
                        resultBo.getHelmetcolorStr() : "未知");
                cell_7.setCellValue(richString);
                // 性别
                HSSFCell cell_8 = row.createCell(8);
                cell_8.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getBikeSex() == 1 ? "男" : (resultBo.getBikeSex() == 2 ? "女" : "未知"));
                cell_8.setCellValue(richString);

                // 年龄
                String age = "未知";
                if (resultBo.getBikeAge() == 4) {
                    age = "小孩";
                } else if (resultBo.getBikeAge() == 8) {
                    age = "青年";
                } else if (resultBo.getBikeAge() == 16) {
                    age = "中年";
                } else if (resultBo.getBikeAge() == 32) {
                    age = "老年";
                }

                HSSFCell cell_9 = row.createCell(9);
                cell_9.setCellStyle(style2);
                richString = new HSSFRichTextString(age);
                cell_9.setCellValue(richString);

                // 挂牌
                String bikeHasPlate = "未知";
                Short bhpe = resultBo.getBikeHasPlate();
                if (bhpe != null) {
                    if (bhpe == 1) {
                        bikeHasPlate = "是";
                    } else if (bhpe == 0) {
                        bikeHasPlate = "否";
                    }
                }
                HSSFCell cell_10 = row.createCell(10);
                cell_10.setCellStyle(style2);
                richString = new HSSFRichTextString(bikeHasPlate);
                cell_10.setCellValue(richString);

                // 车篷
                String umbrella = "未知";
                if (resultBo.getBikeAngle() == 1) {
                    umbrella = "是";
                } else if (resultBo.getBikeAngle() == 0) {
                    umbrella = "否";
                }
                HSSFCell cell_11 = row.createCell(11);
                cell_11.setCellStyle(style2);
                richString = new HSSFRichTextString(umbrella);
                cell_11.setCellValue(richString);

                // 姿态
                String angle = "未知";
                if (resultBo.getBikeAngle() == 128) {
                    angle = "正面";
                } else if (resultBo.getBikeAngle() == 256) {
                    angle = "侧面";
                } else if (resultBo.getBikeAngle() == 512) {
                    angle = "背面";
                }

                HSSFCell cell_12 = row.createCell(12);
                cell_12.setCellStyle(style2);
                richString = new HSSFRichTextString(angle);
                cell_12.setCellValue(richString);

                //眼镜
                String glasses = "未知";
                if (null != resultBo && null != resultBo.getBikeGlasses()) {
                    glasses = resultBo.getBikeGlasses().equals("0")  ? "否" : (resultBo.getBikeGlasses().equals("1") ? "是" : "未知");
                }
                HSSFCell cell_13 = row.createCell(13);
                cell_13.setCellStyle(style2);
                richString = new HSSFRichTextString(glasses);
                cell_13.setCellValue(richString);

                //上衣款式
                String coatStyle = "未知";
                HSSFCell cell_14 = row.createCell(14);
                cell_14.setCellStyle(style2);
                if (resultBo.getBikeCoatStyle() != null) {
                    if (resultBo.getBikeCoatStyle().equals("1")) {
                        coatStyle = "长袖";
                    } else if (resultBo.getBikeCoatStyle().equals("2")) {
                        coatStyle = "短袖";
                    }
                }
                richString = new HSSFRichTextString(coatStyle);
                cell_14.setCellValue(richString);

                //是否戴口罩
                HSSFCell cell_15 = row.createCell(15);
                cell_15.setCellStyle(style2);
                String respirator = resultBo.getBikeRespirator();
                richString = new HSSFRichTextString(respirator);
                cell_15.setCellValue(richString);
                //是否背包
                HSSFCell cell_16 = row.createCell(16);
                cell_16.setCellStyle(style2);
                Integer bag = Integer.valueOf(resultBo.getBikeBag());
                String bagStr = "";
                if (bag == 0) {
                    bagStr = "否";
                } else if (bag == 1) {
                    bagStr = "是";
                } else if (bag == 2) {
                    bagStr = "拎东西";
                } else {
                    bagStr = "未知";
                }
                richString = new HSSFRichTextString(bagStr);
                cell_16.setCellValue(richString);

                //上衣纹理
                HSSFCell cell_17 = row.createCell(17);
                cell_17.setCellStyle(style2);
                String bikeCoatTexture = resultBo.getBikeCoatTexture();
                richString = new HSSFRichTextString(bikeCoatTexture);
                cell_17.setCellValue(richString);

                //载客
                HSSFCell cell_18 = row.createCell(18);
                cell_18.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCarryPassenger());
                cell_18.setCellValue(richString);

                //车灯形状
                HSSFCell cell_19 = row.createCell(19);
                cell_19.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getLampShape());
                cell_19.setCellValue(richString);

                // 目标图片
                HSSFCellStyle linkStyle = workbook.createCellStyle();
                HSSFFont cellFont = workbook.createFont();
                cellFont.setUnderline((byte) 1);
                cellFont.setColor(HSSFColor.BLUE.index);
                linkStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                linkStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                linkStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                linkStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                linkStyle.setFont(cellFont);

                HSSFCell cell_20 = row.createCell(20);
                cell_20.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell_20.setCellStyle(linkStyle);
                String imageUrl = resultBo.getPictureLocalPath();
                cell_20.setCellFormula("HYPERLINK(REPLACE(SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\"),FIND(\"ExportResult\",SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\")),50,\"" + imageUrl.replace("\\", "/") + "\"),\"" + "图片超链接" + "\")\n");

                //人脸图
                /*HSSFCell cell_18 = row.createCell(18);
                cell_18.setCellStyle(linkStyle);
                cell_18.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                String faceImgUrl = resultBo.getPictureLocalPath1();
                if (StringUtils.isNotEmptyString(faceImgUrl)) {
                    cell_18.setCellFormula("HYPERLINK(REPLACE(SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\"),FIND(\"ExportResult\",SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\")),50,\"" + faceImgUrl.replace("\\", "/") + "\"),\"" + "图片超链接" + "\")\n");
                } else {
                    cell_18.setCellFormula(null);
                }*/

                index++;
            }
        }
    }


    /**
     * @param workbook
     * @param sheetNum   (sheet的位置，0表示第一个表格中的第一个sheet)
     * @param sheetTitle （sheet的名称）
     * @param headers    （表格的标题）
     * @param out        （输出流）
     * @throws Exception
     * @Title: exportExcel
     * @Description: 导出Excel的方法
     * @author: evan @ 2014-01-09
     */
    @SuppressWarnings("deprecation")
    public static void exportVlprExcel(HSSFWorkbook workbook, int sheetNum, String sheetTitle, String[] headers, List<ResultQueryVo> resultList, OutputStream out) throws Exception {
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
            for (ResultQueryVo resultBo : resultList) {
                row = sheet.createRow(index);

                // 类型
                HSSFCell cell_00 = row.createCell(0);
                cell_00.setCellStyle(style2);
                HSSFRichTextString richString = new HSSFRichTextString("车");
                cell_00.setCellValue(richString);

                // 监控点
                HSSFCell cell_01 = row.createCell(1);
                cell_01.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCameraName());
                cell_01.setCellValue(richString);


                // 经过时间
                HSSFCell cell_02 = row.createCell(2);
                cell_02.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCreatetimeStr());
                cell_02.setCellValue(richString);

                // 车牌
                HSSFCell cell_3 = row.createCell(3);
                cell_3.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getLicense()) ? "未知" : resultBo.getLicense());
                cell_3.setCellValue(richString);

                //车牌颜色
                HSSFCell cell_4 = row.createCell(4);
                cell_4.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getPlateColorCode()) ? "未知" :
                        resultBo.getPlateColorCode());
                cell_4.setCellValue(richString);

                //车牌类型
                HSSFCell cell_5 = row.createCell(5);
                cell_5.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getPlateType()) ? "未知" :
                        resultBo.getPlateType());
                cell_5.setCellValue(richString);

                // 品牌
                HSSFCell cell_6 = row.createCell(6);
                cell_6.setCellStyle(style2);
                String carlogo = (StringUtils.isEmptyString(resultBo.getCarlogo()) ? "未知" : resultBo.getCarlogo());
                richString = new HSSFRichTextString(carlogo);
                cell_6.setCellValue(richString);

                // 车系
                HSSFCell cell_7 = row.createCell(7);
                cell_7.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getVehicleseries()) ? "未知" :
                        resultBo.getVehicleseries());
                cell_7.setCellValue(richString);

                // 车身颜色
                HSSFCell cell_8 = row.createCell(8);
                cell_8.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCarcolor());
                cell_8.setCellValue(richString);

                // 车辆类型
                HSSFCell cell_9 = row.createCell(9);
                cell_9.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getVehiclekind()) ? "未知" :
                        resultBo.getVehiclekind());
                cell_9.setCellValue(richString);

                // 年检标志
                HSSFCell cell_10 = row.createCell(10);
                cell_10.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getTagNum() == null ? "未知" :
                        resultBo.getTagNum() + "个");
                cell_10.setCellValue(richString);


                // 纸巾盒
                String paper = "未知";
                if (null != resultBo.getPaper()) {
                    if (0 == resultBo.getPaper()) {
                        paper = "否";
                    } else {
                        paper = (resultBo.getPaper() == 1 ? "是" : "未知");
                    }

                } else {
                    paper = "未知";
                }

                HSSFCell cell_11 = row.createCell(11);
                cell_11.setCellStyle(style2);
                richString = new HSSFRichTextString(paper);
                cell_11.setCellValue(richString);

                // 遮阳板
                HSSFCell cell_12 = row.createCell(12);
                cell_12.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getSun()) ? "未知" :
                        resultBo.getSun());
                cell_12.setCellValue(richString);

                // 挂饰
                String drop = "未知";
                if (null != resultBo.getDrop()) {
                    if (0 == resultBo.getDrop()) {
                        drop = "否";
                    } else {
                        drop = (resultBo.getDrop() == 1 ? "是" : "未知");
                    }
                } else {
                    drop = "未知";
                }

                HSSFCell cell_13 = row.createCell(13);
                cell_13.setCellStyle(style2);
                richString = new HSSFRichTextString(drop);
                cell_13.setCellValue(richString);

                //天线
                HSSFCell cell_14 = row.createCell(14);
                cell_14.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getAerial()) ? "未知" :
                        resultBo.getAerial());
                cell_14.setCellValue(richString);

                // 危险车辆
                String danger = "未知";
                if (null != resultBo.getDanger()) {
                    if (0 == resultBo.getDanger()) {
                        danger = "否";
                    } else {
                        danger = (resultBo.getDanger() == 1 ? "是" : "未知");
                    }
                }
                HSSFCell cell_15 = row.createCell(15);
                cell_15.setCellStyle(style2);
                richString = new HSSFRichTextString(danger);
                cell_15.setCellValue(richString);

                //主驾驶安全带
                HSSFCell cell_16 = row.createCell(16);
                cell_16.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getMainDriver()) ? "未知" :
                        resultBo.getMainDriver());
                cell_16.setCellValue(richString);

                //副驾驶安全带
                HSSFCell cell_17 = row.createCell(17);
                cell_17.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getCoDriver()) ? "未知" :
                        resultBo.getCoDriver());
                cell_17.setCellValue(richString);


                /*// 速度
                HSSFCell cell_18 = row.createCell(18);
                cell_18.setCellStyle(style2);
                String carSpeed = "未知";
                if (null != resultBo && null != resultBo.getCarSpeed() && resultBo.getCarSpeed() > 0) {
                    carSpeed = resultBo.getCarSpeed() + "km/h";
                }
                richString = new HSSFRichTextString(carSpeed);
                cell_18.setCellValue(richString);*/

                //挂牌
                HSSFCell cell_18 = row.createCell(18);
                cell_18.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getVehicleHasPlate()) ? "未知" :
                        resultBo.getVehicleHasPlate());
                cell_18.setCellValue(richString);

                //打电话
                HSSFCell cell_19 = row.createCell(19);
                cell_19.setCellStyle(style2);
                String call = "未知";
                if (null != resultBo && null != resultBo.getHasCall()) {
                    call = resultBo.getHasCall().byteValue() == 1 ? "是" : "否";
                }
                richString = new HSSFRichTextString(call);
                cell_19.setCellValue(richString);

                // 姿态
                HSSFCell cell_20 = row.createCell(20);
                cell_20.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getDirection()) ? "未知" :
                        resultBo.getDirection());
                cell_20.setCellValue(richString);

                //天窗
                HSSFCell cell_21 = row.createCell(21);
                cell_21.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getSunRoof()) ? "未知" :
                        resultBo.getSunRoof());
                cell_21.setCellValue(richString);

                //行李架
                HSSFCell cell_22 = row.createCell(22);
                cell_22.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getRack()) ? "未知" :
                        resultBo.getRack());
                cell_22.setCellValue(richString);

                //摆件
                HSSFCell cell_23 = row.createCell(23);
                cell_23.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getDecoration()) ? "未知" :
                        resultBo.getDecoration());
                cell_23.setCellValue(richString);

                //年款
                HSSFCell cell_24 = row.createCell(24);
                cell_24.setCellStyle(style2);
                richString = new HSSFRichTextString(StringUtils.isEmptyString(resultBo.getVehicleStyle()) ? "未知" :
                        resultBo.getVehicleStyle());
                cell_24.setCellValue(richString);

                // 撞损车辆
                String crash = "未知";
                if (null != resultBo.getCrash()) {
                    if (0 == resultBo.getCrash()) {
                        crash = "否";
                    } else {
                        crash = (resultBo.getCrash() == 1 ? "是" : "未知");
                    }
                }
                HSSFCell cell_25 = row.createCell(25);
                cell_25.setCellStyle(style2);
                richString = new HSSFRichTextString(crash);
                cell_25.setCellValue(richString);

                // 目标图片
                HSSFCellStyle linkStyle = workbook.createCellStyle();
                HSSFFont cellFont = workbook.createFont();
                cellFont.setUnderline((byte) 1);
                cellFont.setColor(HSSFColor.BLUE.index);
                linkStyle.setFont(cellFont);

                HSSFCell cell_26 = row.createCell(26);
                cell_26.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell_26.setCellStyle(linkStyle);

                String imageUrl = resultBo.getPictureLocalPath();
                cell_26.setCellFormula("HYPERLINK(REPLACE(SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\"," +
                        "\"\"),FIND(\"ExportResult\",SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\")),50,\"" + imageUrl.replace("\\", "/") + "\"),\"" + "图片超链接" + "\")\n");

                //人脸图
                /*HSSFCell cell_24 = row.createCell(24);
                cell_24.setCellStyle(linkStyle);
                cell_24.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                String faceImgUrl = resultBo.getPictureLocalPath1();
                if (StringUtils.isNotEmptyString(faceImgUrl)) {
                    cell_24.setCellFormula("HYPERLINK(REPLACE(SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\")," +
                            "\"[\",\"\"),FIND(\"ExportResult\",SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\")),50,\"" + faceImgUrl.replace("\\", "/") + "\"),\"" + "图片超链接" + "\")\n");
                } else {
                    cell_24.setCellFormula(null);
                }*/

                index++;
            }
        }
    }

    /**
     * @param workbook
     * @param sheetNum   (sheet的位置, 0表示第一个表格中的sheet)
     * @param sheetTitle (sheet的名称)
     * @param headers    (表格的标题)
     * @param resultList (输出的数据)
     * @param out        (输出流)
     * @throws Exception
     * @Title: exportExcel
     * @Description: 导出Excel的方法
     * @author shitao @ 2019-04-03
     */
    @SuppressWarnings("deprecation")
    public static void exportFaceExcel(HSSFWorkbook workbook, int sheetNum, String sheetTitle, String[] headers, List<ResultQueryVo> resultList, OutputStream out) throws Exception {
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

        // 生成一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成一个字体
        HSSFFont font2 = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
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

        //遍历集合,产生数据行
        if (ValidateHelper.isNotEmptyList(resultList)) {
            int index = 1;
            for (ResultQueryVo resultBo : resultList) {
                row = sheet.createRow(index);

                // 类型
                HSSFCell cell_00 = row.createCell(0);
                cell_00.setCellStyle(style2);
                HSSFRichTextString richString = new HSSFRichTextString("人脸");
                cell_00.setCellValue(richString);

                // 监控点
                HSSFCell cell_01 = row.createCell(1);
                cell_01.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCameraName());
                cell_01.setCellValue(richString);


                // 经过时间
                HSSFCell cell_02 = row.createCell(2);
                cell_02.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCreatetimeStr());
                cell_02.setCellValue(richString);

                // 目标图片
                HSSFCellStyle linkStyle = workbook.createCellStyle();
                HSSFFont cellFont = workbook.createFont();
                cellFont.setUnderline((byte) 1);
                cellFont.setColor(HSSFColor.BLUE.index);
                linkStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                linkStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                linkStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                linkStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                linkStyle.setFont(cellFont);

                //人脸图   因前面注掉了属性所以设置row.createCell(3) 删除注释时修正列数
                HSSFCell cell_10 = row.createCell(3);
                cell_10.setCellStyle(linkStyle);
                cell_10.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                String ImageUrl = resultBo.getPictureLocalPath1();
                cell_10.setCellFormula("HYPERLINK(REPLACE(SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\"),FIND(\"ExportResult\",SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\")),50,\"" + ImageUrl.replace("\\", "/") + "\"),\"" + "图片超链接" + "\")\n");
                index++;
            }
        }

    }

    @SuppressWarnings("deprecation")
    public static void exportOthersExcel(HSSFWorkbook workbook, int sheetNum, String sheetTitle, String[] headers, List<ResultQueryVo> resultList, OutputStream out) {
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
            for (ResultQueryVo resultBo : resultList) {
                row = sheet.createRow(index);

                // 类型
                HSSFCell cell_00 = row.createCell(0);
                cell_00.setCellStyle(style2);
                HSSFRichTextString richString = new HSSFRichTextString("其他");
                cell_00.setCellValue(richString);

                // 监控点
                HSSFCell cell_01 = row.createCell(1);
                cell_01.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCameraName());
                cell_01.setCellValue(richString);


                // 经过时间
                HSSFCell cell_02 = row.createCell(2);
                cell_02.setCellStyle(style2);
                richString = new HSSFRichTextString(resultBo.getCreatetimeStr());
                cell_02.setCellValue(richString);

                // 目标图片
                HSSFCellStyle linkStyle = workbook.createCellStyle();
                HSSFFont cellFont = workbook.createFont();
                cellFont.setUnderline((byte) 1);
                cellFont.setColor(HSSFColor.BLUE.index);
                linkStyle.setFont(cellFont);

                HSSFCell cell_3 = row.createCell(3);
                cell_3.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell_3.setCellStyle(linkStyle);


                String imageUrl = resultBo.getPictureLocalPath();
                cell_3.setCellFormula("HYPERLINK(REPLACE(SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\"),FIND(\"ExportResult\",SUBSTITUTE(SUBSTITUTE(CELL(\"filename\"),\"'\",\"\"),\"[\",\"\")),50,\"" + imageUrl.replace("\\", "/") + "\"),\"" + "图片超链接" + "\")\n");

                index++;
            }
        }
    }

    public static void exportLogExcel(HttpServletResponse response, String[] headers, List<SysLog> resultList) throws Exception {
        HSSFWorkbook workbook  = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();

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
        if (ValidateHelper.isNotEmptyList(resultList))
        {
            int index = 1;
            for (SysLog sysLog : resultList)
            {
                row = sheet.createRow(index);

                // 类型
                HSSFRichTextString richString = new HSSFRichTextString("");


                //创建用户
                HSSFCell cell_4 = row.createCell(0);
                cell_4.setCellStyle(style2);
                richString = new HSSFRichTextString(sysLog.getRealName());
                cell_4.setCellValue(richString);

                //模块名称
                HSSFCell cell_5 = row.createCell(1);
                cell_5.setCellStyle(style2);
                richString = new HSSFRichTextString(sysLog.getModuleName());
                cell_5.setCellValue(richString);

                //操作类型
                Integer actionType = sysLog.getActionType();
                String actionTypeStr="";
                actionTypeStr = actionType==1?"查询":(actionType==2?"新增":(actionType==3?"修改":(actionType==4?"删除":(actionType==5?"登录":(actionType==6?"退出":"其他")))));
                HSSFCell cell_6 = row.createCell(2);
                cell_6.setCellStyle(style2);
                richString = new HSSFRichTextString(actionTypeStr);
                cell_6.setCellValue(richString);

                // 创建时间
                HSSFCell cell_3 = row.createCell(3);
                cell_3.setCellStyle(style2);
                richString = new HSSFRichTextString(DateTimeUtils.formatDate(sysLog.getCreateTime(),null));
                cell_3.setCellValue(richString);
                index++;
            }
        }
        String fileName = "log"+RandomUtils.get8RandomValiteCode(8);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename="
                + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }
}