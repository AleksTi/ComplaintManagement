package ru.yandex.sashanc.db.connection;

import java.sql.Connection;

public interface IConnectionManager {
    Connection getConnection();
}
