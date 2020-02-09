package ru.yandex.sashanc.db;

import java.io.File;
import java.util.List;

public interface IimageDao {
    List<File> getImageList(int notId);
}
