package ru.yandex.sashanc.db;

import org.apache.log4j.Logger;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.yandex.sashanc.Main;
import ru.yandex.sashanc.pojo.Notification;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NotificationDao implements INotificationDao {
    private static final Logger logger = Logger.getLogger(Main.class);

    @Override
    public List<Notification> getListNotifications(String resourceFile) {
        List<Notification> notList = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(resourceFile))) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator rowIter = sheet.rowIterator();
            while (rowIter.hasNext()) {
                XSSFRow row = (XSSFRow) rowIter.next();
                if(row.getRowNum() > 1){
                    Notification notif = new Notification();
                    notif.setNotType(row.getCell(0).getStringCellValue());
                    //TODO Сделать проверку, если число будет сохранено как текст
                    switch (row.getCell(1).getCellType()){
                        case NUMERIC:
                            notif.setNotId((int)row.getCell(1).getNumericCellValue());
                            break;
                        case STRING:
                            notif.setNotId(Integer.parseInt(row.getCell(1).getStringCellValue()));
                            break;

                    }
                    notif.setNotDate(row.getCell(2).getDateCellValue());
                    notif.setMaterialNumber(row.getCell(3).getStringCellValue());
                    notif.setMaterialDesSap(row.getCell(4).getStringCellValue());
//                    notif.setMaterialDesUser(row.getCell(5).getStringCellValue());
                    notif.setComplaintQuantity((int)row.getCell(5).getNumericCellValue());
                    notif.setDefectDescription(row.getCell(6).getStringCellValue());
                    notif.setNotStatus(row.getCell(7).getStringCellValue());
                    //TODO Сделать проверку, на пустые ячейки
                    switch (row.getCell(11).getCellType()){
                        case NUMERIC:
                            notif.setSupplierNumber((int)row.getCell(11).getNumericCellValue());
                            break;
                        case STRING:
                            notif.setSupplierNumber(Integer.parseInt(row.getCell(11).getStringCellValue()));

                            break;

                    }
                    notif.setSupplierName(row.getCell(13).getStringCellValue());
                    logger.info(notif.getNotType() + " " + notif.getNotId() + " " + notif.getNotDate());
                    notList.add(notif);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return notList;
    }
}
