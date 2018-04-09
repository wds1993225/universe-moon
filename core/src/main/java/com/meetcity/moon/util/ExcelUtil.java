package com.meetcity.moon.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Created by wds on 2017/12/8.
 *
 * @author moon
 *         <p>
 *         解析EXCEL的工具类
 *         <p>
 *         此类有两个方法{@linkplain #parseExcel()} 和 {@linkplain #parseExcelWithTitle()}
 *         区别就是 转换成的 数据类型 格式不一样
 *         实现的功能也要选不同的
 *         <p>
 *         {@linkplain #parseExcelWithTitle()} 可以处理带有标题的，返回map可以更好的解析
 */
@Slf4j
public class ExcelUtil {

    private String fileName;        //文件名


    private ExcelFunction excelFunction;

    private ExcelFunctionWithTitle excelFunctionWithTitle;

    public ExcelUtil(String fileName, ExcelFunction excelFunction) {
        this.fileName = fileName;
        this.excelFunction = excelFunction;
    }

    public ExcelUtil(String fileName, ExcelFunctionWithTitle excelFunctionWithTitle) {
        this.fileName = fileName;
        this.excelFunctionWithTitle = excelFunctionWithTitle;
    }

    /**
     * 解析通用的EXCEL
     * 返回一个 {List}
     * <p>
     * 会把每个单元格中的 数据转换成string 放入
     */
    public void parseExcel() {
        try {
            File excelFile = new File(fileName);
            FileInputStream inputStream = new FileInputStream(excelFile);
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);        //获取单个sheet ， 这里默认一个excel中只包含一个sheet
            int rowCount = sheet.getPhysicalNumberOfRows();         //总行数
            for (int r = 0; r < rowCount; r++) {                                    //如果有标题的话， 改成 r=1 , 跳过标题 ， 避免输出行标题
                HSSFRow row = sheet.getRow(r);
                int cellCount = row.getPhysicalNumberOfCells();     //总列数  , 这个方法能跳过空行

                List<String> cellList = new ArrayList<>();
                for (int c = 0; c < cellCount; c++) {
                    HSSFCell cell = row.getCell(c);                 //每个单元格
                    cellList.add(cell.toString());
                }
                if (excelFunction != null) {
                    excelFunction.doFunction(cellList);
                } else {
                    for (String str : cellList) {
                        log.debug("every cell print , cell is : {}", str);
                    }
                }
            }

        } catch (Throwable t) {
            t.printStackTrace();
            log.error("parse excel error , fileName is : {} ,function is : {} ,  error is : ", fileName, excelFunction, t);
        }
    }


    /**
     * 解析
     * 返回一个 {Map}
     * <p>
     * 适用于  带有标题的EXCEL，此方法会把 数据组合成  { 标题 : 内容 } 的一个 {Map}
     */
    public void parseExcelWithTitle() {
        try {
            File excelFile = new File(fileName);
            FileInputStream inputStream = new FileInputStream(excelFile);
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);        //获取单个sheet ， 这里默认一个excel中只包含一个sheet
            int rowCount = sheet.getPhysicalNumberOfRows();         //总行数
            HSSFRow firstRow = sheet.getRow(0);     //行title
            for (int r = 1; r < rowCount; r++) {
                HSSFRow row = sheet.getRow(r);
                int cellCount = row.getPhysicalNumberOfCells();     //总列数  , 这个方法能跳过空行

                //Map<String, String> cellMap = new LinkedHashMap<>();
                Map<String, String> cellMap = new HashMap<>();
                for (int c = 0; c < cellCount; c++) {
                    HSSFCell cell = row.getCell(c);                 //每个单元格
                    HSSFCell titleCell = firstRow.getCell(c);
                    cellMap.put(titleCell.toString(), cell.toString());
                }
                if (excelFunctionWithTitle != null) {
                    excelFunctionWithTitle.doFunction(cellMap);
                } else {
                    for (Map.Entry<String, String> entry : cellMap.entrySet()) {
                        log.debug("every cell print , cell is : {}", entry.getKey() + " : " + entry.getValue());
                    }
                }
            }

        } catch (Throwable t) {
            t.printStackTrace();
            log.error("parse excel error , fileName is : {} ,function is : {} ,  error is : ", fileName, excelFunctionWithTitle, t);
        }
    }

    /**
     * 功能接口
     * 返回 list 类型的数据
     *
     * @see #parseExcel()
     */
    public interface ExcelFunction {

        void doFunction(List<String> cells);

    }


    /**
     * 类型为带有标题的EXCEL
     * <p>
     * 会返回一个Map， K：标题  ，V：内容
     * <p>
     * 这里标题需要在第一行
     * <p>
     *
     * @see #parseExcelWithTitle()
     */
    public interface ExcelFunctionWithTitle {

        void doFunction(Map<String, String> cellMap);
    }

}
