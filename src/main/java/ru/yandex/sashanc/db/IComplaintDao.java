package ru.yandex.sashanc.db;

import ru.yandex.sashanc.pojo.Complaint;
import ru.yandex.sashanc.pojo.Notification;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public interface IComplaintDao {
    String COLUMN_SETTINGS_PROPERTY_VALUE = "propertyValue";

    String getNewComplaintNumber(int supplierNumber, String complaintType);
    String getNewComplaintPath(int supplierNumber);
    List<Complaint> getComplaintList();
    List<Complaint> getComplaintList(int supplierNumber);
}
