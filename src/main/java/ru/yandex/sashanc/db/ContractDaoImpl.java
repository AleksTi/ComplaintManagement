package ru.yandex.sashanc.db;

import org.apache.log4j.Logger;
import ru.yandex.sashanc.db.connection.ConnectionManagerImpl;
import ru.yandex.sashanc.db.connection.IConnectionManager;
import ru.yandex.sashanc.pojo.Contract;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContractDaoImpl implements IContractDao {

    private static final Logger logger = Logger.getLogger(ComplaintDaoImpl.class);
    private IConnectionManager conManager = ConnectionManagerImpl.getInstance();

    @Override
    public Contract getContract(int supplierNumber) {
        Contract contract = null;
        try (Connection connection = conManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM contracts WHERE suppliers_id = ?")) {
                statement.setInt(1, supplierNumber);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        contract = new Contract();
                        contract.setSupplierId(supplierNumber);
                        contract.setSupplierSapName(resultSet.getString("supplierSapName"));
                        contract.setEmployeeId(resultSet.getString("employees_id").toLowerCase());
                        contract.setEmployeeShortName(resultSet.getString("employeeShortName"));
                        contract.setEmployeeIdQuality(resultSet.getString("employees_id_quality").toLowerCase());
                        contract.setEmployeeShortNameQuality(resultSet.getString("employeeShortName_quality"));
                        contract.setContractName(resultSet.getString("contractName"));
                        contract.setSupplierShortName(resultSet.getString("supplierShortName"));
                        contract.setComplaintTitle(resultSet.getString("complaintTitle"));
                        contract.setContractNumber(resultSet.getString("contractNumber"));
                        contract.setDate(resultSet.getDate("date").toLocalDate());
                        contract.setPaymentItem(resultSet.getString("paymentItem"));
                        contract.setResearchItem(resultSet.getString("researchItem"));
                        contract.setReturnTerms(resultSet.getString("returnTerms"));
                        //TODO ppm м.б. n/a
//                        contract.setPpmTarget(resultSet.getInt(" ppmTarget"));
                        contract.setBank(resultSet.getString("bank"));
                        contract.setContractAdress(resultSet.getString("contractAdress"));
                    }
                }
            }
        }
        catch (SQLException e) {
            logger.info("Ошибка получения данных из таблицы contracts: " + e);
        }
        return contract;
    }
}
