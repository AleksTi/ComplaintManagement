package ru.yandex.sashanc.db;

import ru.yandex.sashanc.pojo.Notification;

import java.nio.file.Path;
import java.util.List;

public interface INotificationDao {
    List<Notification> getListNotifications(Path resourceFile, Path resourceFileMB51);
}