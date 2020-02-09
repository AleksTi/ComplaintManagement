package ru.yandex.sashanc.db;

import org.apache.log4j.Logger;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.yandex.sashanc.Main;
import ru.yandex.sashanc.pojo.Notification;
import ru.yandex.sashanc.pojo.PartMB51;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.time.ZoneId;
import java.util.*;

public class NotificationDao implements INotificationDao {
    private static final Logger logger = Logger.getLogger(Main.class);

    @Override
    public List<Notification> getListNotifications(Path resourceFile, Path resourceFileMB51) {
        List<Notification> notList = new ArrayList<>();
        List<PartMB51> partMB51List = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(resourceFile.toFile()))) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator rowIter = sheet.rowIterator();
            Map<String, Integer> notFileHeader = new HashMap<>();
            while (rowIter.hasNext()) {
                XSSFRow row = (XSSFRow) rowIter.next();
                if (row.getRowNum() == 0){
                    Iterator cellIter = row.cellIterator();
                    while (cellIter.hasNext()) {
                        XSSFCell cell = (XSSFCell) cellIter.next();
                        if (cell != null) {
                            switch (cell.getStringCellValue()) {
                                case "Вид сообщения":
                                    notFileHeader.put("notType", cell.getColumnIndex());
                                    break;
                                case "Сообщение":
                                    notFileHeader.put("notId", cell.getColumnIndex());
                                    break;
                                case "Дата сообщения":
                                    notFileHeader.put("notDate", cell.getColumnIndex());
                                    break;
                                case "Материал":
                                    notFileHeader.put("materialNumber", cell.getColumnIndex());
                                    break;
                                case "Краткий текст материала":
                                    notFileHeader.put("materialDesSap", cell.getColumnIndex());
                                    break;
                                case "КоличРекламации":
                                    notFileHeader.put("complaintQuantity", cell.getColumnIndex());
                                    break;
                                case "Описание":
                                    notFileHeader.put("defectDescription", cell.getColumnIndex());
                                    break;
                                case "Статус сообщения":
                                    notFileHeader.put("notStatus", cell.getColumnIndex());
                                    break;
                                case "Поставщик":
                                    notFileHeader.put("supplierNumber", cell.getColumnIndex());
                                    break;
                                case "Имя списка":
                                    notFileHeader.put("supplierName", cell.getColumnIndex());
                                    break;
                                case "Документ материала":
                                    notFileHeader.put("purchasingDoc", cell.getColumnIndex());
                                    break;
                            }
                        }
                    }
                }
                if (row.getRowNum() > 1) {
                    Notification notif = new Notification();
                    //TODO Сделать проверку, на пустые ячейки
                    notif.setNotType(row.getCell(notFileHeader.get("notType")).getStringCellValue());
                    notif.setNotId(getIntFromCell(row.getCell(notFileHeader.get("notId"))));
                    notif.setNotDate(row.getCell(notFileHeader.get("notDate")).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    notif.setMaterialNumber(row.getCell(notFileHeader.get("materialNumber")).getStringCellValue());
                    notif.setMaterialDesSap(row.getCell(notFileHeader.get("materialDesSap")).getStringCellValue());
                    notif.setComplaintQuantity((int) row.getCell(notFileHeader.get("complaintQuantity")).getNumericCellValue());
                    notif.setDefectDescription(row.getCell(notFileHeader.get("defectDescription")).getStringCellValue());
                    notif.setNotStatus(row.getCell(notFileHeader.get("notStatus")).getStringCellValue());
                    notif.setSupplierNumber(getIntFromCell(row.getCell(notFileHeader.get("supplierNumber"))));
                    notif.setSupplierName(row.getCell(notFileHeader.get("supplierName")).getStringCellValue());
                    notif.setPurchasingDoc(row.getCell(notFileHeader.get("purchasingDoc")).getStringCellValue());
                    notList.add(notif);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(new File(resourceFileMB51.toString()).exists()) {
            try (XSSFWorkbook workbookMB51 = new XSSFWorkbook(new FileInputStream(resourceFileMB51.toFile()))) {
                XSSFSheet sheetMB51 = workbookMB51.getSheetAt(0);
                Iterator rowIterMB51 = sheetMB51.rowIterator();
                Map<String, Integer> mapFileMb51 = new HashMap<>();
                while (rowIterMB51.hasNext()) {
                    XSSFRow rowMB51 = (XSSFRow) rowIterMB51.next();
                    if (rowMB51.getRowNum() == 0){
                        Iterator cellIter = rowMB51.cellIterator();
                        while (cellIter.hasNext()) {
                            XSSFCell cell = (XSSFCell) cellIter.next();
                            if (cell != null) {
                                switch (cell.getStringCellValue()) {
                                    case "Материал":
                                        mapFileMb51.put("materialNum", cell.getColumnIndex());
                                        break;
                                    case "Документ материала":
                                        mapFileMb51.put("materialDoc", cell.getColumnIndex());
                                        break;
                                    case "Ссылка":
                                        mapFileMb51.put("deliveryNote", cell.getColumnIndex());
                                        break;
                                    case "Дата документа":
                                        mapFileMb51.put("materialDocDate", cell.getColumnIndex());
                                        break;
                                    case "Дата проводки":
                                        mapFileMb51.put("deliveryDate", cell.getColumnIndex());
                                        break;
                                    case "Текст заголовка документа":
                                        mapFileMb51.put("invoice", cell.getColumnIndex());
                                        break;
                                }
                            }
                        }
                    }
                    if (rowMB51.getRowNum() > 1) {
                        PartMB51 partMB51 = new PartMB51();
                        partMB51.setMaterialNum(rowMB51.getCell(mapFileMb51.get("materialNum")).getStringCellValue());
                        partMB51.setMaterialDoc(rowMB51.getCell(mapFileMb51.get("materialDoc")).getStringCellValue());
                        partMB51.setDeliveryNote(rowMB51.getCell(mapFileMb51.get("deliveryNote")).getStringCellValue());
                        partMB51.setMaterialDocDate(rowMB51.getCell(mapFileMb51.get("materialDocDate")).getDateCellValue());
                        partMB51.setDeliveryDate(rowMB51.getCell(mapFileMb51.get("deliveryDate")).getDateCellValue());
                        partMB51.setInvoice(rowMB51.getCell(mapFileMb51.get("invoice")).getStringCellValue());
                        partMB51List.add(partMB51);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Notification not : notList) {
                for (PartMB51 part51 : partMB51List) {
                    if(not.getPurchasingDoc().equals(part51.getMaterialDoc()) && not.getMaterialNumber().equals(part51.getMaterialNum())){
                        not.setDeliveryNote(part51.getDeliveryNote());
                        not.setDeliveryNoteDate(part51.getMaterialDocDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        not.setPostDate(part51.getDeliveryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        not.setInvoice(part51.getInvoice());
                    }
                }
            }
        }
        return notList;
    }

    private int getIntFromCell(XSSFCell cell){
        int i = 0;
        switch (cell.getCellType()){
            case NUMERIC:
                i = (int)cell.getNumericCellValue();
                break;
            case STRING:
                i = Integer.parseInt(cell.getStringCellValue());
                break;
        }
        return i;
    }
}
