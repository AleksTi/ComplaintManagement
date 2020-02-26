package ru.yandex.sashanc.db;

import org.apache.log4j.Logger;
import ru.yandex.sashanc.db.connection.ConnectionManagerImpl;
import ru.yandex.sashanc.db.connection.IConnectionManager;
import ru.yandex.sashanc.pojo.Part;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PartDaoImpl implements IPartDao {
    private static final Logger logger = Logger.getLogger(ComplaintDaoImpl.class);
    private IConnectionManager conManager = ConnectionManagerImpl.getInstance();

    @Override
    public Part getContractPart(int supplierNumber, String partNumber){
        Part part = null;
        try (Connection connection = conManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM parts WHERE partNumber = ? AND supplierId = ?")) {
                statement.setString(1, partNumber);
                statement.setInt(2, supplierNumber);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        part = new Part();
                        part.setPartNumber(partNumber);
                        part.setPartNameRu(resultSet.getString("partNameRu"));
                        part.setPartNameEn(resultSet.getString("partNameEn"));
                        part.setAltPartNumber(resultSet.getString("altPartNumber"));
                        part.setSupplierPartNumber(resultSet.getString("SupplierPartNumber"));
                        part.setSupplierId(supplierNumber);
                    }
                }
            }
        }
        catch (SQLException e) {
            logger.info("Ошибка получения данных из таблицы parts: " + e);
        }
        return part;
    }
}
