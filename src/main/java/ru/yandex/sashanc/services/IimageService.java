package ru.yandex.sashanc.services;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface IimageService {
    void inputImageToSheet(Path complaintPath, List<File> imageList, int notificationId);
}
