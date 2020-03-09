package ru.yandex.sashanc.db;

import java.sql.Connection;

public interface IConnectionManager {
    Connection getConnection();
}
