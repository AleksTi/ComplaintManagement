package ru.yandex.sashanc.db;

import ru.yandex.sashanc.pojo.Part;

public interface IPartDao {
    Part getContractPart(int supplierNumber, String partNumber);
}
