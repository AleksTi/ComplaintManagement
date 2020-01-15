package ru.yandex.sashanc.services;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.List;

public interface IImageService {
    void inputImageToSheet();
    List<File> getImages(String path);
}
