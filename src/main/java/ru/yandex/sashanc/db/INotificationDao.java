package ru.yandex.sashanc.db;

import ru.yandex.sashanc.pojo.Notification;

import java.util.List;

public interface INotificationDao {
    List<Notification> getListNotifications(String resourceFile);
}