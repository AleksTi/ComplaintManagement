package ru.yandex.sashanc.db.connection;

import org.apache.log4j.Logger;
import ru.yandex.sashanc.Main;
import ru.yandex.sashanc.db.IConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class ConnectionManagerImpl implements IConnectionManager {
    private static final Logger logger = Logger.getLogger(ConnectionManagerImpl.class);
    private static IConnectionManager connectionManager;

    private ConnectionManagerImpl(){

    }

    public static IConnectionManager getInstance(){
        if (connectionManager == null) {
            connectionManager = new ConnectionManagerImpl();
        }
        return connectionManager;
    }

    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + getSettings() + ";memory=false");

        } catch (SQLException e) {
            logger.warn("Ошибка при подключении к БД", e);
        } catch (ClassNotFoundException e) {
            logger.warn("Класс драйвера БД не найден", e);
        }
        return connection;
    }

    private String getSettings() {
        logger.info("Method getSettings is launched...");
        String settingsFile = "";
        try (Scanner sc = new Scanner(Main.class.getResourceAsStream("/settings/settings.txt"))) {
            while (sc.hasNext()) {
                settingsFile = sc.nextLine();
                logger.info("Settings file: " + settingsFile);
            }
        } catch (Exception e) {
            logger.warn(e);
        }
        return settingsFile;
    }
}
