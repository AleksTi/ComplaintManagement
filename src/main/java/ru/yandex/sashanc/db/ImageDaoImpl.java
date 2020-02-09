package ru.yandex.sashanc.db;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import ru.yandex.sashanc.db.connection.ConnectionManagerImpl;
import ru.yandex.sashanc.db.connection.IConnectionManager;

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

    @Override
    public List<File> getImageList(int notId){
        List<File> imageList;
        imageList = getImageListDb(notId);
        if(imageList.isEmpty()){
            imageList = getImageListServer(notId);
        }
        return imageList;
    }

    private List<File> getImageListDb(int notId){
        logger.info("Method getImageListDb(int notId) is launched...");
        List<File> imageListDb = Collections.emptyList();
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
        if(!imagePathDb.toString().equals("")){
            imageListDb = getImageList(imagePathDb);
        }
        return imageListDb;
    }

    private List<File> getImageListServer(int notId){
        logger.info("Method getImageListServer(int notId) is launched...");
        List<File> imageList = Collections.emptyList();
        Map<Integer, String> imageDirs = new HashMap<>();
        getImagePaths(getImagePathDefault(), imageDirs);
        for(Map.Entry<Integer, String> entry : imageDirs.entrySet()){
            logger.info("Сообщение в HashMap " + entry.getKey() + " - " + entry.getValue());
        }
        if(imageDirs.containsKey(notId)){
            imageList = getImageList(Paths.get(imageDirs.get(notId)));
        }
        return imageList;
    }

    /**
     * Метод возвращает список всех файлов в папке
     */
    private List<File> getImageList(Path imagePath) {
        logger.info("Method getImageList(Path imagePath) is launched...");
        List<File> imageList = new ArrayList<>();
        File imagesDir = new File(imagePath.toString());
        File[] fileList = imagesDir.listFiles();
        for(File file : fileList){
            if(file.isFile()){
                imageList.add(file);
            }
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
                if (NumberUtils.isDigits(file.getName())) {
                    //TODO Сделать проверку соответствия имени сообщению
                    imageDir.put(Integer.parseInt(file.getName()), file.getAbsolutePath());
                }
                getImagePaths(Paths.get(file.getAbsolutePath()), imageDir);
            }
        }
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

    private List<File> getImageListMock(){
        List<File> imageFileList = new ArrayList<>();
        imageFileList.add(new File("E:\\work\\image1.jpg"));
        imageFileList.add(new File("E:\\work\\image1.jpg"));
        imageFileList.add(new File("E:\\work\\image1.jpg"));
        imageFileList.add(new File("E:\\work\\image1.jpg"));
        return imageFileList;
    }
}
