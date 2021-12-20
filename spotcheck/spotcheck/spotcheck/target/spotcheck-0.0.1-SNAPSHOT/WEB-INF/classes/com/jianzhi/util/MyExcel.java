package com.jianzhi.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

public class MyExcel {
    public static void getExcel(HttpServletResponse response,int rowsize, List list,String fileName) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //合格并单元格,使用默认标题
        //一次性写出内容,使用默认样式
        //合并单元格后的标题行，使用默认标题样式
        //writer.merge(rowsize-1, fileName);
        writer.write(list);
        
        writer.setDefaultRowHeight(18);
        
        setSizeColumn(writer.getSheet(),rowsize-1);
        
        response.reset();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;fileName="+ URLEncoder.encode(fileName+".xlsx","UTF-8"));
        writer.flush(response.getOutputStream());
        //关闭writer,释放内存
        writer.close();
        
    }




    
    /**
     * 自适应宽度(中文支持)
     * @param sheet
     * @param size 因为for循环从0开始，size值为 列数-1
     */
    
    public static void setSizeColumn(Sheet sheet, int size) {
    	
        for (int columnNum = 0; columnNum <= size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    Cell currentCell = currentRow.getCell(columnNum);
                    
                    if (currentCell.getCellType() == CellType.STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }
    
    
}
