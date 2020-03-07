package ru.yandex.sashanc.db;

import ru.yandex.sashanc.pojo.Complaint;
import ru.yandex.sashanc.pojo.Contract;
import ru.yandex.sashanc.pojo.Employee;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface IComplaintDao {
    String COLUMN_SETTINGS_PROPERTY_VALUE = "propertyValue";

    String getNewComplaintNumber(int supplierNumber, String complaintType);
    Path getComplaintsPath(int supplierNumber);
    List<Complaint> getComplaintList();
    List<Complaint> getComplaintList(int supplierNumber);
    void getComplaintData(Complaint complaint);
    void insertComplaintDB(List<Complaint> complaints);
    void insertDataComplaintExcel(List<Complaint> complaints, Contract contract, Map<String,Employee> employees, Path complaintPath);
}
