package ru.yandex.sashanc.db;

import ru.yandex.sashanc.pojo.Complaint;

import java.nio.file.Path;
import java.util.List;

public interface IComplaintDao {
    String COLUMN_SETTINGS_PROPERTY_VALUE = "propertyValue";

    String getNewComplaintNumber(int supplierNumber, String complaintType);
    Path getNewComplaintPath(int supplierNumber, String newComplaintNumber);
    List<Complaint> getComplaintList();
    List<Complaint> getComplaintList(int supplierNumber);
    void getComplaintData(Complaint complaint);
}
