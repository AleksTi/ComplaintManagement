package ru.yandex.sashanc.db;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import ru.yandex.sashanc.db.connection.ConnectionManagerImpl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ImageDaoImpl implements IimageDao {
    private static final Logger logger = Logger.getLogger(ImageDaoImpl.class);
    private IConnectionManager conManager = ConnectionManagerImpl.getInstance();

    //TODO Сделать поиск только в папке 2020
    //TODO В прошлых папках делать поиск лишь по спецзапросу, который будет в меню


    @Override
    public List<File> getImageList(int notId){
        List<File> imageList;
        Path imagePathDb = getImagePathFromDb(notId);
        if(!imagePathDb.toString().equals("")){
            imageList = getImageList(imagePathDb);
            logger.info("Изображения для сообщения " + notId + " найдены в БД " + imagePathDb);
        } else {
            imageList = getImageListServer(notId);
            logger.info("Изображения для сообщения " + notId + " НЕ найдены в БД..!!");
        }
        return imageList;
    }

    private List<File> getImageListServer(int notId){
        logger.info("Method getImageListServer(int notId) is launched...");
        List<File> imageList = Collections.emptyList();
        Map<Integer, String> imageDirs = new HashMap<>();
        getImagePaths(getImagePathDefault(), imageDirs);
        for(Map.Entry<Integer, String> entry : imageDirs.entrySet()){
            logger.info("(HashMap) Найдена на сервере для сообщения " + entry.getKey() + " папка " + entry.getValue());
        }
        inputImagePathsDb(imageDirs);
        if(imageDirs.containsKey(notId)){
            imageList = getImageList(Paths.get(imageDirs.get(notId)));
        } else {
            logger.info("Изображения для сообщения " + notId + " НЕ найдены на СЕРВЕРЕ..!!");
        }
        return imageList;
    }

    /**
     * Метод возвращает список всех файлов изображений в заданной папке
     */
    private List<File> getImageList(Path imagePath) {
        logger.info("Method getImageList(Path imagePath) is launched...");
        List<File> imageList = new ArrayList<>();
        File imagesDir = new File(imagePath.toString());
        if(imagesDir.exists()) {
            File[] fileList = imagesDir.listFiles();
            if(fileList.length != 0) {
                for (File file : fileList) {
                    if (file.isFile()) {
                        if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("jpg") ||
                                FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("png") ||
                                FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("bmp")) {
                            imageList.add(file);
                        }
                    }
                }
            } else {
                logger.info("Полученный путь для изображений не содержит изображения " + imagePath.toString());
            }
        } else {
            logger.info("Полученный путь для изображений недоступен " + imagePath.toString());
        }
        return imageList;
    }

    /**
     * Метод выполняет рекурсивный поиск всех папок в общей папке и вносит их в HashMap
     */
    private void getImagePaths(Path defaultPath, Map<Integer, String> imageDir) {
        logger.info("Method getImagePaths(Path defaultPath, Map<Integer, String> imageDir) is launched for " + defaultPath);
        File imagesDir = new File(defaultPath.toString());
        File[] fileList = imagesDir.listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                if (NumberUtils.isDigits(file.getName()) && file.getName().trim().length() == 10) {
                    imageDir.put(Integer.parseInt(file.getName()), file.getAbsolutePath());
                }
                getImagePaths(Paths.get(file.getAbsolutePath()), imageDir);
            }
        }
    }

    private void inputImagePathsDb(Map<Integer, String> imageDirs){
        logger.info("Method inputImagePathsDb(Map<Integer, String> imageDirs) is launched...");
        try (Connection connection = conManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO notifications (id, imageLink ) VALUES (?, ?)")) {
                for(Map.Entry<Integer, String> entry : imageDirs.entrySet()){
                    statement.setLong(1, entry.getKey());
                    statement.setString(2, entry.getValue());
                    int addedRow = statement.executeUpdate();
                    logger.info("Method executeUpdate() is done..." + addedRow);
                    if (addedRow == 1) {
                        logger.info("Link for notification " + entry.getKey() + " is added " + entry.getValue());
                    } else {
                        logger.info("Link for notification " + entry.getKey() + " is NOT added " + entry.getValue());
                    }
                }
            }
        } catch (SQLException e) {
            logger.info("SQLException: " + e);
        }


    }

    /**
     * Метод возвращает путь из БД к изображениям
     * */
    private Path getImagePathFromDb(int notId){
        logger.info("Method getImagePathFromDb(int notId) is launched...");
        Path imagePathDb = Paths.get("");
        try (Connection connection = conManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT imageLink FROM notifications WHERE id = ?")) {
                statement.setLong(1, notId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        imagePathDb = Paths.get(resultSet.getString("imageLink"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.info(e);
        }
        return imagePathDb;
    }

    /**
     * Метод возвращает общую папку из БД, где хранятся все папки c фотографиями
     */
    private Path getImagePathDefault() {
        logger.info("Method getImagePathDefault() is launched...");
        Path getImagePathDefault = Paths.get("");
        try (Connection connection = conManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT propertyValue FROM settings WHERE id = ?")) {
                statement.setInt(1, 6);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        getImagePathDefault = Paths.get(resultSet.getString("propertyValue"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.info(e);
        }
        return getImagePathDefault;
    }

    public Map<Integer, Path> getImagesPathsDB(){
        logger.info("Method getImagePathFromDb(int notId) is launched...");
        Map<Integer, Path> imageMap = new HashMap<>();
        try (Connection connection = conManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT id, imageLink FROM notifications")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        imageMap.put(
                                resultSet.getInt("id"),
                                Paths.get(resultSet.getString("imageLink"))
                        );
                    }
                }
            }
        } catch (SQLException e) {
            logger.info(e);
        }
        return imageMap;
    }
}
