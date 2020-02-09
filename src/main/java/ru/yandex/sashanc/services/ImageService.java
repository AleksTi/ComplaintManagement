package ru.yandex.sashanc.services;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.yandex.sashanc.db.IimageDao;
import ru.yandex.sashanc.db.ImageDaoImpl;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class ImageService implements IimageService {
    private static final Logger logger = Logger.getLogger(ImageService.class);

    @Override
    /**
     * Метод получает в качестве параметров путь к файлу РА и список фото
     *
     */
    public void inputImageToSheet(Path complaintPath, List<File> imageList, int notificationId) {
        if(imageList.isEmpty()){
            IimageDao imageDao = new ImageDaoImpl();
            imageList = imageDao.getImageList(notificationId);
        }
        try (InputStream isExcel = new FileInputStream(complaintPath.toFile())) {
            XSSFWorkbook wb = new XSSFWorkbook(isExcel);
            List<Integer[]> imageMap = new ArrayList<>();
            imageMap.add(new Integer[]{2, 73, 25, 92});
            imageMap.add(new Integer[]{27, 73, 49, 92});
            imageMap.add(new Integer[]{2, 93, 25, 112});
            imageMap.add(new Integer[]{27, 93, 49, 112});
            imageMap.add(new Integer[]{2, 117, 25, 136});
            imageMap.add(new Integer[]{27, 117, 49, 136});
            imageMap.add(new Integer[]{2, 137, 25, 156});
            imageMap.add(new Integer[]{27, 137, 49, 156});
            byte[] bytes;
            int imageCounter = 0;
            for (File imageFile : imageList) {
                try (InputStream isImage = new FileInputStream(imageFile)) {
                    bytes = IOUtils.toByteArray(isImage);
                }
                int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
                CreationHelper helper = wb.getCreationHelper();
                Sheet sheet = wb.getSheetAt(0);
                Drawing drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(imageMap.get(imageCounter)[0]);
                anchor.setRow1(imageMap.get(imageCounter)[1]);
                anchor.setCol2(imageMap.get(imageCounter)[2]);
                anchor.setRow2(imageMap.get(imageCounter)[3]);
                Picture pict = drawing.createPicture(anchor, pictureIdx);
                imageCounter++;
            }
            FileOutputStream fileOut = new FileOutputStream(complaintPath.toFile());
            wb.write(fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
