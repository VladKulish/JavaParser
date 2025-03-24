package com.project.parsing.excel;

import com.project.parsing.model.Unit;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WriteToExcel {
    public static final String filePath = "src/main/java/com/project/parsing/excel/jobs.xlsx";
    public void toExcel(List<Unit> units){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Result");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Название");
        headerRow.createCell(1).setCellValue("Описание");
        headerRow.createCell(2).setCellValue("Зарплата");
        for(int i = 0; i<units.size();i++){
            Unit job = units.get(i);
            Row row = sheet.createRow(i+1);

            row.createCell(0).setCellValue(job.getJobName());
            row.createCell(1).setCellValue(job.getAboutJob());
            row.createCell(2).setCellValue(job.getJobSalaryRange());
        }
        try(FileOutputStream fileOut = new FileOutputStream(filePath)){
            workbook.write(fileOut);
            System.out.println("Запис завершено.");
            System.out.println("Файл створено за шляхом: " + filePath);

        }catch (IOException e){
            e.printStackTrace();
        }
        try{
            workbook.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
