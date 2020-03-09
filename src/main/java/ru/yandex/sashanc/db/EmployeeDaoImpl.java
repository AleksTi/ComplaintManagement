package ru.yandex.sashanc.db;

import org.apache.log4j.Logger;
import ru.yandex.sashanc.db.connection.ConnectionManagerImpl;
import ru.yandex.sashanc.pojo.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EmployeeDaoImpl implements IEmployeeDao {
    private static final Logger logger = Logger.getLogger(ComplaintDaoImpl.class);
    private IConnectionManager conManager = ConnectionManagerImpl.getInstance();

    public Map<String, Employee> getEmployeeList() {
        Map<String, Employee> employees = new HashMap<>();
        Employee employee;
        try (Connection connection = conManager.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        employee = new Employee();
                        employee.setId(resultSet.getString("id"));
                        employee.setDepartment(resultSet.getString("department"));
                        employee.setSurnameRu(resultSet.getString("surnameRu"));
                        employee.setNameRu(resultSet.getString("nameRu"));
                        employee.setSurnameEn(resultSet.getString("surnameEn"));
                        employee.setNameEn(resultSet.getString("nameEn"));
                        employee.seteMail(resultSet.getString("e-mail"));
                        employee.setPositionRu(resultSet.getString("positionRu"));
                        employee.setPositionEn(resultSet.getString("positionEn"));
                        employees.put(employee.getId().toLowerCase(), employee);
                    }
                }
            }
        }
        catch (SQLException e) {
            logger.info("Ошибка получения данных из таблицы parts: " + e);
        }
        return employees;
    }
}
