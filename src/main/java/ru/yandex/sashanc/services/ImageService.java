package ru.yandex.sashanc.services;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.yandex.sashanc.db.IimageDao;
import ru.yandex.sashanc.db.ImageDaoImpl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
        if(!imageList.isEmpty()){
            List<Integer[]> imageMap;
            List<Integer[]> imageMap169 = getImageMap169();
            List<Integer[]> imageMap43 = getImageMap43();
            try (InputStream isExcel = new FileInputStream(complaintPath.toFile())) {
                XSSFWorkbook wb = new XSSFWorkbook(isExcel);
                byte[] bytes;
                int imageCounter = 0;
                double imageHeight;
                double imageWidth;
                for (File imageFile : imageList) {
                    try (InputStream isImage = new FileInputStream(imageFile)) {
                        bytes = IOUtils.toByteArray(isImage);
                    }
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    imageHeight = bufferedImage.getHeight();
                    imageWidth = bufferedImage.getWidth();
                    logger.info("Размеры изображения " + imageFile.getName() + " : " + imageHeight + " х " + imageWidth);
                    if(imageWidth/imageHeight > 1.56){
                        imageMap = imageMap169;
                    } else {
                        imageMap = imageMap43;
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
                    if (imageCounter > 7){
                        break;
                    }
                }
                FileOutputStream fileOut = new FileOutputStream(complaintPath.toFile());
                wb.write(fileOut);
            } catch (FileNotFoundException e) {
                logger.info(e);
            } catch (IOException e) {
                logger.info(e);
            }
        } else {
            logger.info("Изображения для сообщения " + notificationId + " НЕ найдены..!!");
        }
    }

    private List<Integer[]> getImageMap169(){
        List<Integer[]> imageMap = new ArrayList<>();
        imageMap.add(new Integer[]{1, 70, 25, 84});
        imageMap.add(new Integer[]{27, 70, 50, 84});
        imageMap.add(new Integer[]{1, 89, 25, 103});
        imageMap.add(new Integer[]{27, 89, 50, 103});
        imageMap.add(new Integer[]{1, 111, 25, 125});
        imageMap.add(new Integer[]{27, 111, 50, 125});
        imageMap.add(new Integer[]{1, 130, 25, 144});
        imageMap.add(new Integer[]{27, 130, 50, 144});
        return imageMap;
    }

    private List<Integer[]> getImageMap43(){
        List<Integer[]> imageMap = new ArrayList<>();
        imageMap.add(new Integer[]{1, 70, 23, 87});
        imageMap.add(new Integer[]{27, 70, 47, 87});
        imageMap.add(new Integer[]{1, 89, 23, 106});
        imageMap.add(new Integer[]{27, 89, 47, 106});
        imageMap.add(new Integer[]{1, 111, 23, 128});
        imageMap.add(new Integer[]{27, 111, 47, 128});
        imageMap.add(new Integer[]{1, 130, 23, 147});
        imageMap.add(new Integer[]{27, 130, 47, 147});
        return imageMap;
    }
}
