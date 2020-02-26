package ru.yandex.sashanc.db;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.yandex.sashanc.db.connection.ConnectionManagerImpl;
import ru.yandex.sashanc.db.connection.IConnectionManager;
import ru.yandex.sashanc.pojo.Complaint;
import ru.yandex.sashanc.pojo.Property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ComplaintDaoImpl implements IComplaintDao {
    private static final Logger logger = Logger.getLogger(ComplaintDaoImpl.class);
    private IConnectionManager conManager = ConnectionManagerImpl.getInstance();

//    //TODO Проверить необходимость данного метода
//    @Override
//    public Map<String, String> getComplaintData(int supplierId){
//        Map<String, String> complaintData = new HashMap<>();
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//        try (Connection connection = conManager.getConnection()) {
//            try (PreparedStatement statement = connection.prepareStatement("SELECT contractName, shortName, contractNumber, date," +
//                    " paymentItem, bank, contractAdress FROM contracts WHERE suppliers_id = ?")) {
//                statement.setInt(1, supplierId);
//                try (ResultSet resultSet = statement.executeQuery()) {
//                    while (resultSet.next()) {
//                        complaintData.put("contractName", resultSet.getString("contractName"));
//                        complaintData.put("shortName", resultSet.getString("shortName"));
//                        complaintData.put("contractNumber", resultSet.getString("contractNumber"));
//                        complaintData.put("date", resultSet.getDate("date").toLocalDate().format(dtf));
//                        complaintData.put("paymentItem", resultSet.getString("paymentItem"));
//                        complaintData.put("bank", resultSet.getString("bank"));
//                        complaintData.put("contractAdress", resultSet.getString("contractAdress"));
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return complaintData;
//    }

    @Override
    public void getComplaintData(Complaint complaint) {

    }

    /**
     * Метод возвращает новый номер для рекламации
     */
    @Override
    public String getNewComplaintNumber(int supplierNumber, String complaintType) {
        return "mock";
    }

    /**
     * Метод возвращает часть имени -поставщик- для рекламационного акта
     * */
    public String getComplaintTitle(int supplierNumber){
        String complaintTitle = "";
        try (Connection connection = conManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT complaintTitle FROM contracts WHERE suppliers_id = ?")) {
                statement.setInt(1, supplierNumber);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        complaintTitle = resultSet.getString("complaintTitle");
                    }
                }
            }
        } catch (SQLException e) {
            logger.info(e);
        }
        return complaintTitle;
    }

    /**
     * Метод возвращает путь для хранения имени файлов документов РА
     */
    @Override
    public Path getNewComplaintPath(int supplierNumber, String newComplaintNumber) {
        logger.info("Method getNewComplaintPath(int supplierNumber) is launched...");
        String complFilePath = "";
        String shortName = "";
        try(Connection connection = conManager.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT propertyValue FROM settings WHERE id = 7")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()){
                        complFilePath = resultSet.getString("propertyValue");
                    }
                }
            }
            try(PreparedStatement statement = connection.prepareStatement("SELECT supplierShortName FROM contracts WHERE suppliers_id = ?")) {
                statement.setInt(1, supplierNumber);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()){
                        shortName = resultSet.getString("supplierShortName");
                    }
                }
            }
        } catch (SQLException e) {
            logger.info(e);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");
        return Paths.get(complFilePath + "\\" + LocalDate.now().format(dtf) + "\\" + shortName + "\\" + newComplaintNumber + "\\" + newComplaintNumber + ".xlsx");
    }

    /**
     * Метод возвращает список всех рекламаций из БД Excel РА для заданного поставщика
     */
    @Override
    public List<Complaint> getComplaintList(int supplierNumber) {
        logger.info("Method getComplaintList(int supplierNumber) is launched...");
        Integer supplier = supplierNumber;
        List<Complaint> complaintList;
        List<Path> pathList = getFileList();
        List<Path> pathSupplier = new ArrayList<>();
        for (Path path: pathList) {
            if(path.toFile().getName().contains(supplier.toString())){
                pathSupplier.add(path);
            }
        }
        complaintList = getComplaintList(pathSupplier);
        return complaintList;
    }

    /**
     * Метод возвращает список всех рекламаций всех поставщиков
     */
    @Override
    public List<Complaint> getComplaintList() {
        logger.info("Method getComplaintList() is launched...");
        List<Complaint> complaintList;
        complaintList = getComplaintList(getFileList());
        return complaintList;
    }

    /**
     * Метод получает список папок из БД Access
     */
    private Map<Integer, Property> getProperties(){
        logger.info("Method getProperties() is launched...");
        Map<Integer, Property> properties = new HashMap<>();
        try (Connection connection = conManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM settings")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()){
                        Property property = new Property();
                        property.setId(resultSet.getInt("id"));
                        property.setProperty(resultSet.getString("property"));
                        property.setPropertyValue(resultSet.getString("propertyValue"));
                        properties.put(resultSet.getInt("id"), property);
                    }
                }
            }
        } catch (SQLException e) {
            logger.info(e);
        }
        return properties;
    }

    /**
     * Метод возвращает список файлов БД РА*/
    private List<Path> getFileList(){
        logger.info("Method getFileList() is launched...");
        List<Path> pathList = new ArrayList<>();
        Path complaintDbPath = Paths.get(getProperties().get(8).getPropertyValue());
        logger.info("complaintDbPath: " + complaintDbPath);
        File complaintDbDir = new File(complaintDbPath.toString());
        File[] complaintDbDirArr = complaintDbDir.listFiles();
        File complaintDbDirLast = complaintDbDirArr[complaintDbDirArr.length - 1];
        File[] complaintDbDirLastArr = complaintDbDirLast.listFiles();
        for(File file : complaintDbDirLastArr){
            if(file.isFile()){
                if(!file.isHidden()){
                    pathList.add(Paths.get(file.getAbsolutePath()));
                }
            }
        }
        return pathList;
    }

    /**
     * Метод возвращает список РА из списка файлов
     */
    private List<Complaint> getComplaintList(List<Path> pathList){
        logger.info("Method getComplaintList(List<Path> pathList) is launched...");
        List<Complaint> complaints = new ArrayList<>();
        for (Path path: pathList) {
            logger.info(path.getFileName().toString());
            try (XSSFWorkbook wbComplBlank = new XSSFWorkbook(new FileInputStream(path.toFile()))) {
                XSSFSheet sheet = wbComplBlank.getSheetAt(0);
                Iterator rowIter = sheet.rowIterator();
                while (rowIter.hasNext()) {
                    XSSFRow row = (XSSFRow) rowIter.next();
                    if (row.getRowNum() > 0) {
                        if (row.getCell(0).getCellType() == CellType.NUMERIC &&
                                row.getCell(1).getCellType() == CellType.STRING &&
                                row.getCell(5).getCellType() == CellType.STRING &&
                                row.getCell(7).getCellType() == CellType.NUMERIC) {

                            LocalDate date11 = null;
                            LocalDate date12 = null;
                            LocalDate date13 = null;
                            String invoice = "";
                            if (row.getCell(11).getCellType() == CellType.NUMERIC) {
                                date11 = row.getCell(11).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            }
                            if (row.getCell(12).getCellType() == CellType.NUMERIC) {
                                date12 = row.getCell(12).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            }
                            if (row.getCell(13).getCellType() == CellType.NUMERIC) {
                                date13 = row.getCell(13).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            }
                            if (row.getCell(10).getCellType() == CellType.NUMERIC) {
                                invoice =  String.valueOf(row.getCell(10).getNumericCellValue());
                            } else {
                                invoice = row.getCell(10).getStringCellValue();
                            }

                            logger.info("Complaint started: " + row.getCell(1).getStringCellValue());
                            Complaint complaint = new Complaint();
                            logger.info("Complaint CompDate: " + row.getCell(0).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            complaint.setCompDate(row.getCell(0).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            logger.info("Complaint CompNumber: " + row.getCell(1).getStringCellValue());
                            //TODO сделать парсер строки на номера Сообщений
                            complaint.setCompNumber(row.getCell(1).getStringCellValue());
                            logger.info("Complaint RelNotList: " + row.getCell(2).getStringCellValue());
                            complaint.setRelNotList(row.getCell(2).getStringCellValue());
                            logger.info("Complaint PlaceDetected: " + row.getCell(3).getStringCellValue());
                            complaint.setPlaceDetected(row.getCell(3).getStringCellValue());
                            logger.info("Complaint PartNumber: " + row.getCell(5).getStringCellValue());
                            complaint.setPartNumber(row.getCell(5).getStringCellValue());
                            logger.info("Complaint PartNameRu: " + row.getCell(4).getStringCellValue());
                            //TODO сделать парсер имени для разбивки на русское и англ название
                            complaint.setPartNameRu(row.getCell(4).getStringCellValue());
                            logger.info("Complaint DefectQuantity: " + (int) row.getCell(6).getNumericCellValue());
                            complaint.setDefectQuantity((int) row.getCell(6).getNumericCellValue());
                            logger.info("Complaint DefectQuantityToPpm: " + (int) row.getCell(7).getNumericCellValue());
                            complaint.setDefectQuantityToPpm((int) row.getCell(7).getNumericCellValue());
                            logger.info("Complaint DeviationDescription: " + row.getCell(8).getStringCellValue());
                            complaint.setDeviationDescription(row.getCell(8).getStringCellValue());
                            logger.info("Complaint Link: " + row.getCell(9).getStringCellValue());
                            complaint.setLink(row.getCell(9).getStringCellValue());
                            logger.info("Complaint Invoice: " + invoice);
                            complaint.setInvoice(invoice);
                            logger.info("Complaint ShippingDate: " + date11);
                            complaint.setShippingDate(date11);
                            logger.info("Complaint DeliveryDate: " + date12);
                            complaint.setDeliveryDate(date12);
                            logger.info("Complaint DetectionDate: " + date13);
                            complaint.setDetectionDate(date13);
                            logger.info("Complaint TotalCost: " + row.getCell(16).getNumericCellValue());
                            complaint.setTotalCost(row.getCell(16).getNumericCellValue());
                            logger.info("Complaint ExtraDelivery: " + row.getCell(17).getStringCellValue());
                            complaint.setExtraDelivery(row.getCell(17).getStringCellValue());
                            logger.info("Complaint PaidCost: " + row.getCell(19).getNumericCellValue());
                            complaint.setPaidCost(row.getCell(19).getNumericCellValue());
                            logger.info("Complaint Decision: " + row.getCell(20).getStringCellValue());
                            complaint.setDecision(row.getCell(20).getStringCellValue());
                            logger.info("Complaint Annotation: " + row.getCell(21).getStringCellValue());
                            complaint.setAnnotation(row.getCell(21).getStringCellValue());
                            complaints.add(complaint);
                            logger.info("Complaint added: " + row.getCell(1).getStringCellValue());
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Complaint complaint : complaints){
            logger.info("Дата " + complaint.getCompDate() + ", Номер рекламации: " + complaint.getCompNumber()+ ", Дата получения: " + complaint.getDeliveryDate());
        }
        return complaints;
    }

    private List<Complaint> getComplaintNameList(Path path) {
        logger.info("Method getComplaintNameList(Path pathList) is launched...");
        List<Complaint> complaints = new ArrayList<>();
        logger.info(path.getFileName().toString());
        try (XSSFWorkbook wbComplBlank = new XSSFWorkbook(new FileInputStream(path.toFile()))) {
            XSSFSheet sheet = wbComplBlank.getSheetAt(0);
            Iterator rowIter = sheet.rowIterator();
            while (rowIter.hasNext()) {
                XSSFRow row = (XSSFRow) rowIter.next();
                if (row.getRowNum() > 0) {
                    if (row.getCell(0).getCellType() == CellType.NUMERIC &&
                            row.getCell(1).getCellType() == CellType.STRING &&
                            row.getCell(5).getCellType() == CellType.STRING &&
                            row.getCell(7).getCellType() == CellType.NUMERIC) {
                        logger.info("Complaint started: " + row.getCell(1).getStringCellValue());
                        Complaint complaint = new Complaint();
                        logger.info("Complaint CompDate: " + row.getCell(0).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        complaint.setCompDate(row.getCell(0).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        logger.info("Complaint CompNumber: " + row.getCell(1).getStringCellValue());
                        complaint.setCompNumber(row.getCell(1).getStringCellValue());
                        logger.info("Complaint PartNumber: " + row.getCell(5).getStringCellValue());
                        complaint.setPartNumber(row.getCell(5).getStringCellValue());
                        logger.info("Complaint DefectQuantityToPpm: " + (int) row.getCell(7).getNumericCellValue());
                        complaint.setDefectQuantityToPpm((int) row.getCell(7).getNumericCellValue());
                        complaints.add(complaint);
                        logger.info("Complaint added: " + row.getCell(1).getStringCellValue());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Complaint complaint : complaints) {
            logger.info("getComplaintNameList(Path pathList):  Дата " + complaint.getCompDate() + ", Номер рекламации: " + complaint.getCompNumber());
        }
        return complaints;
    }
}
