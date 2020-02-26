package ru.yandex.sashanc.db;

import ru.yandex.sashanc.pojo.Contract;

public interface IContractDao {
    Contract getContract(int supplierNumber);
}
