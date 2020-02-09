package ru.yandex.sashanc.db;

import org.apache.commons.lang3.math.NumberUtils;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.yandex.sashanc.db.connection.ConnectionManagerImpl;
import ru.yandex.sashanc.db.connection.IConnectionManager;
import ru.yandex.sashanc.pojo.Complaint;
import ru.yandex.sashanc.pojo.Property;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ComplaintDao implements IComplaintDao {
    private static final Logger logger = Logger.getLogger(ComplaintDao.class);
    private IConnectionManager conManager = ConnectionManagerImpl.getInstance();


    /**
     * Метод возвращает новый номер для рекламации
     */
    @Override
    public String getNewComplaintNumber(int supplierNumber, String complaintType) {
        logger.info("Method getNewComplaintNumber(int supplierNumber) is launched...");
        List<Complaint> complaintList = getComplaintList(supplierNumber);
        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy");
        List<Complaint> complList;
        stringBuilder.append(complaintType);
        stringBuilder.append("-");
        if (complaintList.isEmpty()) {
            complList = Collections.emptyList();
        } else {
            List<Complaint> hkComplList = new ArrayList<>();
            List<Complaint> bkComplList = new ArrayList<>();
            List<Complaint> teComplList = new ArrayList<>();
            for (Complaint compl : complaintList) {
                String[] complNumberElements = compl.getCompNumber().split("[-/]+");
                switch (complNumberElements[0].toLowerCase().trim()) {
                    case "hk":
                        hkComplList.add(compl);
                        break;
                    case "bk":
                        bkComplList.add(compl);
                        break;
                    case "te":
                        teComplList.add(compl);
                        break;
                    default:
                        logger.info("Тип рекламации не определён: " + compl.getCompNumber());
                }
            }
            switch (complaintType.toLowerCase()) {
                case "hk":
                    complList = hkComplList;
                    break;
                case "bk":
                    complList = bkComplList;
                    break;
                case "te":
                    complList = teComplList;
                    break;
                default:
                    complList = Collections.emptyList();
            }
        }
        if (complList.isEmpty()) {
            try (Connection connection = conManager.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("SELECT complaintTitle FROM contracts WHERE supplier_id = ?")) {
                    statement.setInt(1, supplierNumber);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            stringBuilder.append(resultSet.getString("complaintTitle"));
                            logger.info(resultSet.getString("complaintTitle"));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            stringBuilder.append("-");
            stringBuilder.append("01");
        } else {
            Complaint complaint = complList.get(complList.size() - 1);
            String[] complNumberElements = complaint.getCompNumber().split("[-/]+");
            int newComplNum = NumberUtils.createInteger(complNumberElements[2]);
            stringBuilder.append(complNumberElements[1]);
            stringBuilder.append("-");
            if(!complNumberElements[3].equals(LocalDate.now().format(dtf))){
                stringBuilder.append("01");
            } else {
                if (newComplNum < 9) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(++newComplNum);
            }
        }
        stringBuilder.append("/");
        stringBuilder.append(LocalDate.now().format(dtf));
        logger.info("New Complaint Number: " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * Метод возвращает путь для хранения имени файлов документов РА
     */
    @Override
    public String getNewComplaintPath(int supplierNumber) {
        logger.info("Method getNewComplaintPath(int supplierNumber) is launched...");
        //TODO Получить короткое имя из БД Access
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh-mm-ss");
        String complaintName = "E:\\work\\blank_complaint_" + LocalDate.now().toString() + "_" + LocalTime.now().format(dtf) + ".xlsx";
        return complaintName;
    }

    /**
     * Метод возвращает список всех рекламаций заданного поставщиков
     */
    @Override
    public List<Complaint> getComplaintList(int supplierNumber) {
        logger.info("Method getComplaintList(int supplierNumber) is launched...");
        Integer supplier = supplierNumber;
        List<Complaint> complaintList;
        List<Path> pathList = getFileList();
        List<Path> pathSupplier = new ArrayList<>();
        for (Path path: pathList) {
            String fileName1 = path.getFileName().toString();
            String fileName2 = path.toFile().getName();
            if(path.toFile().getName().contains(supplier.toString())){
                logger.info("Имя поставщика для получения номера сообщения path.getFileName().toString() = " + fileName1 + " path.toFile().getName() = " + fileName2);
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

                            logger.info(row.getCell(1).getStringCellValue());
                            Complaint complaint = new Complaint(
                                    row.getCell(0).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                                    row.getCell(1).getStringCellValue(),

                                    //TODO сделать парсер строки на номера Сообщений
                                    row.getCell(2).getStringCellValue(),
                                    row.getCell(3).getStringCellValue(),
                                    row.getCell(4).getStringCellValue(),
                                    row.getCell(5).getStringCellValue(),
                                    (int) row.getCell(6).getNumericCellValue(),
                                    (int) row.getCell(7).getNumericCellValue(),
                                    row.getCell(8).getStringCellValue(),
                                    row.getCell(9).getStringCellValue(),
                                    invoice,
                                    date11,
                                    date12,
                                    date13,
                                    row.getCell(16).getNumericCellValue(),
                                    row.getCell(17).getStringCellValue(),

                                    row.getCell(19).getNumericCellValue(),
                                    row.getCell(20).getStringCellValue(),
                                    row.getCell(21).getStringCellValue()
                            );
                            complaints.add(complaint);
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
}
