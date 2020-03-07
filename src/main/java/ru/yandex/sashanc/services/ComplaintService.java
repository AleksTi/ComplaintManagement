package ru.yandex.sashanc.services;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import ru.yandex.sashanc.db.*;
import ru.yandex.sashanc.pojo.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class ComplaintService implements IComplaintService {
    private static final Logger logger = Logger.getLogger(ComplaintService.class);

    /**
     * Метод создаёт новый рекламационный акт из данных сообщения
     */
    @Override
    public List<Complaint> generateComplaint(List<Notification> notList, String descriptionRu, String descriptionEn, LocalDate complaintDate, boolean imageAdd, String complaintType, List<File> imageFileList) {
        logger.info("Method generateComplaint is launched...");
        List<Complaint> complaints = new CopyOnWriteArrayList<>();
        IEmployeeDao employeeDao = new EmployeeDaoImpl();
        IComplaintDao complaintDao = new ComplaintDaoImpl();
        IContractDao contractDao = new ContractDaoImpl();
        IPartDao partDao = new PartDaoImpl();

        Map<String, Employee> employees = employeeDao.getEmployeeList();
        Contract contract = contractDao.getContract(notList.get(0).getSupplierNumber());
        String complaintNumber = getNewComplaintNumberDraft(notList.get(0).getSupplierNumber(), complaintType);
        Path complaintPath = Paths.get(complaintDao.getComplaintsPath(notList.get(0).getSupplierNumber()).toString()
                + "\\" + complaintNumber + "\\" + complaintNumber + ".xlsx");
        logger.info("Полное Имя нового рекламационного акта " + complaintPath);


        String invoice;
        boolean isNotAdded;
        String deviationDescSap;

        for (Notification not : notList) {
            isNotAdded = false;
            deviationDescSap = "";
            invoice = "";
            if (not.getDefectDescription() != null) {
                deviationDescSap = not.getDefectDescription();
            }
            if (not.getInvoice() != null) {
                invoice = not.getInvoice();
            }
            for (Complaint com : complaints) {
                if (not.getMaterialNumber().equals(com.getPartNumber()) && invoice.equals(com.getInvoice()) &&
                        deviationDescSap.equals(com.getDeviationDescriptionSap())) {
                    com.setDefectQuantity(com.getDefectQuantity() + not.getComplaintQuantity());
                    com.setDefectQuantityToPpm(com.getDefectQuantityToPpm() + not.getComplaintQuantity());
                    com.getRelNotList().add(not.getNotId());
                    isNotAdded = true;
                }
            }
            if(!isNotAdded){
                Part part = partDao.getContractPart(not.getSupplierNumber(), not.getMaterialNumber());
                complaints.add(createComplaint(not, complaintNumber, complaintDate, part, descriptionRu, descriptionEn, contract));
            }
        }
        complaintDao.insertDataComplaintExcel(complaints, contract, employees, complaintPath);
        if (imageAdd) {
            ImageService imageService = new ImageService();
            imageService.inputImageToSheet(complaintPath, imageFileList, notList.get(0).getNotId());
        }
        complaintDao.insertComplaintDB(complaints);
        return complaints;
    }

    /**
     * Метод возвращает имя для нового рекламационного акта (сначала проверяет в БД РА, если там нет, то присваевает новый номер)
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

    private String getShortNewComplNumberDraft(int supplierNumber, String complaintType){
        return getNewComplaintNumber(supplierNumber, complaintType) + "_DRAFT";
    }

    /**
     * Метод возвращает имя-ЧЕРНОВИК для нового рекламационного акта
     * */
    private String getNewComplaintNumberDraft(int supplierNumber, String complaintType){
        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH-mm-ss");
        stringBuilder.append(getShortNewComplNumberDraft(supplierNumber, complaintType));
        stringBuilder.append("_");
        stringBuilder.append(LocalDate.now().format(dtfDate));
        stringBuilder.append("_");
        stringBuilder.append(LocalDateTime.now().format(dtfTime));
        return stringBuilder.toString();
    }

    private Complaint createComplaint(Notification not, String complaintNumber, LocalDate complaintDate,
                                      Part part, String descriptionRu, String descriptionEn, Contract contract){
        Complaint complaint = new Complaint();
        complaint.setPartNumber(part.getPartNumber());
        complaint.setPartNameRu(part.getPartNameRu());
        complaint.setPartNameEn(part.getPartNameEn());
        complaint.setRelNotList(new ArrayList<>());
        complaint.getRelNotList().add(not.getNotId());
        complaint.setCompNumber(complaintNumber);
        complaint.setCompDate(complaintDate);
        complaint.setSupplierId(not.getSupplierNumber());
        complaint.setStatus("DRAFT");
        //TODO Сделать выпадающий список для определения места возникновения
        complaint.setPlaceDetected("");
        complaint.setDefectQuantity(not.getComplaintQuantity());
        //TODO Сделать отдельное получение количества для PPM
        complaint.setDefectQuantityToPpm(not.getComplaintQuantity());
        if (descriptionRu.equals("")) {
            complaint.setDeviationDescriptionRu(not.getDefectDescription());
        } else {
            complaint.setDeviationDescriptionRu(descriptionRu);
        }
        if (descriptionEn.equals("")) {
            complaint.setDeviationDescriptionEn("n/a");
        } else {
            complaint.setDeviationDescriptionEn(descriptionEn);
        }
        //TODO Указать имя папки, где будет создан РА Excel
        complaint.setLink("");
        if (not.getInvoice() != null) {
            complaint.setInvoice(not.getInvoice());
        } else {
            complaint.setInvoice("");
        }
        if (not.getDefectDescription() != null) {
            complaint.setDeviationDescriptionSap(not.getDefectDescription());
        } else {
            complaint.setDeviationDescriptionSap("");
        }
        complaint.setShippingDate(not.getDeliveryNoteDate());
        complaint.setDeliveryDate(not.getPostDate());
        complaint.setDetectionDate(not.getNotDate());
        //TODO Продумать механизм работы с затратами
        complaint.setTotalCost(0.0);
        complaint.setCompContract(contract.getContractNumber());
        complaint.setCompContractDate(contract.getDate());
        return complaint;
    }
}