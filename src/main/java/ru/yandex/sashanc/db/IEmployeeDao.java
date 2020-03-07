package ru.yandex.sashanc.db;

import ru.yandex.sashanc.pojo.Employee;

import java.util.Map;

public interface IEmployeeDao {
    Map<String,Employee> getEmployeeList();
}
