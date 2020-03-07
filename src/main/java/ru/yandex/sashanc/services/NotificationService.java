package ru.yandex.sashanc.services;

import ru.yandex.sashanc.db.ImageDaoImpl;
import ru.yandex.sashanc.db.NotificationDao;
import ru.yandex.sashanc.pojo.Notification;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class NotificationService {

    public List<Notification> getListNotifications(Path resourceFile, Path resourceFileMB51){
        NotificationDao notificationDao = new NotificationDao();
        List<Notification> notifications = notificationDao.getListNotifications(resourceFile, resourceFileMB51);
        Map<Integer, Path> imagesMap = getNotImagesPath();
        for (Notification not : notifications){
            if(imagesMap.containsKey(not.getNotId())){
                not.setImageLink(imagesMap.get(not.getNotId()));
            }
        }
        return notifications;
    }

    public Map<Integer, Path> getNotImagesPath(){
        ImageDaoImpl imageDao = new ImageDaoImpl();
        return imageDao.getImagesPathsDB();
    }
}
