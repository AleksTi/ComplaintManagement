package ru.yandex.sashanc.services;

import ru.yandex.sashanc.pojo.Complaint;
import ru.yandex.sashanc.pojo.Notification;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public interface IComplaintService {
    List<Complaint> generateComplaint(List<Notification> notList, String text, String text1, LocalDate date, boolean selected, String value, List<File> fileList);
}
