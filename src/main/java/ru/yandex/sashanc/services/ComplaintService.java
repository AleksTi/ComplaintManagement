package ru.yandex.sashanc.services;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.yandex.sashanc.pojo.Notification;

import java.io.*;

public class ComplaintService implements IComplaintService {
    public void generateComplaint(Notification not) {
        try (XSSFWorkbook wbComplBlank = new XSSFWorkbook(new FileInputStream("E:\\work\\blank_complaint.xlsx"))) {
            XSSFSheet sheetA = wbComplBlank.getSheetAt(0);
            System.out.println(sheetA.getSheetName());
            System.out.println(sheetA.getRow(22).getCell(31).getStringCellValue());
            sheetA.getRow(22).getCell(8).setCellValue(not.getMaterialDesSap());
            sheetA.getRow(22).getCell(31).setCellValue(not.getMaterialNumber());
            sheetA.getRow(22).getCell(45).setCellValue(not.getComplaintQuantity());
            sheetA.getRow(24).getCell(8).setCellValue(not.getDefectDescription());
            sheetA.getRow(25).getCell(45).setCellValue(not.getNotId());
            FileOutputStream fileOut = new FileOutputStream("E:\\work\\blank_complaint1.xlsx");
            wbComplBlank.write(fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
