package ru.yandex.sashanc.services;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.yandex.sashanc.db.ComplaintDao;
import ru.yandex.sashanc.db.IComplaintDao;
import ru.yandex.sashanc.pojo.Notification;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class ComplaintService implements IComplaintService {
    private static final Logger logger = Logger.getLogger(ComplaintService.class);

    /**
     * Метод создаёт новый рекламационный акт из данных сообщения
     */
    public void generateComplaint(Notification not, String descriptionRu, String descriptionEn, LocalDate complaintDate, boolean imageAdd, String complaintType, List<File> imageFileList){
        logger.info("Method generateComplaint(Notification not, String descriptionRu, String descriptionEn, LocalDate complaintDate, boolean imageAdd) is launched...");
        IComplaintDao complaintDao = new ComplaintDao();
//        String complBlank = getClass().getResource("/blanks/blank_complaint.xlsx").getPath();
        InputStream complBlank = getClass().getResourceAsStream("/blanks/blank_complaint.xlsx");
        String newComplaintNumber = complaintDao.getNewComplaintNumber(not.getSupplierNumber(), complaintType);
        String newComplaintPath = complaintDao.getNewComplaintPath(not.getSupplierNumber());
        logger.info("Имя рекламационного акта " + newComplaintPath);
        try (XSSFWorkbook wbComplBlank = new XSSFWorkbook(complBlank)) {
            XSSFSheet sheetA = wbComplBlank.getSheetAt(0);
            sheetA.getRow(22).getCell(8).setCellValue(not.getMaterialDesSap());
            sheetA.getRow(22).getCell(31).setCellValue(not.getMaterialNumber());
            sheetA.getRow(22).getCell(45).setCellValue(not.getComplaintQuantity());
            if(descriptionRu.equals("")){
                sheetA.getRow(24).getCell(8).setCellValue(not.getDefectDescription());
            } else {
                sheetA.getRow(24).getCell(8).setCellValue(descriptionRu);
            }
            if(descriptionEn.equals("")){
                sheetA.getRow(26).getCell(8).setCellValue("n/a");
            } else {
                sheetA.getRow(26).getCell(8).setCellValue(descriptionEn);
            }
            FileOutputStream fileOut = new FileOutputStream(newComplaintPath);
            wbComplBlank.write(fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(imageAdd){
            ImageService imageService = new ImageService();
            imageService.inputImageToSheet(Paths.get(newComplaintPath), imageFileList, not.getNotId());
        }
    }
}