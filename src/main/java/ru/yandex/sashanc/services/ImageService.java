package ru.yandex.sashanc.services;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ImageService implements IImageService {

    @Override
    public void inputImageToSheet() {
        try (InputStream isImage = new FileInputStream("E:\\work\\image1.jpg");
             InputStream isExcel = new FileInputStream("E:\\work\\blank_complaint1.xlsx")) {
            XSSFWorkbook wb = new XSSFWorkbook(isExcel);
            byte[] bytes = IOUtils.toByteArray(isImage);
            int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            CreationHelper helper = wb.getCreationHelper();
            Sheet sheet = wb.getSheetAt(0);
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(2);
            anchor.setRow1(73);
            anchor.setCol2(24);
            anchor.setRow2(89);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            FileOutputStream fileOut = new FileOutputStream("E:\\work\\blank_complaint1.xlsx");
            wb.write(fileOut);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public List<File> getImages(String path) {
        Map<Integer, String> imageDir = new HashMap<>();
        List<File> fileList = new ArrayList<>();
        getFileList(path, fileList, imageDir);
        for (Map.Entry<Integer, String> entry : imageDir.entrySet()){
            System.out.println("Сообщение " + entry.getKey() + " путь " + entry.getValue());
        }
        return fileList;
    }

    //TODO Сначала автоматический поиск, если не указан путь; если указан путь, то искать в указанной папке.
    //TODO Запускать поиск фотографий не всегда. Сначала искать а файле со списком сообщений, которые были найдены в предыдущем поиске.
    //TODO Потом включать новый поиск и фиксировать найденные сообщения.
    //TODO Дать возможность выбрать папку для фотографий вручную.
    private void getFileList(String directoryName, List<File> files, Map<Integer, String> imageDir) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                if(NumberUtils.isDigits(file.getName())){
                    imageDir.put(Integer.parseInt(file.getName()), file.getAbsolutePath());
                }
                getFileList(file.getAbsolutePath(), files, imageDir);
            }
        }
    }
}
