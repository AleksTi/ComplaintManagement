package ru.yandex.sashanc.services;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.yandex.sashanc.db.*;
import ru.yandex.sashanc.pojo.*;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComplaintService implements IComplaintService {
    private static final Logger logger = Logger.getLogger(ComplaintService.class);

    /**
     * Метод создаёт новый рекламационный акт из данных сообщения
     */
    public void generateComplaint(Notification not, String descriptionRu, String descriptionEn, LocalDate complaintDate, boolean imageAdd, String complaintType, List<File> imageFileList){
        logger.info("Method generateComplaint is launched...");
        IComplaintDao complaintDao = new ComplaintDaoImpl();
        String newComplaintNumber = getNewComplaintNumberDraft(not.getSupplierNumber(), complaintType);

        Complaint complaint = new Complaint();
        complaintDao.getComplaintData(complaint);

        IPartDao partDao = new PartDaoImpl();
        Part part = partDao.getContractPart(not.getSupplierNumber(), not.getMaterialNumber());

        IContractDao contractDao = new ContractDaoImpl();
        Contract contract = contractDao.getContract(not.getSupplierNumber());

        EmployeeDaoImpl employeeDao = new EmployeeDaoImpl();
        Map<String, Employee> employees = employeeDao.getEmployeeList();

        Path newComplaintPath = complaintDao.getNewComplaintPath(not.getSupplierNumber(), newComplaintNumber);
        logger.info("Имя нового рекламационного акта " + newComplaintPath);

        InputStream complBlank = getClass().getResourceAsStream("/blanks/blank_complaint.xlsx");
        try (XSSFWorkbook wbComplBlank = new XSSFWorkbook(complBlank)) {
            XSSFFont fontItalic = wbComplBlank.createFont();
            fontItalic.setItalic(true);
            fontItalic.setFontName("Times New Roman");
            fontItalic.setFontHeightInPoints((short) 14);
            XSSFRichTextString cellValuePartName = new XSSFRichTextString();
            cellValuePartName.append(part.getPartNameRu());
            cellValuePartName.append("\n" + part.getPartNameEn().toUpperCase(), fontItalic);
            XSSFSheet sheetA = wbComplBlank.getSheetAt(0);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            sheetA.getRow(7).getCell(22).setCellValue(newComplaintNumber);
            sheetA.getRow(7).getCell(34).setCellValue(complaintDate.format(dtf));
            sheetA.getRow(16).getCell(5).setCellValue(contract.getContractName());
            sheetA.getRow(17).getCell(5).setCellValue(contract.getContractName());
            sheetA.getRow(16).getCell(43).setCellValue(contract.getContractNumber() + " от/from " + contract.getDate().format(dtf));
            sheetA.getRow(20).getCell(6).setCellValue(not.getInvoice());
            if(not.getDeliveryNoteDate() != null) {
                sheetA.getRow(20).getCell(20).setCellValue(not.getDeliveryNoteDate().format(dtf));
                sheetA.getRow(47).getCell(22).setCellValue(not.getInvoice() + " от/from " + not.getDeliveryNoteDate().format(dtf));
            }
            if(not.getPostDate() != null) {
                sheetA.getRow(20).getCell(32).setCellValue(not.getPostDate().format(dtf));
            }
            sheetA.getRow(20).getCell(44).setCellValue(not.getNotDate().format(dtf));
            sheetA.getRow(22).getCell(8).setCellValue(cellValuePartName);
            sheetA.getRow(47).getCell(8).setCellValue(cellValuePartName);
            sheetA.getRow(22).getCell(31).setCellValue(not.getMaterialNumber());
            sheetA.getRow(22).getCell(45).setCellValue(not.getComplaintQuantity());
            sheetA.getRow(25).getCell(45).setCellValue(not.getNotId());
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
            sheetA.getRow(28).getCell(8).setCellValue(contract.getContractName());
            sheetA.getRow(47).getCell(3).setCellValue(not.getMaterialNumber());
            sheetA.getRow(47).getCell(8).setCellValue(not.getMaterialDesSap());
            sheetA.getRow(47).getCell(27).setCellValue(not.getComplaintQuantity());


            //Блок вставки имён
            XSSFRichTextString cellValueQualityResp = new XSSFRichTextString();
            cellValueQualityResp.append(employees.get(contract.getEmployeeIdQuality()).getSurnameRu() + " " + employees.get(contract.getEmployeeIdQuality()).getNameRu());
            cellValueQualityResp.append("\n" + employees.get(contract.getEmployeeIdQuality()).getSurnameEn() + " " + employees.get(contract.getEmployeeIdQuality()).getNameEn(), fontItalic);
            XSSFRichTextString cellValueExtlogResp = new XSSFRichTextString();
            cellValueExtlogResp.append(employees.get(contract.getEmployeeId()).getSurnameRu() + " " + employees.get(contract.getEmployeeId()).getNameRu());
            cellValueExtlogResp.append("\n" + employees.get(contract.getEmployeeId()).getSurnameEn() + " " + employees.get(contract.getEmployeeId()).getNameEn(), fontItalic);
            XSSFRichTextString cellValueControlResp = new XSSFRichTextString();
            cellValueControlResp.append(employees.get("z298410").getSurnameRu() + " " + employees.get("z298410").getNameRu());
            cellValueControlResp.append("\n" + employees.get("z298410").getSurnameEn() + " " + employees.get("z298410").getNameEn(), fontItalic);
            sheetA.getRow(35).getCell(24).setCellValue(cellValueQualityResp);
            sheetA.getRow(61).getCell(24).setCellValue(cellValueExtlogResp);
            sheetA.getRow(63).getCell(24).setCellValue(cellValueControlResp);
            //Конец блока вставки имён

            File folder = new File(newComplaintPath.getParent().toString());
            folder.mkdirs();
            FileOutputStream fileOut = new FileOutputStream(newComplaintPath.toFile());
            wbComplBlank.write(fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(imageAdd){
            ImageService imageService = new ImageService();
            imageService.inputImageToSheet(newComplaintPath, imageFileList, not.getNotId());
        }
    }

    /**
     * Метод возвращает имя для нового рекламационного акта
     * */
    private String getNewComplaintNumber(int supplierNumber, String complaintType){
        logger.info("Method getNewComplaintNumber(int supplierNumber, String complaintType) is launched...");
        ComplaintDaoImpl complaintDao = new ComplaintDaoImpl();
        StringBuilder  stringBuilder = new StringBuilder();
        DateTimeFormatter dtfYY = DateTimeFormatter.ofPattern("yy");
        DateTimeFormatter dtfYYYY = DateTimeFormatter.ofPattern("yyyy");
        List<Complaint> complaintList = complaintDao.getComplaintList(supplierNumber); //Список рекламаций для заданного поставщика
        List<Complaint> complaintListByType = new ArrayList<>();
        stringBuilder.append(complaintType);
        stringBuilder.append("-");
        if (!complaintList.isEmpty()) {
            for (Complaint compl : complaintList) {
                String[] complNumberElements = compl.getCompNumber().split("[-/]+");
                if(complaintType.equalsIgnoreCase(complNumberElements[0].trim())){
                    complaintListByType.add(compl);
                }
            }
        }
        if (complaintListByType.isEmpty()) {
            stringBuilder.append(complaintDao.getComplaintTitle(supplierNumber));
            stringBuilder.append("-01");
        } else {
            Complaint complaint = complaintListByType.get(complaintListByType.size() - 1);
            String[] complNumberElements = complaint.getCompNumber().split("[-/]+");
            int newComplNum = NumberUtils.createInteger(complNumberElements[2]);
            stringBuilder.append(complNumberElements[1]);
            stringBuilder.append("-");
            if(!complNumberElements[3].equals(LocalDate.now().format(dtfYYYY)) && !complNumberElements[3].equals(LocalDate.now().format(dtfYY))){
                stringBuilder.append("01");
            } else {
                if (newComplNum < 9) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(++newComplNum);
            }
        }
        stringBuilder.append("-");
        stringBuilder.append(LocalDate.now().format(dtfYYYY));
        logger.info("New Complaint Number: " + stringBuilder.toString());
        return  stringBuilder.toString();
    }

    /**
     * Метод возвращает имя-ЧЕРНОВИК для нового рекламационного акта
     * */
    private String getNewComplaintNumberDraft(int supplierNumber, String complaintType){
        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH-mm-ss");

        stringBuilder.append(getNewComplaintNumber(supplierNumber, complaintType));
        stringBuilder.append("_DRAFT_");
        stringBuilder.append(LocalDate.now().format(dtfDate));
        stringBuilder.append("_");
        stringBuilder.append(LocalDateTime.now().format(dtfTime));
        return stringBuilder.toString();
    }
}