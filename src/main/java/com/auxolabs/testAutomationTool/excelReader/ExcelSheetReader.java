package com.auxolabs.testAutomationTool.excelReader;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelSheetReader {

    private Workbook getWorkbook(FileInputStream inputStream, String excelFilePath)
            throws IOException {
        Workbook workbook;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();

            case NUMERIC:
                return cell.getNumericCellValue();
        }

        return null;
    }

    public HashMap<String,ArrayList<Object>> readFromExcelFile(String excelFilePath) throws IOException {

        HashMap<String,ArrayList<Object>> documentData = new HashMap<String, ArrayList<Object>>();
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
        Workbook workbook = getWorkbook(inputStream,excelFilePath);
        Sheet excelSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = excelSheet.rowIterator();
        ArrayList<String> columnNames = new ArrayList<String>();

        if(rowIterator.hasNext()){
            Row nextRow = rowIterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            while (cellIterator.hasNext()) {
                Cell nextCell = cellIterator.next();
                columnNames.add(nextCell.getStringCellValue());
                documentData.put(nextCell.getStringCellValue(),null);
            }
        }

        while (rowIterator.hasNext()){
            int i = 0;
            Row nextRow = rowIterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            while (cellIterator.hasNext()){
                Cell nextCell = cellIterator.next();
                ArrayList<Object> arrayList = documentData.get(columnNames.get(i));
                if(arrayList == null) arrayList = new ArrayList<Object>();
                arrayList.add(getCellValue(nextCell));
                documentData.put(columnNames.get(i),arrayList);
                i++;
            }
        }
        return documentData;
    }

}